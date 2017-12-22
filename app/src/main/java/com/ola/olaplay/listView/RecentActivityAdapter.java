package com.ola.olaplay.listView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ola.olaplay.picassoClient.PicassoClient;
import com.ola.olaplay.models.Recent;
import com.ola.olaplay.R;
import java.util.ArrayList;
/**
 * Created by MohsinM on 17-12-2017.
 *
 * custom adapter for recent activity listview.
 */

public class RecentActivityAdapter extends BaseAdapter {
    private Context c;
    private ArrayList<Recent> recents;
    private LayoutInflater inflater;
    public RecentActivityAdapter(Context c, ArrayList<Recent> recents) {
        this.c = c;
        this.recents = recents;
    }
    @Override
    public int getCount() {
        return recents.size();
    }
    @Override
    public Object getItem(int position) {
        return recents.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null)
        {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.recent_row_item,parent,false);
        }
        RecentHolder rHolder = new RecentHolder(convertView);
        TextView songTitle= rHolder.title;
        TextView artists= rHolder.artists;
        TextView activity= rHolder.activity;
        PicassoClient.downloadImage(c, recents.get(position).getCover_image(),rHolder.cover_image);
        songTitle.setText(recents.get(position).getSongTitle());
        artists.setText(recents.get(position).getArtists());
        activity.setText(recents.get(position).getActivity());
        final int pos=position;

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, recents.get(pos).getSongTitle(),Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
