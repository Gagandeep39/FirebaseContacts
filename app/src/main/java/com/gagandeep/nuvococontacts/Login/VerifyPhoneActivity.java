package com.gagandeep.nuvococontacts.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gagandeep.nuvococontacts.Helpers.ObjectSerializer;
import com.gagandeep.nuvococontacts.MainActivity;
import com.gagandeep.nuvococontacts.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.gagandeep.nuvococontacts.Helpers.Constants.CURRENT_USER;
import static com.gagandeep.nuvococontacts.Helpers.Constants.PACKAGE_NAME;
import static com.gagandeep.nuvococontacts.Helpers.HelperClass.setMaxLength;
import static com.gagandeep.nuvococontacts.Login.SplashScreenActivity.currentUser;
import static com.gagandeep.nuvococontacts.Login.SplashScreenActivity.isAdmin;
import static com.gagandeep.nuvococontacts.Login.SplashScreenActivity.temporaryUser;

public class VerifyPhoneActivity extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;
    private Button buttonSignIn;

    ProgressBar progressBar;

    //firebase auth object
    private FirebaseAuth mAuth;
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(VerifyPhoneActivity.this, LoginActivity.class));
            finish();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(VerifyPhoneActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
            //storing the verification id that is s ent to the user
            mVerificationId = s;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextPhone);
        progressBar = findViewById(R.id.progressBar);

        setMaxLength(editTextCode, 6);


        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        if (checkInternetConnection()) {
            progressBar.setVisibility(View.VISIBLE);
            sendVerificationCode(mobile);
        } else {
            Toast.makeText(VerifyPhoneActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(VerifyPhoneActivity.this, LoginActivity.class));
            finish();
        }
        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

//
                verifyVerificationCode(code);
//
            }
        });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
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

    private void verifyVerificationCode(String code) {
        //creating the credential
        try {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast.makeText(this, "Please Try Again Later" + e, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(VerifyPhoneActivity.this, LoginActivity.class));
            finish();
        }

        //signing the user
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = temporaryUser;
                            saveUserInfo();
                            Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            String message = "Check Your Connection";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toast.makeText(VerifyPhoneActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }

    /**
     * 0 SapID
     * 1 name
     * 2 department
     * 3 location
     * 4 dsignation
     * 5 division
     * 6 employeeid
     * 7 email1
     * 8 email2
     * 9 phone1
     * 10 phone2
     * 11 adminright
     * 12 desknumber
     * 13 emergencynumber
     * 14 profilecacheuri
     * 15 profile uri
     */
    void saveUserInfo() {
        ArrayList<String> set = new ArrayList<>();
        set.add(currentUser.getSapId());
        set.add(currentUser.getName());
        set.add(currentUser.getDepartment());
        set.add(currentUser.getLocation());
        set.add(currentUser.getDesignation());
        set.add(currentUser.getDivision());
        set.add(currentUser.getEmployeeId());
        set.add(currentUser.getEmail1());
        set.add(currentUser.getEmail2());
        set.add(currentUser.getPhoneno_1());
        set.add(currentUser.getPhoneno_2());
        set.add(currentUser.getAdminRights());
        set.add(currentUser.getDeskNumber());
        set.add(currentUser.getEmergencyNumber());
        set.add(currentUser.getProfileCacheUri());
        set.add(currentUser.getProfileUri());
        if (!TextUtils.isEmpty(currentUser.getAdminRights())) {
            if (currentUser.getAdminRights().equals("true"))
                isAdmin = true;
        }
        SharedPreferences sharedPreferences = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);

        sharedPreferences.edit().putString(CURRENT_USER, ObjectSerializer.serialize(set)).apply();
        sharedPreferences.edit().apply();
    }
    }


