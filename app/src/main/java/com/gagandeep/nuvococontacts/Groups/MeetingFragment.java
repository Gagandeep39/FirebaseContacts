package com.gagandeep.nuvococontacts.Groups;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gagandeep.nuvococontacts.DbHelper;
import com.gagandeep.nuvococontacts.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MeetingFragment extends Fragment {
    DatabaseReference databaseReferenceUser;
    ArrayList<Group> groupArrayList;
    DbHelper dbHelper;
    SQLiteDatabase database;
    ListView listView;
    GroupAdapter groupAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meeting, container, false);
        findViews(v);
        readFromDatabase();


        return v;
    }

    private void readFromDatabase() {
        groupArrayList = new ArrayList<>();
        dbHelper = new DbHelper(getActivity());
        database = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.readFromGroupTable(database);

        if (cursor.getCount() > 0)
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(GroupContract.Group.COLUMN_GROUP_NAME));
                String phoneNumbers = cursor.getString(cursor.getColumnIndex(GroupContract.Group.COLUMN_MEMBERS_NUMBER));
                String memberName = cursor.getString(cursor.getColumnIndex(GroupContract.Group.COLUMN_MEMBER_NAME));

                int count = cursor.getInt(cursor.getColumnIndex(GroupContract.Group.COLUMN_MEMBER_COUNT));
                int id = cursor.getInt(cursor.getColumnIndex(GroupContract.Group._ID));
                groupArrayList.add(new Group(id, name, phoneNumbers, memberName, count));
            }

        groupAdapter = new GroupAdapter(getActivity(), groupArrayList);
        listView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

    private void findViews(View v) {
        Toolbar actionBarToolBar = v.findViewById(R.id.toolbar);
        listView = v.findViewById(R.id.listView);
        ((AppCompatActivity) getActivity()).setSupportActionBar(actionBarToolBar);
        actionBarToolBar.setTitle("Groups");
        //actionBarToolBar.inflateMenu(R.menu.search_menu);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden){

//        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readFromDatabase();
            }
        }, 200);   //2000ms->2s
    }

    @Override
    public void onResume() {
        super.onResume();
        readFromDatabase();
    }
}
