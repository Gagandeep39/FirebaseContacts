package com.gagandeep.nuvococontacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.gagandeep.nuvococontacts.Constants.FIREBASE_USERINFO;
import static com.gagandeep.nuvococontacts.Constants.PHONE_NUMBER_LENGTH;
import static com.gagandeep.nuvococontacts.HelperClass.setMaxLength;

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
    TextInputEditText editTextFirstName, editTextLastName, editTextDesignation, editTextLocation, editTextDepartment, editTextEmail, editTextEmail2, editTextPhoneNo1, editTextPhoneNo2, editTextPhoneNo3, editTextEmployeeId, editTextDeskNumber, editTextEmergencyNumber, editTextSapId;
    AppCompatSpinner adminRightsSpinner;
    String id, firstName, lastName, location, department, designation, deskNo, phoneno1, phoneno2, phoneno3, emergencyNumber, email, email2, employeeId, profileUri, profileCacheUri, sapId;
    ProgressBar uploadProgressBar;
    String adminRights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userList = new ArrayList<>();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference(FIREBASE_USERINFO);
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

    //Drop down menu to select Yes or No for Admin rights
    private void fillSpinner() {
        adminRightsSpinner = findViewById(R.id.adminRightsSpinner);
        final String[] decision = {"true", "false"};
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(AddUserActivity.this, android.R.layout.simple_spinner_item, decision);

        // setting adapteers to spinners
        adminRightsSpinner.setAdapter(spin_adapter);
        adminRightsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    adminRights = "true";
                } else
                    adminRights = "false";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Link widgets with variables
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
        editTextDeskNumber = findViewById(R.id.editTextDivision);
        frameLayout = findViewById(R.id.frameLayout);
        editTextSapId = findViewById(R.id.editTextSapId);
        uploadProgressBar = findViewById(R.id.uploadProgressBar);

        setMaxLength(editTextPhoneNo3, PHONE_NUMBER_LENGTH);
        setMaxLength(editTextPhoneNo2, PHONE_NUMBER_LENGTH);
        setMaxLength(editTextPhoneNo1, PHONE_NUMBER_LENGTH);
        setMaxLength(editTextEmergencyNumber, PHONE_NUMBER_LENGTH);

        chooseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        id = databaseReferenceUser.push().getKey();
    }

    //fetch data from textbox and performvalidation and save to server
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
        sapId = editTextSapId.getText().toString();


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
        else if (TextUtils.isEmpty(sapId))
            editTextSapId.setError("Enter Sap ID");
        else {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    User user = new User(id, firstName, designation, location, email, email2, phoneno1, phoneno2, emergencyNumber, department, profileUri, profileCacheUri, employeeId, adminRights, deskNo, sapId);
                    databaseReferenceUser.child(id).setValue(user);
                    if (TextUtils.isEmpty(profileUri))
                        Toast.makeText(AddUserActivity.this, "Profile Picture could not be Uploaded\nOther data inserted successfully", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(AddUserActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, 2000);   //2000ms->2s


        }

    }

    //Operations performed once we select an Image from gallery
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
                profileCacheUri = uploadCachedImage(id);
                profileUri = uploadImage(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Save a cache image to server (smaller version of actual image)
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

    //Save an image to server (actual one)
    String uploadImage(String id) {
        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();
        uploadProgressBar.setVisibility(View.VISIBLE);

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
                uploadProgressBar.setVisibility(View.GONE);

            }
        });

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
                int currentprogress = (int) progress;
                uploadProgressBar.setProgress(currentprogress);
            }
        });


        return profileUri;
    }

    //Resize image to specific size
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

    //Intent to open gallery app
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 234);
    }


}
