package com.hhdev.milan.popularmovies;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    public static final String AUTHORITY = "com.hhdev.milan.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String FAVORITES_PATH = "favorites";

    public static final class Favorites implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FAVORITES_PATH).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_DATA = "data";
        public static final String COLUMN_MOVIE_ID = "movie_id";

    }

}
