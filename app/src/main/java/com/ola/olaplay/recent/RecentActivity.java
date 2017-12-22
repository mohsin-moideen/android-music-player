package com.ola.olaplay.recent;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ola.olaplay.R;
import com.ola.olaplay.models.Song;
import com.ola.olaplay.mDataBase.DBAdapter;
import com.ola.olaplay.models.Recent;
import com.ola.olaplay.listView.RecentActivityAdapter;
import java.util.ArrayList;
import java.util.Collections;

public class RecentActivity extends AppCompatActivity {
    ListView lv;
    SearchView sv;
    Button saveBtn,retrieveBtn;
    RecentActivityAdapter adapter;
    ArrayList<Recent> RecentActivitys=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        lv= (ListView) findViewById(R.id.recent_list);
        getRecentActivitys(null);
       // sv= (SearchView) findViewById(R.id.sv);
        //save("Me");
        Collections.reverse(RecentActivitys);
        adapter=new RecentActivityAdapter(this,RecentActivitys);
        lv.setAdapter(adapter);
    /*    sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                getRecentActivitys(newText);
                return false;
            }
        });*/
    }
  /*  private void displayDialog()
    {
        Dialog d=new Dialog(this);
        d.setTitle("SQLite Database");
        d.setContentView(R.layout.dialog_layout);
        nameEditText= (EditText) d.findViewById(R.id.nameEditTxt);
        saveBtn= (Button) d.findViewById(R.id.saveBtn);
        retrieveBtn= (Button) d.findViewById(R.id.retrieveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(nameEditText.getText().toString());
            }
        });
        retrieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecentActivitys(null);
            }
        });
        d.show();
    }*/

    /*public void save(String name)
    {
        DBAdapter db=new DBAdapter(this);
        db.openDB();
        if(db.add(name))
        {
            Log.e("done","saved");
        }else {
            Toast.makeText(this,"Unable To Save",Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
        this.getRecentActivitys(null);
    }*/

    public void save(Song song, String activity, Context c){

        DBAdapter db=new DBAdapter(c);
        db.openDB();
        if(db.add(song.getTitle(),song.getCover_image(),activity,song.getArtists()))
        {
            Log.e("done","saved");
        }else {
            Toast.makeText(this,"Unable To Save",Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
    }
    private void getRecentActivitys(String searchTerm)
    {
        RecentActivitys.clear();
        DBAdapter db=new DBAdapter(getApplicationContext());
        db.openDB();
        Recent recent=null;
        Cursor c=db.retrieve();
        while (c.moveToNext())
        {
            int id=c.getInt(0);
            String name=c.getString(1);
            String artists=c.getString(2);
            String cover_image=c.getString(3);
            String activity=c.getString(4);

            recent=new Recent();
            recent.setSongTitle(name);
            recent.setArtists(artists);
            recent.setCover_image(cover_image);
            recent.setActivity(activity);
            //p.setT(name);
            RecentActivitys.add(recent);
        }
        db.closeDB();
        lv.setAdapter(adapter);
    }
}