package com.ola.olaplay.models;

/**
 * Created by MohsinM on 17-12-2017.
 *
 * Model for Recent Activity
 */

public class Recent {
    private String songTitle;
    private String cover_image;
    private String activity;
    private String artists;

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }


}
