package com.gagandeep.nuvococontacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    public static User currentUser;
    EditText editTextPhone;
    Button button;
    String number;
    TextView versionTextView;
    public static boolean isAdmin = true;

    public static String applicationUser;
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
                    currentUser = issue.getValue(User.class);
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
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber;
        userArrayList = new ArrayList<>();



        authenticationCheck();


        editTextPhone = findViewById(R.id.editTextPhone);
        versionTextView = findViewById(R.id.versionTextView);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = editTextPhone.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    searchFirebase(number);
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

    private void searchFirebase(final String number) {

        Query query1 = FirebaseDatabase.getInstance().getReference("userinfo")
                .orderByChild("phoneno_1")
                .equalTo(number);
        Query query2 = FirebaseDatabase.getInstance().getReference("userinfo")
                .orderByChild("phoneno_2")
                .equalTo(number);
        Query query3 = FirebaseDatabase.getInstance().getReference("userinfo")
                .orderByChild("phoneno_3")
                .equalTo(number);


        query1.addListenerForSingleValueEvent(view);
        query2.addListenerForSingleValueEvent(view);
        query3.addListenerForSingleValueEvent(view);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (counter == 0)
                    Toast.makeText(LoginActivity.this, "User Not Registered", Toast.LENGTH_SHORT).show();
                else {
                    counter = 0;
                    Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                    intent.putExtra("mobile", currentUser.getPhoneno_1());
                    startActivity(intent);
                }
            }
        }, 2000);   //2000ms->2s

    }


//    private void authenticationCheck() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String name = preferences.getString("userid", "");
//        applicationUser = name;
//
//        Toast.makeText(this, "" + name, Toast.LENGTH_SHORT).show();
//
//        if (!TextUtils.isEmpty(name) && !name.equals("XXXX")) {
//            FirebaseDatabase.getInstance().getReference("userinfo")
//                    .orderByChild("userId")
//                    .equalTo(number);
//
//            Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }
//    }

    private User authenticationCheck() {
        ArrayList<String> newArralist = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("com.gagandeep.nuvococontacts", Context.MODE_PRIVATE);
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
        Toast.makeText(this, "" + name, Toast.LENGTH_SHORT).show();

        if (!TextUtils.isEmpty(name) && !name.equals("XXXX")) {
            Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
            currentUser = user;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        return user;
    }


}
