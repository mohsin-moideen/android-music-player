package com.ola.olaplay.listView;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ola.olaplay.models.Song;
import com.ola.olaplay.R;
import com.ola.olaplay.recent.RecentActivity;
import com.ola.olaplay.picassoClient.PicassoClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
/**
 * Created by MohsinM on 17-12-2017.
 *
 *  custom adapter for songs listview.
 */
public class SongListAdapter extends BaseAdapter  {
    private Context c;
    private List<String> playlist;
    private List<Song> songs;
    private LayoutInflater inflater;
    private Song song;
    private List<Song> currentList;
    private Set<String> addedSongs;

    public Set<String> getAddedSongs(){
        return addedSongs;
    }
    public List<String> getPlaylist() {
        playlist.clear();
        playlist.addAll(addedSongs);
        return playlist;
    }

    public SongListAdapter(Context c, List<Song> allsongs) {
        this.playlist = new ArrayList<>();
        this.addedSongs = new HashSet<>();
        this.c = c;
        this.songs = allsongs;
        this.currentList = new ArrayList<>();
        currentList.addAll(songs);
    }

    @Override
    public int getCount() {
        return currentList.size();
    }
    @Override
    public Object getItem(int position) {
        return currentList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(inflater==null)
        {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.song_row_item,parent,false);
        }
        //BIND DATA
        SongHolder holder=new SongHolder(convertView);
        song = currentList.get(position);
        holder.title.setText(song.getTitle());
        holder.artists.setText(song.getArtists());
        PicassoClient.downloadImage(c,song.getCover_image(),holder.cover_image);
        ToggleButton toggleButton = (ToggleButton) convertView.findViewById(R.id.playlist_toggle) ;
        toggleButton.setOnCheckedChangeListener(null);
        if(addedSongs.contains(song.getUrl())){
           toggleButton.setChecked(true);
        }
        else{
            toggleButton.setChecked(false);
        }
        Button downloadButton =  convertView.findViewById(R.id.download);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RecentActivity recentActivity= new RecentActivity();
                song = currentList.get(position);
                recentActivity.save(song,"downloaded", c);
                downloadFile(song);
                Toast toast= Toast.makeText(v.getContext(),"Downloading",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });

        toggleButton.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                song = currentList.get(position);
                if(isChecked){
                    addToPlaylist(song);
                }
               else{
                    removeFromPlaylist(song);
                }
            }
        }) ;

        return convertView;
    }

    private void addToPlaylist(Song song) {
        addedSongs.add(song.getUrl());
    }
    private void removeFromPlaylist(Song song) {
        addedSongs.remove(song.getUrl());
    }
    private void downloadFile(Song song) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(song.getUrl()));
        request.setDescription(song.getArtists());
        request.setTitle(song.getTitle());
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String downloadDirectoryPath = c.getApplicationContext().getFilesDir().getPath();
        File fileDir = new File(downloadDirectoryPath);
        if (!fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        request.setDestinationInExternalFilesDir(c.getApplicationContext(),downloadDirectoryPath,song.getTitle()+".mp3");
        Log.e("path", c.getApplicationContext().getFilesDir().getPath());
        DownloadManager manager = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        currentList.clear();
        if (charText.length() == 0) {
            currentList.addAll(songs);
        }
        else
        {
            for (Song song : songs)
            {
                if (song.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    currentList.add(song);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void loadList(int number) {
        final int NUM_ITEMS_PAGE   = 4;
        currentList.clear();
        int start = number * NUM_ITEMS_PAGE;
        for (int i = start; i < (start) + NUM_ITEMS_PAGE; i++) {
            if (i < songs.size()) {
                currentList.add(songs.get(i));
            } else {
                break;
            }
        }
        notifyDataSetChanged();
    }
    public void resetCurrentList(){
        currentList.clear();
        currentList.addAll(songs);
    }

    }
