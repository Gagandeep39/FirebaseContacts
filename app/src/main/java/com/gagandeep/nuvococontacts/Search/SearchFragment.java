package com.gagandeep.nuvococontacts.Search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gagandeep.nuvococontacts.ChangelogActivity;
import com.gagandeep.nuvococontacts.Login.LoginActivity;
import com.gagandeep.nuvococontacts.R;
import com.gagandeep.nuvococontacts.User;
import com.gagandeep.nuvococontacts.UserAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.gagandeep.nuvococontacts.Helpers.Constants.PACKAGE_NAME;
import static com.gagandeep.nuvococontacts.Helpers.Constants.valueEventListenerCalled;


public class SearchFragment extends Fragment {

    public static final String SHARED_PREF_NAME = "mysharedpref";

    public static List<User> userList;
    public static final String KEY_NAME = "keyname";
    public static final String KEY_LOCATION = "keylocation";
    ListView listView;
    public static final String KEY_DESIGNATION = "keydesignation";
    public static final String KEY_DEPARTMENT = "keydepartment";
    public static final String KEY_DIVISION = "keydivision";
    public static ArrayList<User> sortedArrayList, backupArrayList, multiSelectList;
    public static LinearLayout linearLayoutBroadcast;
    Button button;
    DatabaseReference databaseReferenceUser;
    UserAdapter recyclerAdapter;
    FloatingActionButton clearSearchFAB, sendBrodcastFAB;
    TextView textViewCancelBroadcast, textViewSendBroadcast;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userList.clear();
            for (DataSnapshot artistSnapShot : dataSnapshot.getChildren()) {
                User user = artistSnapShot.getValue(User.class);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setItemsCanFocus(false);
                userList.add(user);
            }
            showRecyclerView();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    public SearchFragment() {
    }

