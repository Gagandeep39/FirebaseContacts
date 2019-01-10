package com.gagandeep.nuvococontacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends AppCompatActivity {
    String name, designation, department, email_1, email_2, phone_1, phone_2, phone_3, location;
    LinearLayout phoneLayout, messageLayout, whatsappLayout, emailLayout, phoneno_1Layout, phoneno_2Layout, phoneno_3Layout, email_1Layout, email_2Layout;
    TextView phoneno_1TextView, phoneno_2TextView, phoneno_3TextView, email_1TextView, email_2TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        String value;
        if (extras != null) {
            name = extras.getString("name");
            designation = extras.getString("designation");
            location = extras.getString("location");
            department = extras.getString("department");
            phone_1 = extras.getString("phoneno_1");
            email_1 = extras.getString("email_1");
            Toast.makeText(this, name + "" + phone_1, Toast.LENGTH_SHORT).show();
            setTitle(name);
        }
        findViews();
        onClickListeners();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    void findViews() {
        phoneLayout = findViewById(R.id.phoneLayout);
        messageLayout = findViewById(R.id.messageLayout);
        whatsappLayout = findViewById(R.id.whatsappLayout);
        emailLayout = findViewById(R.id.emailLayout);
        phoneno_1Layout = findViewById(R.id.phoneno_1Layout);
        phoneno_2Layout = findViewById(R.id.phoneno_2Layout);
        phoneno_3Layout = findViewById(R.id.phoneno_3Layout);
        email_1Layout = findViewById(R.id.email_1Layout);
        email_2Layout = findViewById(R.id.email_2Layout);
        phoneno_1TextView = findViewById(R.id.phoneno_1TextView);
        phoneno_2TextView = findViewById(R.id.phoneno_2TextView);
        phoneno_3TextView = findViewById(R.id.phoneno_3TextView);
        email_1TextView = findViewById(R.id.email_1TextView);
        email_2TextView = findViewById(R.id.email_2TextView);

        phoneno_1TextView.setText(phone_1);
        email_1TextView.setText(email_1);

        if (TextUtils.isEmpty(phone_2))
            phoneno_2Layout.setVisibility(View.GONE);
        else
            phoneno_2TextView.setText(phone_2);

        if (TextUtils.isEmpty(phone_3))
            phoneno_3Layout.setVisibility(View.GONE);
        else
            phoneno_2TextView.setText(phone_3);

        if (TextUtils.isEmpty(email_2))
            email_2Layout.setVisibility(View.GONE);
        else
            email_2TextView.setText(email_2);
    }

    void onClickListeners() {
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91" + phone_1));
                startActivity(intent);
            }
        });

        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri sms_uri = Uri.parse("smsto:+91" + phone_1);
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", "Good Morning ! how r U ?");
                startActivity(sms_intent);
            }
        });

        whatsappLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email_1, null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }

    void callFunction(View v) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        switch (v.getId()) {
            case R.id.phoneno_1Layout:
                intent.setData(Uri.parse("tel:+91" + phone_1));
                break;
            case R.id.phoneno_2Layout:
                intent.setData(Uri.parse("tel:+91" + phone_2));
                Toast.makeText(this, "" + phone_2, Toast.LENGTH_SHORT).show();
                break;
            case R.id.phoneno_3Layout:
//                intent.setData(Uri.parse("tel:+91" + phone_3));
                break;

        }

        startActivity(intent);
    }

    void messageFunction(View v) {
        Uri sms_uri = null;
        switch (v.getId()) {
            case R.id.message_1ImageView:
                sms_uri = Uri.parse("smsto:+91" + phone_1);
                break;
            case R.id.message_2ImageView:
                sms_uri = Uri.parse("smsto:+91" + phone_2);
                break;
            case R.id.message_3ImageView:
                sms_uri = Uri.parse("smsto:+91" + phone_3);
                break;

        }
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
//        sms_intent.putExtra("sms_body", "Good Morning ! how r U ?");
        startActivity(sms_intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        moveTaskToBack(true);
    }

    /**
     * react to the user tapping the back/up icon in the action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
