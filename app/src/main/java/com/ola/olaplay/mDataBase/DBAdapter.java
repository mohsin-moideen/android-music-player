package com.ola.olaplay.mDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;
import android.util.Log;

/**
 * Created by MohsinM on 17-12-2017.
 *
 *  DBAdapter class to run queries
 */

public class DBAdapter {
    private Context c;
    private SQLiteDatabase db;
    private DBHelper helper;
    public DBAdapter(Context c) {
        this.c = c;
        helper=new DBHelper(c);
    }
    //OPEN DB
    public void openDB()
    {
        try
        {
            db=helper.getWritableDatabase();
        }catch (SQLException e)
        {
            Log.e("SQLException" , e.getMessage());
        }
    }
    //CLOSE DB
    public void closeDB()
    {
        try
        {
            helper.close();
        }catch (SQLException e)
        {
            Log.e("SQLException" , e.getMessage());
        }
    }
    //INSERT DATA
    public boolean add(String title, String cover_image, String activity, String artists)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(Constants.TITLE, title);
            cv.put(Constants.ARTISTS, artists);
            cv.put(Constants.COVER_IMAGE, cover_image);
            cv.put(Constants.ACITIVTY, activity);

            db.insert(Constants.TB_NAME, Constants.ROW_ID, cv);
            return true;
        }catch (SQLException e)
        {
            Log.e("SQLException" , e.getMessage());
        }
        return false;
    }
    //RETRIEVE DATA
    public Cursor retrieve()
    {
        String[] columns={Constants.ROW_ID,Constants.TITLE,Constants.ARTISTS,Constants.COVER_IMAGE,Constants.ACITIVTY};
        return db.query(Constants.TB_NAME,columns,null,null,null,null,null);
    }
}
