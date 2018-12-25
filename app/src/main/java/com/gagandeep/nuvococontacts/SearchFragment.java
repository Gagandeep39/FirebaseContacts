package com.gagandeep.nuvococontacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    List<User> userList, sortedArrayList;
    ListView listView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    AdvancedSearch search;
    FloatingActionButton clearSearchFAB;
    UserList adapter;

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
            adapter = new UserList(getActivity(), userList);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

                        return false;
                    }
                });
                break;
            case R.id.advanced_search:
                showUpdateDialogue();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findViews(View v) {
        listView = v.findViewById(R.id.listView);
        clearSearchFAB = v.findViewById(R.id.fab);
        clearSearchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new UserList(getActivity(), userList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                clearSearchFAB.hide();
            }
        });

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

    private void showUpdateDialogue() {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = getLayoutInflater().inflate(R.layout.advanced_search_dialogue, null);
        dialogueBuilder.setView(dialogueView);

        final TextInputEditText editTextName, editTextLocation, editTextDepartment, editTextPhone;
        Button searchButton;
        editTextName = dialogueView.findViewById(R.id.editTextName);
        editTextLocation = dialogueView.findViewById(R.id.editTextLocation);
        editTextDepartment = dialogueView.findViewById(R.id.editTextDepartment);
        editTextPhone = dialogueView.findViewById(R.id.editTextPhone);
        searchButton = dialogueView.findViewById(R.id.searchButton);

        dialogueBuilder.setTitle("Search");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();

        sortedArrayList = new ArrayList<>();
        search = new AdvancedSearch();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String location = editTextLocation.getText().toString();
                String department = editTextDepartment.getText().toString();
                int counter;

                for (int i = 0; i < userList.size(); i++) {
                    counter = 0;
                    if (!TextUtils.isEmpty(name)) {
                        if (userList.get(i).getName().contains(name)) {
                            Toast.makeText(getActivity(), "LOL", Toast.LENGTH_SHORT).show();
                            counter++;
                        }
                    }
                    if (!TextUtils.isEmpty(location)) {
                        if (userList.get(i).getLocation().contains(location))
                            counter++;
                    }
                    if (!TextUtils.isEmpty(department)) {
                        if (userList.get(i).getDesignation().contains(department))
                            counter++;
                    }


                    if (counter > 0) {
                        Toast.makeText(getActivity(), "" + userList.get(i), Toast.LENGTH_SHORT).show();
                        sortedArrayList.add(userList.get(i));
                    }
                }

                adapter = new UserList(getActivity(), sortedArrayList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                clearSearchFAB.show();


                alertDialog.dismiss();
            }
        });


    }
}
