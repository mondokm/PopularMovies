package com.hhdev.milan.popularmovies;

import android.provider.BaseColumns;

public class FavoriteContract {

    public static final class Favorites implements BaseColumns{

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_DATA = "data";

    }

}
