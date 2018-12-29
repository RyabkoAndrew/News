package com.example.dryunchik.news.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.dryunchik.news.R;

/**
 * Created by Dryunchik on 02.05.2018.
 */

public class NewsProvider extends ContentProvider {

    public static final String LOG_TAG = NewsProvider.class.getSimpleName();
    private NewsDbHelper mDbHelper;

    private static final int NEWS = 100;
    private static final int NEWS_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS, NEWS);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS_ID, NEWS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case NEWS:
                cursor = db.query(NewsContract.NewsEntry.TABLE_NAME, projection, selection, selectionArgs
                        , null, null, sortOrder);
                break;

            case NEWS_ID:
                String select = NewsContract.NewsEntry._ID + "=?";
                String[] selectArg = {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(NewsContract.NewsEntry.TABLE_NAME, projection, select, selectArg
                        , null, null, sortOrder);
                break;
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case NEWS:
                return insetNews(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    private Uri insetNews(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long newRowId = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, contentValues);

        if (newRowId != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        if (newRowId == 0) {
            Log.v(LOG_TAG, "Failed for insert row for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, newRowId);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int numberOfRowsDelete = 0;

        switch (match) {
            case NEWS:
                numberOfRowsDelete = db.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                Toast.makeText(getContext(), R.string.all_news_deleted, Toast.LENGTH_SHORT).show();
                break;
            case NEWS_ID:
                String where = NewsContract.NewsEntry._ID + "=?";
                String[] whereArg = {String.valueOf(ContentUris.parseId(uri))};

                numberOfRowsDelete = db.delete(NewsContract.NewsEntry.TABLE_NAME, where, whereArg);
                Toast.makeText(getContext(), R.string.news_deleted, Toast.LENGTH_SHORT).show();
                break;
        }
        if (numberOfRowsDelete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        if (numberOfRowsDelete < 0) {
            Toast.makeText(getContext(), R.string.error_delete, Toast.LENGTH_SHORT).show();
        }
        return numberOfRowsDelete;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
