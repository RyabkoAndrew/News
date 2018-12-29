package com.example.dryunchik.news.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dryunchik on 01.05.2018.
 */

public final class NewsContract {
    private NewsContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.dryunchik.news";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NEWS = "news";
    public static final String PATH_NEWS_ID = "news/#";

    public static class NewsEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_NEWS);

        public static final String TABLE_NAME = "news";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGE = "imageUrl";
        public static final String COLUMN_NAME_URL_ADDRESS = "urlAddress";
    }
}
