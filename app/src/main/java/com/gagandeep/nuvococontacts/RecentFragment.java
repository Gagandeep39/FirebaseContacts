package com.gagandeep.nuvococontacts;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.gagandeep.nuvococontacts.SearchFragment.userList;

public class RecentFragment extends Fragment {
    ListView listView;
    SQLiteDatabase db;
    FavouriteDbHelper mDbHelper;
    List<FavouriteItem> itemIds;
    List<User> favouriteList;
    String[] projection = {
            BaseColumns._ID,
            FavouriteContract.Favourite.COLUMN_NAME_TITLE,
            FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE
    };

    public RecentFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recent, container, false);
//        listView = v.findViewById(R.id.listView);

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

        while (cursor.moveToNext()) {
            int itemId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FavouriteContract.Favourite._ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_TITLE));
            String itemPhone = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE));
            itemIds.add(new FavouriteItem(itemId, itemName, itemPhone));
        }
        cursor.close();

        for (int i = 0; i < userList.size(); i++) {
            for (int j = 0; j < itemIds.size(); j++) {
                if (itemIds.get(j).getName().contains(userList.get(i).getName()))
                    favouriteList.add(userList.get(i));
            }
        }

        GridView gridview = v.findViewById(R.id.gridview);
        GridViewAdapter adapter = new GridViewAdapter(getActivity(), favouriteList);
        gridview.setAdapter(adapter);
        return v;
    }


}
