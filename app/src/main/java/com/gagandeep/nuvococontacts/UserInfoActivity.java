package com.gagandeep.nuvococontacts;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity {
    String name, designation, department, email_1, email_2, phone_1, phone_2, phone_3, location, profileUri;
    LinearLayout phoneLayout, messageLayout, whatsappLayout, emailLayout, phoneno_1Layout, phoneno_2Layout, phoneno_3Layout, email_1Layout, email_2Layout, whatsapp_1Layout, whatsapp_2Layout, whatsapp_3Layout;
    TextView phoneno_1TextView, phoneno_2TextView, phoneno_3TextView, email_1TextView, email_2TextView, whatsapp_1TextView, whatsapp_2TextView, whatsapp_3TextView;
    ImageView profileImageView;

    //Database vriable
    SQLiteDatabase db;
    FavouriteDbHelper mDbHelper;
    List<FavouriteItem> itemIds;
    String[] projection = {
            BaseColumns._ID,
            FavouriteContract.Favourite.COLUMN_NAME_TITLE,
            FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE
    };

    // Filter results WHERE "title" = 'My Title'
    String selection = FavouriteContract.Favourite.COLUMN_NAME_TITLE + " = ?";


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
            phone_2 = extras.getString("phoneno_2");
            phone_3 = extras.getString("phoneno_3");
            email_1 = extras.getString("email_1");
            email_2 = extras.getString("email_2");
            profileUri = extras.getString("profileuri");
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
        whatsapp_1Layout = findViewById(R.id.whatsapp_1Layout);
        whatsapp_2Layout = findViewById(R.id.whatsapp_2Layout);
        whatsapp_3Layout = findViewById(R.id.whatsapp_3Layout);
        phoneno_1TextView = findViewById(R.id.phoneno_1TextView);
        phoneno_2TextView = findViewById(R.id.phoneno_2TextView);
        phoneno_3TextView = findViewById(R.id.phoneno_3TextView);
        email_1TextView = findViewById(R.id.email_1TextView);
        email_2TextView = findViewById(R.id.email_2TextView);
        whatsapp_1TextView = findViewById(R.id.whatsapp_1TextView);
        whatsapp_2TextView = findViewById(R.id.whatsapp_2TextView);
        whatsapp_3TextView = findViewById(R.id.whatsapp_3TextView);



        profileImageView = findViewById(R.id.profileImageView);

        mDbHelper = new FavouriteDbHelper(this);
        itemIds = new ArrayList<>();

        phoneno_1TextView.setText(phone_1);
        email_1TextView.setText(email_1);
        whatsapp_1TextView.setText("WhatsApp to +91" + phone_1);

        if (TextUtils.isEmpty(phone_2)) {
            phoneno_2Layout.setVisibility(View.GONE);
            whatsapp_2Layout.setVisibility(View.GONE);
        } else {
            phoneno_2TextView.setText(phone_2);
            whatsapp_2TextView.setText("WhatsApp to +91" + phone_2);
        }

        if (TextUtils.isEmpty(phone_3)) {
            phoneno_3Layout.setVisibility(View.GONE);
            whatsapp_3Layout.setVisibility(View.GONE);
        } else {
            phoneno_3TextView.setText(phone_3);
            whatsapp_3TextView.setText("WhatsApp to +91" + phone_3);
        }

        if (TextUtils.isEmpty(email_2))
            email_2Layout.setVisibility(View.GONE);
        else
            email_2TextView.setText(email_2);

        if (!TextUtils.isEmpty(profileUri)) {
            Bitmap b = stringToBitMap(profileUri);
            profileImageView.setImageBitmap(b);

        }
    }

    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
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
                whatsappFunction(v);
            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailFunction(v);
            }
        });
    }

    void emailFunction(View view) {

        String email = "";
        switch (view.getId()) {
            case R.id.email_1Layout:
                email = email_1;
                break;
            case R.id.email_2Layout:
                email = email_2;
                break;
            case R.id.emailLayout:
                email = email_1;
                break;
        }


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    void callFunction(View view) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        switch (view.getId()) {
            case R.id.phoneno_1Layout:
                intent.setData(Uri.parse("tel:+91" + phone_1));
                break;
            case R.id.phoneno_2Layout:
                intent.setData(Uri.parse("tel:+91" + phone_2));
                break;
            case R.id.phoneno_3Layout:
//                intent.setData(Uri.parse("tel:+91" + phone_3));
                break;

        }

        startActivity(intent);
    }

    void messageFunction(View view) {
        Uri sms_uri = null;
        switch (view.getId()) {
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


        db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FavouriteContract.Favourite.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        itemIds.clear();

        while (cursor.moveToNext()) {
            int itemId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FavouriteContract.Favourite._ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_TITLE));
            String itemPhone = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE));
            itemIds.add(new FavouriteItem(itemId, itemName, itemPhone));
        }
        cursor.close();
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favourite:
                int counter = 0;
                if (itemIds.isEmpty())
                    addToFavourites(item);
                for (int i = 0; i < itemIds.size(); i++) {
                    if (itemIds.get(i).getName().contains(name))
                        counter = 1;
                }

                if (counter == 1) {
                    deleteFavourites(item);
                    counter = 0;
                } else
                    addToFavourites(item);

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteFavourites(MenuItem item) {
// Define 'where' part of query.
        String selection = FavouriteContract.Favourite.COLUMN_NAME_TITLE + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = {name};
// Issue SQL statement.
        int deletedRows = db.delete(FavouriteContract.Favourite.TABLE_NAME, selection, selectionArgs);
        Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
        item.setIcon(R.drawable.ic_star);
    }

    private void addToFavourites(MenuItem item) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FavouriteContract.Favourite.COLUMN_NAME_TITLE, name);
        values.put(FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE, phone_1);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FavouriteContract.Favourite.TABLE_NAME, null, values);
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_filled));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_info, menu);    // Define a projection that specifies which columns from the database
// you will actually use after this query.

        String[] selectionArgs = {name};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE + " DESC";

        db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FavouriteContract.Favourite.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );


        while (cursor.moveToNext()) {
            int itemId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FavouriteContract.Favourite._ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_TITLE));
            String itemPhone = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE));
            itemIds.add(new FavouriteItem(itemId, itemName, itemPhone));
        }
        cursor.close();
        for (int i = 0; i < itemIds.size(); i++) {
            if (itemIds.get(i).getName().contains(name)) {
                menu.getItem(0).setIcon(R.drawable.ic_star_filled);
                break;
            }
        }
        return super.onCreateOptionsMenu(menu);
    }


    public void whatsappFunction(View view) {

        String smsNumber = ""; //without '+'

        switch (view.getId()) {
            case R.id.whatsapp_1Layout:
                smsNumber = phone_1;
                break;
            case R.id.whatsapp_2Layout:
                smsNumber = phone_2;
                break;
            case R.id.whatsapp_3Layout:
                smsNumber = phone_3;
                break;
            case R.id.whatsappLayout:
                smsNumber = phone_1;
                break;
        }

        String formattedNumber = "91" + smsNumber;
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "");
            sendIntent.putExtra("jid", formattedNumber + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(UserInfoActivity.this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }


    }
}
