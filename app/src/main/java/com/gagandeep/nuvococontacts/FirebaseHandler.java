package com.gagandeep.nuvococontacts;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

//Create an Instance of firebase(seprate class since it must be executed only once)
public class FirebaseHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}