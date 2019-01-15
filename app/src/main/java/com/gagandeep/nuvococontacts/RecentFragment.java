package com.gagandeep.nuvococontacts;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static com.gagandeep.nuvococontacts.SearchFragment.userList;

public class RecentFragment extends Fragment {
    ListView listView;
    GridView gridview;
    SQLiteDatabase db;
    FavouriteDbHelper mDbHelper;
    List<FavouriteItem> itemIds;
    List<User> favouriteList;
    GridViewAdapter adapter;
    String[] projection = {
            BaseColumns._ID,
            FavouriteContract.Favourite.COLUMN_NAME_TITLE,
            FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE
    };


    public RecentFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recent, container, false);
        gridview = v.findViewById(R.id.gridview);


        mDbHelper = new FavouriteDbHelper(getContext());
        itemIds = new ArrayList<>();
        favouriteList = new ArrayList<>();
        db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FavouriteContract.Favourite.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        Log.e(TAG, "onCreateView: " + cursor);
        while (cursor.moveToNext()) {
            int itemId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FavouriteContract.Favourite._ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_TITLE));
            String itemPhone = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE));
            itemIds.add(new FavouriteItem(itemId, itemName, itemPhone));
        }
        cursor.close();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getActivity(), itemIds.size() + " " + userList.size(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < userList.size(); i++) {
                    for (int j = 0; j < itemIds.size(); j++) {
                        if (itemIds.get(j).getName().equals(userList.get(i).getFirstName())) {
                            favouriteList.add(userList.get(i));
                        }
                    }

                }

                adapter = new GridViewAdapter(getActivity(), favouriteList);
//                Toast.makeText(getActivity(), "" + adapter.getCount(), Toast.LENGTH_SHORT).show();
                gridview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }, 500);   //2000ms->2s

        return v;
    }


}
