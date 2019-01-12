package com.gagandeep.nuvococontacts;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.gagandeep.nuvococontacts.LoginActivity.MY_PREFS_NAME;


public class SearchFragment extends Fragment {

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    DatabaseReference databaseReferenceUser;
    public static List<User> userList;
    ListView listView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    UserList adapter;
    ArrayList<User> sortedArrayList, backupArrayList;
    FloatingActionButton clearSearchFAB;

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
            if (getActivity() != null) {
                adapter = new UserList(getActivity(), userList);
                listView.setAdapter(adapter);
            }
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
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {


            case R.id.advanced_search:
                showAdvancedSearchDialogue();
                break;

            case R.id.add_user:
                startActivity(new Intent(getActivity(), AddUserActivity.class));
                break;

            case R.id.log_out:
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("phone", "XXXX");
                editor.apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                Toast.makeText(getActivity(), ""  + userList.size(), Toast.LENGTH_SHORT).show();
                for (int i=0; i<userList.size(); i++){
                    if (userList.get(i).getName().toLowerCase().contains(query.toLowerCase()))
                        sortedArrayList.add(userList.get(i));
                }

                adapter = new UserList(getActivity(), sortedArrayList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }

    private void showAdvancedSearchDialogue() {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = getLayoutInflater().inflate(R.layout.advanced_search_dialogue, null);
        dialogueBuilder.setView(dialogueView);

        final TextInputEditText editTextName, editTextLocation, editTextDepartment, editTextDesignation;
        Button searchButton;
        editTextName = dialogueView.findViewById(R.id.editTextName);
        editTextLocation = dialogueView.findViewById(R.id.editTextLocation);
        editTextDepartment = dialogueView.findViewById(R.id.editTextDepartment);
        editTextDesignation = dialogueView.findViewById(R.id.editTextDesignation);
        searchButton = dialogueView.findViewById(R.id.searchButton);

        dialogueBuilder.setTitle("Search");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();

        sortedArrayList = new ArrayList<>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().toLowerCase();
                String location = editTextLocation.getText().toString().toLowerCase();
                String department = editTextDepartment.getText().toString().toLowerCase();
                String designation = editTextDesignation.getText().toString().toLowerCase();
                sortedArrayList.addAll(userList);

                Log.i(TAG, "onClick: " + sortedArrayList.size());
                for (int i = 0; i < sortedArrayList.size(); i++) {
                    if (!TextUtils.isEmpty(name)) {
                        if (!sortedArrayList.get(i).getName().toLowerCase().contains(name)) {
                            sortedArrayList.remove(i);
                            i--;
                            continue;
                        }

                    }
                    if (!TextUtils.isEmpty(location)) {
                        if (!sortedArrayList.get(i).getLocation().toLowerCase().contains(location)) {
                            sortedArrayList.remove(i);
                            i--;
                            continue;
                        }

                    }
                    if (!TextUtils.isEmpty(department)) {
                        if (!sortedArrayList.get(i).getDepartment().toLowerCase().contains(department)) {
                            sortedArrayList.remove(i);
                            i--;
                            continue;
                        }

                    }
                    if (!TextUtils.isEmpty(designation)) {
                        if (!sortedArrayList.get(i).getDesignation().toLowerCase().contains(designation)) {
                            sortedArrayList.remove(i);
                            i--;
                        }

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


    private void findViews(View v) {
        listView = v.findViewById(R.id.listView);
        clearSearchFAB = v.findViewById(R.id.fab);
        sortedArrayList  = new ArrayList<>();

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

        backupArrayList = new ArrayList<>();
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
