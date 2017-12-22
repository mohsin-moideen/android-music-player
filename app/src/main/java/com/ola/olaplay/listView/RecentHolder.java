package com.ola.olaplay.listView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ola.olaplay.R;

/**
 * Created by MohsinM on 17-12-2017.
 *
 *  holder class for recent activity UI elements
 */

public class RecentHolder {
    TextView title;
    TextView artists;
    TextView activity;
    ImageView cover_image;
    public RecentHolder(View v) {
        title = (TextView) v.findViewById(R.id.recent_title);
        artists = (TextView) v.findViewById(R.id.recent_artists);
        activity = (TextView) v.findViewById(R.id.recent_activity);
        cover_image = (ImageView) v.findViewById(R.id.recent_cover_image);
    }
}
