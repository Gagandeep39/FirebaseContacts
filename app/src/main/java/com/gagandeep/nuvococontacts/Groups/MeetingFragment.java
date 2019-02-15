package com.gagandeep.nuvococontacts.Groups;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gagandeep.nuvococontacts.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MeetingFragment extends Fragment {
    DatabaseReference databaseReferenceUser;
    ArrayList<Group> groupArrayList;
    GroupDbHelper dbHelper;
    SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meeting, container, false);
        groupArrayList = new ArrayList<>();
        dbHelper = new GroupDbHelper(getContext());
        database = dbHelper.getWritableDatabase();
        dbHelper.readFromLocalDatabase(database);
        Cursor cursor = dbHelper.readFromLocalDatabase(database);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(GroupContract.Group.COLUMN_GROUP_NAME));
            String phoneNumbers = cursor.getString(cursor.getColumnIndex(GroupContract.Group.COLUMN_MEMBERS_NUMBER));
            int count = cursor.getInt(cursor.getColumnIndex(GroupContract.Group.COLUMN_MEMBER_COUNT));
            int id = cursor.getInt(cursor.getColumnIndex(GroupContract.Group._ID));
            groupArrayList.add(new Group(id, name, phoneNumbers, count));

        }

        return v;
    }

    private void findViews(View v) {
        Toolbar actionBarToolBar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(actionBarToolBar);
        actionBarToolBar.setTitle("Conferences & Meetings");
        //actionBarToolBar.inflateMenu(R.menu.search_menu);
    }
}
