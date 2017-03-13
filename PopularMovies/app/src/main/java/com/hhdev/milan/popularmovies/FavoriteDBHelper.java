package com.hhdev.milan.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoriteContract.Favorites.TABLE_NAME + " (" +
                FavoriteContract.Favorites._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.Favorites.COLUMN_DATA + " TEXT NOT NULL + " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i, int i1){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FavoriteContract.Favorites.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
