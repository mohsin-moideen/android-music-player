package com.ola.olaplay;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ola.olaplay.custonFields.CustomEditText;
import com.ola.olaplay.models.Song;
import com.ola.olaplay.http.HttpHandler;
import com.ola.olaplay.listView.SongListAdapter;
import com.ola.olaplay.musicPlayer.MusicPlayerActivity;
import com.ola.olaplay.recent.RecentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by MohsinM on 17-12-2017.
 *
 * Main Activity Class
 *
 */
public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    private SongListAdapter adapter;
    private int TOTAL_LIST_ITEMS;
    private final int NUM_ITEMS_PAGE = 4;
    private int noOfBtns;
    private Button [] btns;
    private CustomEditText editsearch;
    private LinearLayout btnLayout;

    List<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songList = new ArrayList<>();

        lv =  findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                RecentActivity recentActivity= new RecentActivity();
                Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                Song selectedSong=(Song)arg0.getItemAtPosition(position);
                String songUrl = selectedSong.getUrl();
                List<String> playList = adapter.getPlaylist();
                if(!adapter.getAddedSongs().contains(songUrl)){
                    playList.add(songUrl);
                }
                recentActivity.save(selectedSong,"Streamed",getApplicationContext());
                intent.putStringArrayListExtra("PLAYLIST", (ArrayList<String>) playList);
                startActivity(intent);

            }
        });
        Button recentActivityButton =findViewById(R.id.recent_button);
        recentActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecentActivity.class);
                startActivity(intent);
            }
        });
        new GetSongs().execute();

        editsearch = findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.resetCurrentList();
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }
        });
        editsearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    adapter.resetCurrentList();
                    btnLayout.setVisibility(LinearLayout.GONE);

                } else {
                    editsearch.setText(null);
                    btnLayout.setVisibility(LinearLayout.VISIBLE);
                    btns[0].performClick();
                }
            }
        });


    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetSongs extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final String waitMessage = getString(R.string.wait_messaage);
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(waitMessage);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            final String url = getString(R.string.hackerEarth_api_URL);

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray songs = new JSONArray(jsonStr);
                    for (int i = 0; i < songs.length(); i++) {
                        JSONObject c = songs.getJSONObject(i);

                        String title = c.getString("song");
                        String songUrl = c.getString("url");
                        String artists = c.getString("artists");
                        String cover_image = c.getString("cover_image");
                        songUrl = resolveURL(songUrl);
                        cover_image = resolveURL(cover_image);
                        Song song = new Song();

                        song.setTitle(title);
                        song.setUrl(songUrl);
                        song.setArtists(artists);
                        song.setCover_image(cover_image);

                        songList.add(song);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                final String connectionErrorMessage = getString(R.string.connection_error_message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                connectionErrorMessage,
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            // Updating parsed JSON data into ListView
            Btnfooter();
            adapter = new SongListAdapter(MainActivity.this,songList);
            lv.setAdapter(adapter);
            adapter.loadList(0);
            CheckBtnBackGroud(0);
        }
    }
    private String resolveURL(String shortenedURL){
        URL resolvedUrl = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        try {
            resolvedUrl = new URL(shortenedURL);
        } catch (MalformedURLException e) {
            Log.e("IOException",e.getMessage());
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) resolvedUrl.openConnection();
        } catch (IOException e) {
           Log.e("IOException",e.getMessage());
        }
        return httpURLConnection.getHeaderField("location");
    }
    private void Btnfooter()
    {
        TOTAL_LIST_ITEMS = songList.size();
        int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
        val = val==0?0:1;
        noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;

        btnLayout = findViewById(R.id.btnLay);

        btns  = new Button[noOfBtns];

        for(int i=0;i<noOfBtns;i++)
        {
            btns[i] =   new Button(this);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            BitmapDrawable bmap = (BitmapDrawable) this.getResources().getDrawable(R.drawable.pagination_selected);
            float bmapWidth = bmap.getBitmap().getWidth();
            float bmapHeight = bmap.getBitmap().getHeight();

            float wRatio = width / bmapWidth;
            float hRatio = height / bmapHeight;

            float ratioMultiplier = wRatio;
            if (hRatio < wRatio) {
                ratioMultiplier = hRatio;
            }
            int newBmapWidth = (int) (bmapWidth*ratioMultiplier / 10);
            int newBmapHeight = (int) (bmapHeight*ratioMultiplier / 10);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(newBmapWidth,newBmapHeight);
            btnLayout.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    adapter.loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }
    }
    /**
     * Method for Checking Button Backgrounds
     */
    private void CheckBtnBackGroud(int index)
    {
        for(int i=0;i<noOfBtns;i++)
        {
            if(i==index)
            {
                btns[i].setBackgroundResource(R.drawable.pagination_selected);
            }
            else
            {
                btns[i].setBackgroundResource(R.drawable.pagination_unselected);
            }
        }

    }

    @Override
    public void onBackPressed() {
        final String exitConfirmMessage = getString(R.string.exit_confirm_message);
        new AlertDialog.Builder(this)
                .setMessage(exitConfirmMessage)
                .setPositiveButton(R.string.reponse_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}