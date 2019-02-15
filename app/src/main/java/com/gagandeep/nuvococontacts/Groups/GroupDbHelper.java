package com.gagandeep.nuvococontacts.Groups;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.gagandeep.nuvococontacts.Favourites.FavouriteDbHelper.DATABASE_NAME;
import static com.gagandeep.nuvococontacts.Favourites.FavouriteDbHelper.DATABASE_VERSION;

public class GroupDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + GroupContract.Group.TABLE_NAME + " (" +
                    GroupContract.Group._ID + " INTEGER PRIMARY KEY," +
                    GroupContract.Group.COLUMN_GROUP_NAME + " TEXT," +
                    GroupContract.Group.COLUMN_MEMBER_COUNT + " INTEGER, " +
                    GroupContract.Group.COLUMN_MEMBERS_NUMBER + " TEXT)";

    public GroupDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static int groupmemberCount(String members) {
        String[] arrOfStr = members.split(",");
        return arrOfStr.length;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveToLocalDatabase(String name, String groupMembers, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        int memberCount = groupmemberCount(groupMembers);
        contentValues.put(GroupContract.Group.COLUMN_GROUP_NAME, name);
        contentValues.put(GroupContract.Group.COLUMN_MEMBERS_NUMBER, groupMembers);
        contentValues.put(GroupContract.Group.COLUMN_MEMBER_COUNT, memberCount);
        database.insert(GroupContract.Group.TABLE_NAME, null, contentValues);
    }

    public Cursor readFromLocalDatabase(SQLiteDatabase database) {
        String[] projection = {GroupContract.Group._ID, GroupContract.Group.COLUMN_GROUP_NAME, GroupContract.Group.COLUMN_MEMBERS_NUMBER, GroupContract.Group.COLUMN_MEMBER_COUNT};
        return database.query(GroupContract.Group.TABLE_NAME, projection, null, null, null, null, null);
    }

    public void updateLocalDatabase(String name, String groupMembers, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GroupContract.Group.COLUMN_GROUP_NAME, name);
        contentValues.put(GroupContract.Group.COLUMN_MEMBERS_NUMBER, groupMembers);
        String selection = GroupContract.Group.COLUMN_GROUP_NAME + " LIKE ?";
        String[] selection_args = {name};
        database.update(GroupContract.Group.TABLE_NAME, contentValues, selection, selection_args);
    }
}
