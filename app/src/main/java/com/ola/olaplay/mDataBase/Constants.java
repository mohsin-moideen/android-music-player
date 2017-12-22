package com.ola.olaplay.mDataBase;

/**
 * Created by MohsinM on 17-12-2017.
 *
 *  DB contants
 */

public class Constants {
    static final String ROW_ID="id";
    static final String TITLE="Title";
    static final String ARTISTS="Artists";
    static final String COVER_IMAGE="Cover_image";
    static final String ACITIVTY="Activity";

    //DB
    static final String DB_NAME="OLAPLAY_DB";
    static final String TB_NAME="OLAPLAY_TABLE";
    static final int DB_VERSION=1;
    //CREATE TB
    static final String CREATE_TB="CREATE TABLE "+TB_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT,Title TEXT NOT NULL, Artists TEXT NOT NULL,Cover_image TEXT NOT NULL,Activity TEXT NOT NULL)";
    //DROP TB

    static final String DROP_TB="DROP TABLE IF EXISTS "+TB_NAME;
}
