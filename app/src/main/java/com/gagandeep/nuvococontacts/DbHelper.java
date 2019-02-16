package com.gagandeep.nuvococontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gagandeep.nuvococontacts.Favourites.FavouriteContract;
import com.gagandeep.nuvococontacts.Groups.GroupContract;

import static com.gagandeep.nuvococontacts.Favourites.FavouriteContract.Favourite.TABLE_FAV;
import static com.gagandeep.nuvococontacts.Groups.GroupContract.Group.TABLE_GROUP;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String SQL_CREATE_FAV_TABLE =
            "CREATE TABLE " + TABLE_FAV + " (" +
                    FavouriteContract.Favourite._ID + " INTEGER PRIMARY KEY, " +
                    FavouriteContract.Favourite.COLUMN_FAV_NAME + " TEXT, " +
                    FavouriteContract.Favourite.COLUMN_FAV_PHONE + " TEXT )";

    private static final String SQL_CREATE_GROUP_TABLE =
            "CREATE TABLE " + TABLE_GROUP + " (" +
                    GroupContract.Group._ID + " INTEGER PRIMARY KEY, " +
                    GroupContract.Group.COLUMN_GROUP_NAME + " TEXT, " +
                    GroupContract.Group.COLUMN_MEMBER_COUNT + " INTEGER, " +
                    GroupContract.Group.COLUMN_MEMBER_NAME + " TEXT, " +
                    GroupContract.Group.COLUMN_MEMBERS_NUMBER + " TEXT )";




    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_FAV;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static int groupmemberCount(String members) {
        String[] arrOfStr = members.split(",");
        return arrOfStr.length;
    }

    // To create a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAV_TABLE);
        db.execSQL(SQL_CREATE_GROUP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);

        // create new tables
        onCreate(db);
    }

    public void saveToGroupTable(String name, String groupMembersName, String groupMembersNumber, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        int memberCount = groupmemberCount(groupMembersNumber);
        contentValues.put(GroupContract.Group.COLUMN_GROUP_NAME, name);
        contentValues.put(GroupContract.Group.COLUMN_MEMBER_NAME, groupMembersName);
        contentValues.put(GroupContract.Group.COLUMN_MEMBERS_NUMBER, groupMembersNumber);
        contentValues.put(GroupContract.Group.COLUMN_MEMBER_COUNT, memberCount);
        database.insert(TABLE_GROUP, null, contentValues);
    }

    public Cursor readFromGroupTable(SQLiteDatabase database) {
        String[] projection = {GroupContract.Group._ID, GroupContract.Group.COLUMN_GROUP_NAME, GroupContract.Group.COLUMN_MEMBERS_NUMBER, GroupContract.Group.COLUMN_MEMBER_COUNT};
        return database.query(TABLE_GROUP, projection, null, null, null, null, null);
    }

    public void updateGroupTable(String name, String groupMembersName, String groupMembersNumber, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GroupContract.Group.COLUMN_GROUP_NAME, name);
        contentValues.put(GroupContract.Group.COLUMN_MEMBER_NAME, groupMembersName);
        contentValues.put(GroupContract.Group.COLUMN_MEMBERS_NUMBER, groupMembersNumber);
        String selection = GroupContract.Group.COLUMN_GROUP_NAME + " LIKE ?";
        String[] selection_args = {name};
        database.update(TABLE_GROUP, contentValues, selection, selection_args);
    }
}
