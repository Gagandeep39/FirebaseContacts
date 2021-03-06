package com.gagandeep.nuvococontacts.Login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gagandeep.nuvococontacts.R;
import com.gagandeep.nuvococontacts.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_PHONENO_1;
import static com.gagandeep.nuvococontacts.Helpers.Constants.FIREBASE_USERINFO;
import static com.gagandeep.nuvococontacts.Helpers.Constants.PHONE_NUMBER_LENGTH;
import static com.gagandeep.nuvococontacts.Helpers.HelperClass.setMaxLength;
import static com.gagandeep.nuvococontacts.Helpers.HelperClass.validatePhoneNumber;
import static com.gagandeep.nuvococontacts.Login.SplashScreenActivity.temporaryUser;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText editTextPhone;
    Button button;
    String number;
    ProgressBar progressBar;

    //firebase auth object
    private FirebaseAuth mAuth;
    ArrayList<User> userArrayList;

    ValueEventListener view = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                    temporaryUser = issue.getValue(User.class);
                    break;

                }
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", number);
                startActivity(intent);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "User Not Registered", Toast.LENGTH_SHORT).show();

            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        userArrayList = new ArrayList<>();
        editTextPhone = findViewById(R.id.textInputPhone);
        progressBar = findViewById(R.id.progressBar);

        setMaxLength(editTextPhone, PHONE_NUMBER_LENGTH);

        button = findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = editTextPhone.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    if (validatePhoneNumber(number, editTextPhone)) {

                        if (checkInternetConnection()) {
                            searchFirebase(number);
                        } else {
                            Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    editTextPhone.setError("Phone number required.");
                }
            }
        });

    }


    //Perform search for phone_1 in database
    private void searchFirebase(final String number) {

        Query query1 = FirebaseDatabase.getInstance().getReference(FIREBASE_USERINFO)
                .orderByChild(COLUMN_PHONENO_1)
                .equalTo(number);

        query1.addListenerForSingleValueEvent(view);
    }


    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("", "Internet Connection Not Present");
            return false;
        }
    }
}
