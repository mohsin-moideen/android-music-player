package com.ola.olaplay.listView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ola.olaplay.R;

/**
 * Created by MohsinM on 17-12-2017.
 *
 *  holder class for song activity UI elements
 */

public class SongHolder {
    TextView title;
    TextView artists;
    ImageView cover_image;
    public SongHolder(View v) {
        title = (TextView) v.findViewById(R.id.title);
        artists = (TextView) v.findViewById(R.id.artists);
        cover_image = (ImageView) v.findViewById(R.id.cover_image);
    }
}