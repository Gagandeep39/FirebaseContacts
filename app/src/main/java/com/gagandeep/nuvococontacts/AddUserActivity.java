package com.gagandeep.nuvococontacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    public static final String TAG = "AddUser Activity";
    DatabaseReference databaseReferenceUser;
    List<User> userList;
    ImageView profileImageView, chooseImageView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Uri filePath;
    FrameLayout frameLayout;
    TextInputEditText editTextFirstName, editTextLastName, editTextDesignation, editTextLocation, editTextDepartment, editTextEmail, editTextEmail2, editTextPhoneNo1, editTextPhoneNo2, editTextPhoneNo3, editTextEmployeeId, editTextDeskNumber, editTextEmergencyNumber;
    String cacheUri;
    AppCompatSpinner adminRightsSpinner;
    String adminRights = "0";  //0 for no , 1 for yes
    String id, firstName, lastName, location, department, designation, deskNo, phoneno1, phoneno2, phoneno3, emergencyNumber, email, email2, employeeId, profileUri, profileCacheUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userList = new ArrayList<>();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("userinfo");
        databaseReferenceUser.keepSynced(true);

        findViews();
        fillSpinner();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToServer();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fillSpinner() {
        adminRightsSpinner = findViewById(R.id.adminRightsSpinner);
        final String[] decision = {"Yes", "No"};
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(AddUserActivity.this, android.R.layout.simple_spinner_item, decision);

        // setting adapteers to spinners
        adminRightsSpinner.setAdapter(spin_adapter);
        adminRightsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adminRights = decision[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void findViews() {

        editTextFirstName = findViewById(R.id.editTextName);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        editTextDesignation = findViewById(R.id.editTextDesignation);
        editTextEmail = findViewById(R.id.editTextEmailId);
        editTextEmail2 = findViewById(R.id.editTextEmailId2);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextPhoneNo1 = findViewById(R.id.editTextPhoneNo1);
        editTextPhoneNo2 = findViewById(R.id.editTextPhoneNo2);
        editTextPhoneNo3 = findViewById(R.id.editTextPhoneNo3);
        profileImageView = findViewById(R.id.profileImageView);
        chooseImageView = findViewById(R.id.chooseImageView);
        editTextEmployeeId = findViewById(R.id.editTextEmployeeId);
        editTextEmergencyNumber = findViewById(R.id.editTextEmergencyNumber);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextDeskNumber = findViewById(R.id.editTextDeskNumber);
        frameLayout = findViewById(R.id.frameLayout);

        chooseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


    }

    private void saveDataToServer() {
        firstName = editTextFirstName.getText().toString();
        department = editTextDepartment.getText().toString();
        designation = editTextDepartment.getText().toString();
        email = editTextEmail.getText().toString();
        email2 = editTextEmail2.getText().toString();
        location = editTextLocation.getText().toString();
        phoneno1 = editTextPhoneNo1.getText().toString();
        phoneno2 = editTextPhoneNo2.getText().toString();
        phoneno3 = editTextPhoneNo3.getText().toString();
        deskNo = editTextDeskNumber.getText().toString();
        employeeId = editTextEmployeeId.getText().toString();
        emergencyNumber = editTextEmergencyNumber.getText().toString();
        lastName = editTextLastName.getText().toString();


        if (TextUtils.isEmpty(employeeId))
            editTextEmployeeId.setError("Enter Employee ID");
        else if (TextUtils.isEmpty(firstName))
            editTextFirstName.setError("Enter Name");
        else if (TextUtils.isEmpty(lastName))
            editTextLastName.setError("Enter LastName");
        else if (TextUtils.isEmpty(department))
            editTextDepartment.setError("Enter Department");
        else if (TextUtils.isEmpty(designation))
            editTextDesignation.setError("Enter Designation");
        else if (TextUtils.isEmpty(email))
            editTextEmail.setError("Enter Email");
        else if (TextUtils.isEmpty(location))
            editTextLocation.setError("Enter Location");
        else if (TextUtils.isEmpty(phoneno1))
            editTextPhoneNo1.setError("Enter Phone No");
        else if (TextUtils.isEmpty(emergencyNumber))
            editTextEmergencyNumber.setError("Enter Emergency no");
        else if (TextUtils.isEmpty(deskNo))
            editTextDeskNumber.setError("Enter Desk No");
        else {
            id = databaseReferenceUser.push().getKey();
//             (String userId, String firstName, String lastName, String designation, String location, String email1, String email2, String phoneno_1
//                    , String phoneno_2, String phoneno_3, String emergencyNumber, String department, String profileUri, String profileCacheUri, String employeeId, String adminRights, String deskNumber)

            if (filePath != null) {

                profileCacheUri = uploadCachedImage(id);
                profileUri = uploadImage(id);
            }
            User user = new User(id, firstName, lastName, designation, location, email, email2, phoneno1, phoneno2, phoneno3, emergencyNumber, department, profileUri, profileCacheUri, employeeId, adminRights, deskNo);
            databaseReferenceUser.child(id).setValue(user);
            Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
            finish();

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap = getResizedBitmap(bitmap, 800, 600);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                profileImageView.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                profileImageView.setImageBitmap(bitmap);
                Toast.makeText(this, "Displayed Image", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String uploadCachedImage(String id) {
        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();

        final StorageReference nameReference = storageRef.child(id + "cache.jpg");
        Bitmap bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        bitmap = getResizedBitmap(bitmap, 800, 600);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = nameReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: " + exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                nameReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileCacheUri = uri.toString();
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.e(TAG, "onComplete: " + task.getResult().getMetadata().getPath());

            }
        });

        return profileCacheUri;
    }

    String uploadImage(String id) {
        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();

        final StorageReference nameReference = storageRef.child(id + ".jpg");
        Bitmap bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap = getResizedBitmap(bitmap, 800, 600);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = nameReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: " + exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                nameReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileUri = uri.toString();
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.e(TAG, "onComplete: " + task.getResult().getMetadata().getPath());

            }
        });

        return profileUri;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 234);
    }


}