    private void showRecyclerView() {
        Toast.makeText(getContext(), "" + userList.size(), Toast.LENGTH_SHORT).show();
        if (getActivity() != null) {
            Collections.sort(userList, new Comparator<User>() {
                @Override
                public int compare(User item, User t1) {
                    String s1 = item.getName();
                    String s2 = t1.getName();
                    return s1.compareToIgnoreCase(s2);
                }

            });

            recyclerAdapter = new UserAdapter(getActivity(), userList);
            TextView empty = new TextView(getContext());
            empty.setHeight(150);
            listView.addFooterView(empty);
            listView.setAdapter(recyclerAdapter);

            listView.setScrollingCacheEnabled(true);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e(TAG, "onCreateViwe: ");
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReferenceUser = database.getReference();
        findViews(v);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Log.e(TAG, "onoptionitemselected: ");
        switch (item.getItemId()) {
            case R.id.advanced_search:
                showAdvancedSearchDialogue();
                break;
            case R.id.log_out:
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.changelog:
                startActivity(new Intent(getActivity(), ChangelogActivity.class));
                break;
            case R.id.checkable:
                if (!UserAdapter.checkable) {
                    recyclerAdapter.displayAllCheckbox(true);
                    linearLayoutBroadcast.setVisibility(View.VISIBLE);


                } else {

                    recyclerAdapter.displayAllCheckbox(false);
                    item.setTitle("Send Broadcast");
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.e(TAG, "onCreateoptionmenu: ");
        inflater.inflate(R.menu.search_menu, menu);
        sortedArrayList.addAll(userList);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                // Toast.makeText(getActivity(), "No data found",  Toast.LENGTH_LONG).show();
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getName().toLowerCase().contains(query.toLowerCase()))
                        sortedArrayList.add(userList.get(i));
                }
                if (sortedArrayList.isEmpty()) {
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();
                    recyclerAdapter = new UserAdapter(getActivity(), sortedArrayList);
                    listView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                    clearSearchFAB.show();
                } else {
                    recyclerAdapter = new UserAdapter(getActivity(), sortedArrayList);
                    listView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                    clearSearchFAB.show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    void showBroadcastLayout(ArrayList<String> numberList) {

        String number = "";
        for (int i = 0; i < numberList.size(); i++) {
            number += numberList.get(i);
            if (i != numberList.size() - 1)
                number += ", ";
        }
        Toast.makeText(getContext(), "" + numberList, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = getLayoutInflater().inflate(R.layout.broadcast_dialogue, null);
        dialogueBuilder.setView(dialogueView);
        final TextInputEditText editTextMessage;
        Button sendButton, clearButton;
        editTextMessage = dialogueView.findViewById(R.id.editTextMessage);
        sendButton = dialogueView.findViewById(R.id.send);
        clearButton = dialogueView.findViewById(R.id.clear);
        dialogueBuilder.setTitle("Advance Search");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextMessage.setText("");
            }
        });

        final String finalNumber = number;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri sms_uri = Uri.parse("smsto:+91" + finalNumber);
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", "" + editTextMessage.getText().toString());
                getContext().startActivity(sms_intent);
            }
        });
    }

    private void showAdvancedSearchDialogue() {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = getLayoutInflater().inflate(R.layout.advanced_search_dialogue, null);
        dialogueBuilder.setView(dialogueView);
        final TextInputEditText editTextName, editTextLocation, editTextDepartment, editTextDesignation, editTextDivision;
        Button searchButton, clearButton;
        editTextName = dialogueView.findViewById(R.id.editTextName);
        editTextLocation = dialogueView.findViewById(R.id.editTextLocation);
        editTextDepartment = dialogueView.findViewById(R.id.editTextDepartment);
        editTextDesignation = dialogueView.findViewById(R.id.editTextDesignation);
        editTextDivision = dialogueView.findViewById(R.id.editTextDivision);
        searchButton = dialogueView.findViewById(R.id.searchButton);
        clearButton = dialogueView.findViewById(R.id.clearButton);


        dialogueBuilder.setTitle("Advance Search");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();
        //Retrieve the 4 variable value
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String name = sp.getString(KEY_NAME, null);
        String location = sp.getString(KEY_LOCATION, null);
        String department = sp.getString(KEY_DEPARTMENT, null);
        String designation = sp.getString(KEY_DESIGNATION, null);
        String division = sp.getString(KEY_DIVISION, null);
        if (name != null || location != null || department != null || designation != null || division != null) {
            editTextName.setText(name);
            editTextLocation.setText(location);
            editTextDesignation.setText(designation);
            editTextDepartment.setText(department);
            editTextDivision.setText(division);
        }

        sortedArrayList = new ArrayList<>();
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName.setText("");
                editTextLocation.setText("");
                editTextDepartment.setText("");
                editTextDesignation.setText("");
                editTextDivision.setText("");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().toLowerCase();
                String location = editTextLocation.getText().toString().toLowerCase();
                String department = editTextDepartment.getText().toString().toLowerCase();
                String designation = editTextDesignation.getText().toString().toLowerCase();
                String division = editTextDivision.getText().toString().toLowerCase();

                //store the 4 variable value
                SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(KEY_NAME, name);
                editor.putString(KEY_LOCATION, location);
                editor.putString(KEY_DEPARTMENT, department);
                editor.putString(KEY_DESIGNATION, designation);
                editor.putString(KEY_DIVISION, division);
                editor.apply();

                if ((TextUtils.isEmpty(name)) && (TextUtils.isEmpty(location)) && (TextUtils.isEmpty(department)) && (TextUtils.isEmpty(division))) {
                    Toast.makeText(getActivity(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    sortedArrayList.addAll(userList);
                    Log.i(TAG, "onClick: " + sortedArrayList.size());
                    for (int i = 0; i < sortedArrayList.size(); i++) {
                        if (!TextUtils.isEmpty(name)) {
                            if (!(sortedArrayList.get(i).getName() + "").toLowerCase().contains(name)) {
                                sortedArrayList.remove(i);
                                i--;
                                continue;
                            }
                        }
                        if (!TextUtils.isEmpty(location)) {
                            if (!(sortedArrayList.get(i).getLocation() + "").toLowerCase().equals(location)) {
                                sortedArrayList.remove(i);
                                i--;
                                continue;
                            }
                        }
                        if (!TextUtils.isEmpty(division)) {
                            if (!(sortedArrayList.get(i).getDivision() + "").toLowerCase().equals(division)) {
                                sortedArrayList.remove(i);
                                i--;
                                continue;
                            }
                        }
                        if (!TextUtils.isEmpty(department)) {
                            if (!(sortedArrayList.get(i).getDepartment() + "").toLowerCase().equals(department)) {
                                sortedArrayList.remove(i);
                                i--;
                                continue;
                            }
                        }
                        if (!TextUtils.isEmpty(designation)) {
                            if (!(sortedArrayList.get(i).getDesignation() + "").toLowerCase().equals(designation)) {
                                sortedArrayList.remove(i);
                                i--;
                            }
                        }
                    }

                }
                if (sortedArrayList.isEmpty()) {
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();
                    recyclerAdapter = new UserAdapter(getActivity(), sortedArrayList);
                    listView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                    clearSearchFAB.show();
                    alertDialog.show();
                } else {
                    recyclerAdapter = new UserAdapter(getActivity(), sortedArrayList);
                    listView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                    clearSearchFAB.show();
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void findViews(View v) {
        Log.e(TAG, "onView: ");
        listView = v.findViewById(R.id.listView);

        linearLayoutBroadcast = v.findViewById(R.id.broadcastLayout);
        textViewCancelBroadcast = v.findViewById(R.id.cancelBroadCast);
        textViewSendBroadcast = v.findViewById(R.id.sendBroadCast);


        textViewSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast();
            }
        });
        textViewCancelBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBroadcast();
            }
        });


        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);
        clearSearchFAB = v.findViewById(R.id.fab);
        sortedArrayList = new ArrayList<>();
        multiSelectList = new ArrayList<>();
        clearSearchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerAdapter = new UserAdapter(getActivity(), userList);
                listView.setAdapter(recyclerAdapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                recyclerAdapter.notifyDataSetChanged();
                clearSearchFAB.hide();
                sortedArrayList.clear();
            }
        });

        backupArrayList = new ArrayList<>();
        userList = new ArrayList<>();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("userinfo");
        databaseReferenceUser.keepSynced(true);

        Toolbar actionBarToolBar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(actionBarToolBar);
        actionBarToolBar.setTitle("Search Contacts");
        actionBarToolBar.inflateMenu(R.menu.search_menu);
    }

    private void cancelBroadcast() {
        recyclerAdapter.displayAllCheckbox(false);
        linearLayoutBroadcast.setVisibility(View.GONE);
    }

    private void sendBroadcast() {
        ArrayList<String> numberList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).selected)
                numberList.add(userList.get(i).getPhoneno_1());
        }

        showBroadcastLayout(numberList);
        recyclerAdapter.displayAllCheckbox(false);
        linearLayoutBroadcast.setVisibility(View.GONE);
    }

    /**
     * A check is performed to see if Employee data is previously fetched or not
     * If fetched already, the operstion will not be repeated
     * This will allow activity state retention on returning back to this activity
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onResume: ");
        if (!valueEventListenerCalled) {
            databaseReferenceUser.addValueEventListener(valueEventListener);
            valueEventListenerCalled = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            recyclerAdapter = new UserAdapter(getActivity(), sortedArrayList);
            listView.setAdapter(recyclerAdapter);

        }
    }

    /**
     * Always executed after onStart()
     * Executed at Activity launch and after returning to this activity
     * Executed By Pressing back button
     */
    @Override
    public void onResume() {
        Log.e(TAG, "onResume: ");
        super.onResume();
    }


}
