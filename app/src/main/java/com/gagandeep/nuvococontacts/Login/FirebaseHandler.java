package com.gagandeep.nuvococontacts.Login;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

//Create an Instance of firebase(seprate class since it must be executed only once)
//for offline functionality
public class FirebaseHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}