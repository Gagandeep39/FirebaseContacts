package com.gagandeep.nuvococontacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

import static com.gagandeep.nuvococontacts.Constants.PACKAGE_NAME;

public class SplashScreenActivity extends AppCompatActivity {

    public static User currentUser, temporaryUser;
    public static boolean isAdmin = false;
    TextView textViewVersion;

    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        textViewVersion = findViewById(R.id.textViewVersion);
        textViewVersion.setText("Version " + versionName + "\n" + getString(R.string.created_by_nuvoco));

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                authenticationCheck();

                // close this activity
                finish();
            }
        }, 2000);
    }


    //check if user is previously autenticated and logged in from locally stored data
    private User authenticationCheck() {
        ArrayList<String> newArralist = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        User user = null;
        newArralist = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("currentuser", ObjectSerializer.serialize(new ArrayList<String>())));
        if (newArralist.size() != 0)
            user = new User(newArralist.get(0)
                    , newArralist.get(1)
                    , newArralist.get(2)
                    , newArralist.get(3)
                    , newArralist.get(4)
                    , newArralist.get(5)
                    , newArralist.get(6)
                    , newArralist.get(7)
                    , newArralist.get(8)
                    , newArralist.get(9)
                    , newArralist.get(10)
                    , newArralist.get(11)
                    , newArralist.get(12)
                    , newArralist.get(13)
                    , newArralist.get(14)
                    , newArralist.get(15)
                    , newArralist.get(16)
                    , newArralist.get(17));


        String name = "";
        if (user != null)
            name = user.getFirstName();


        if (!TextUtils.isEmpty(name)) {
            currentUser = user;
            if (!TextUtils.isEmpty(currentUser.getAdminRights())) {
                if (currentUser.getAdminRights().equals("true"))
                    isAdmin = true;
            }
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        } else
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        finish();

        return user;
    }


}
