package com.gagandeep.nuvococontacts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecentFragment extends Fragment {

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
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        return v;
    }


}