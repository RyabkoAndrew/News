package com.example.dryunchik.news.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Dryunchik on 01.05.2018.
 */

public class NewsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "news.db";


    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
                NewsContract.NewsEntry.TABLE_NAME + "(" +
                NewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsContract.NewsEntry.COLUMN_NAME_IMAGE + " BLOB, " +
                NewsContract.NewsEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_NAME_URL_ADDRESS + " TEXT NOT NULL" + ");";


        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
