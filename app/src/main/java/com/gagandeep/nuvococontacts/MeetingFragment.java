package com.gagandeep.nuvococontacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MeetingFragment extends Fragment {
    DatabaseReference databaseReferenceUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        // FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReferenceUser = database.getReference();
        findViews(v);
        return v;
    }

    private void findViews(View v) {
        Toolbar actionBarToolBar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(actionBarToolBar);
        actionBarToolBar.setTitle("Conferences & Meetings");
        //actionBarToolBar.inflateMenu(R.menu.search_menu);
    }
}
