package com.gagandeep.nuvococontacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    DatabaseReference databaseReferenceUser;
    List<User> userList;
    ListView listView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    public SearchFragment() {
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userList.clear();
            for (DataSnapshot artistSnapShot : dataSnapshot.getChildren()) {
                User user = artistSnapShot.getValue(User.class);
                userList.add(user);

            }
            UserList adapter = new UserList(getActivity(), userList);
            listView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReferenceUser = database.getReference();
        findViews(v);


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//        MenuItem searchViewItem = menu.findItem(R.id.action_search);
//        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
//        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchViewAndroidActionBar.clearFocus();
//                Query q = FirebaseDatabase.getInstance().getReference("userinfo")
//                        .orderByChild("name").startAt(query);
//                q.addListenerForSingleValueEvent(valueEventListener);
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                Query q = FirebaseDatabase.getInstance().getReference("userinfo")
//                        .orderByChild("name").startAt(newText);
//                q.addListenerForSingleValueEvent(valueEventListener);
//                return true;
//            }
//        });
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        MenuItem searchViewItem =    menu.findItem(R.id.action_search);
        switch (item.getItemId()) {
            case R.id.action_search:
                final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(item);
                searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchViewAndroidActionBar.clearFocus();
                        Query q = FirebaseDatabase.getInstance().getReference("userinfo")
                                .orderByChild("name").startAt(query);
                        q.addListenerForSingleValueEvent(valueEventListener);

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        Query q = FirebaseDatabase.getInstance().getReference("userinfo")
                                .orderByChild("name").startAt(newText);
                        q.addListenerForSingleValueEvent(valueEventListener);
                        return true;
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
    }

    private void findViews(View v) {
        listView = v.findViewById(R.id.listView);
        mCollapsingToolbarLayout = v.findViewById(R.id.collapsing);
        mCollapsingToolbarLayout.setTitleEnabled(false);


        userList = new ArrayList<>();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("userinfo");
        databaseReferenceUser.keepSynced(true);

        Toolbar actionBarToolBar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(actionBarToolBar);
        actionBarToolBar.setTitle("Search Contacts");
        actionBarToolBar.inflateMenu(R.menu.search_menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReferenceUser.addValueEventListener(valueEventListener);
    }
}
