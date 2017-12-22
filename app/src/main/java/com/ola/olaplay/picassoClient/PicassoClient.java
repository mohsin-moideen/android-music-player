package com.ola.olaplay.picassoClient;

import android.content.Context;
import android.widget.ImageView;
import com.ola.olaplay.R;
import com.squareup.picasso.Picasso;

/**
 * Created by MohsinM on 17-12-2017.
 *
 * Picasso client to download images and convert to resource
 */

public class PicassoClient {
    public static void downloadImage(Context c,String url,ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.no_img).into(img);
        }else
        {
            Picasso.with(c).load(R.drawable.no_img).into(img);
        }
    }
}