package com.gagandeep.nuvococontacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.gagandeep.nuvococontacts.Constants.COLUMN_PHONENO_1;
import static com.gagandeep.nuvococontacts.Constants.FIREBASE_USERINFO;
import static com.gagandeep.nuvococontacts.Constants.PACKAGE_NAME;

public class LoginActivity extends AppCompatActivity {
    public static User currentUser, temporaryUser;
    EditText editTextPhone;
    Button button;
    String number;
    TextView versionTextView;
    public static boolean isAdmin = false;
    ProgressBar progressBar;

    //firebase auth object
    private FirebaseAuth mAuth;
    ArrayList<User> userArrayList;
    int counter = 0;
    ValueEventListener view = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                    counter++;
                    temporaryUser = issue.getValue(User.class);
                    break;

                }
                if (counter == 0)
                    Toast.makeText(LoginActivity.this, "User Not Registered", Toast.LENGTH_SHORT).show();
                else {
                    counter = 0;
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                    intent.putExtra("mobile", currentUser.getPhoneno_1());
                    startActivity(intent);
                }
            } else {
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        userArrayList = new ArrayList<>();



        authenticationCheck();


        editTextPhone = findViewById(R.id.editTextPhone);
        versionTextView = findViewById(R.id.versionTextView);
        progressBar = findViewById(R.id.progressBar);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = editTextPhone.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    progressBar.setVisibility(View.VISIBLE);
                    searchFirebase(number);
                } else {
                    editTextPhone.setError("Phone number required.");
                }
            }
        });

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionTextView.setText("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    //Perform search for phone_1 in database
    private void searchFirebase(final String number) {

        Query query1 = FirebaseDatabase.getInstance().getReference(FIREBASE_USERINFO)
                .orderByChild(COLUMN_PHONENO_1)
                .equalTo(number);

        query1.addListenerForSingleValueEvent(view);
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
            Toast.makeText(this, "Entered", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        return user;
    }


}
