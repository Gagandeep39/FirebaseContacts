package com.gagandeep.nuvococontacts.Favourites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouriteDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FavouriteContract.Favourite.TABLE_NAME + " (" +
                    FavouriteContract.Favourite._ID + " INTEGER PRIMARY KEY," +
                    FavouriteContract.Favourite.COLUMN_LOCAL_NAME + " TEXT," +
                    FavouriteContract.Favourite.COLUMN_LOCAL_PHONE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavouriteContract.Favourite.TABLE_NAME;

    public FavouriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // To create a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
