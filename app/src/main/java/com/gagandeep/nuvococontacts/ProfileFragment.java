package com.gagandeep.nuvococontacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.gagandeep.nuvococontacts.AddUserActivity.TAG;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_DEPARTMENT;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_DESIGNATION;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_DESK_NUMBER;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_EMAIL_1;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_EMAIL_2;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_EMERGENCY_NUMBER;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_EMPLOYEE_ID;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_FIRST_NAME;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_LAST_NAME;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_LOCATION;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_PHONENO_1;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_PHONENO_2;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_PHONENO_3;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_SAP_ID;
import static com.gagandeep.nuvococontacts.Constants.CURRENT_USER;
import static com.gagandeep.nuvococontacts.Constants.FIREBASE_USERINFO;
import static com.gagandeep.nuvococontacts.Constants.PACKAGE_NAME;
import static com.gagandeep.nuvococontacts.Constants.PHONE_NUMBER_LENGTH;
import static com.gagandeep.nuvococontacts.HelperClass.setMaxLength;
import static com.gagandeep.nuvococontacts.HelperClass.validatePhoneNumber;
import static com.gagandeep.nuvococontacts.SplashScreenActivity.currentUser;


public class ProfileFragment extends Fragment {


    private static final int PICK_IMAGE_REQUEST = 234;
    //    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference();
    Bitmap universalBitmap;
    Uri filePath;
    TextInputEditText editTextFirstName, editTextLastName, editTextDesignation, editTextLocation, editTextDepartment, editTextEmail, editTextEmail2, editTextPhoneNo1, editTextPhoneNo2, editTextPhoneNo3, editTextEmergencyPhone, editTextDeskNumber, editTextSapId, editTextEmployeeId;
    TextInputLayout phoneno_1Layout, phoneno_2Layout, phoneno_3Layout, email_1Layout, email_2Layout;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    ImageView chooseImageView;
    String id, firstName, lastName, designation, department, email_1, email_2, phone_1, phone_2, phone_3, location, profileUri, profileCacheUri, sapId, emergencyNumber, employeeId, deskNumber;
    KeyListener nameKeyListener, locationKeyListener, designationKeyListener, departmentKeyListener, email_1KeyListener, email_2KeyListener, phoneno_1KeyListener, phoneno_2KeyListener, phoneno_3KeyListener, sapIdKeyListener, employeeIdKeyListener, deskNumberKeyListener, emergencyNumberKeyListener, lastNameKeyListener;
    String newEmail_2, newPhone_2, newPhone_3, newProfileUri, newProfileCacheUri;
    TextView addImageTextView, uploadInfoTextView;
    FrameLayout frameLayout;
    ProgressBar uploadProgressBar;
    FloatingActionButton fab;
    int edit = 0, saveImageLocally = 0;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void findViews(View v) {


        editTextFirstName = v.findViewById(R.id.editTextFirstName);
        editTextLastName = v.findViewById(R.id.editTextLastName);
        editTextDepartment = v.findViewById(R.id.editTextDepartment);
        editTextDesignation = v.findViewById(R.id.editTextDesignation);
        editTextEmail = v.findViewById(R.id.editTextEmailId);
        editTextEmail2 = v.findViewById(R.id.editTextEmailId2);
        editTextLocation = v.findViewById(R.id.editTextLocation);
        editTextPhoneNo1 = v.findViewById(R.id.editTextPhoneNo1);
        editTextPhoneNo2 = v.findViewById(R.id.editTextPhoneNo2);
        editTextPhoneNo3 = v.findViewById(R.id.editTextPhoneNo3);
        chooseImageView = v.findViewById(R.id.chooseImageView);
        phoneno_1Layout = v.findViewById(R.id.phoneno_1EditTextLayout);
        phoneno_2Layout = v.findViewById(R.id.phoneno_2EditTextLayout);
        phoneno_3Layout = v.findViewById(R.id.phoneno_3EditTextLayout);
        email_1Layout = v.findViewById(R.id.email_1EditTextLayout);
        email_2Layout = v.findViewById(R.id.email_2EditTextLayout);
        addImageTextView = v.findViewById(R.id.addImageTextView);
        uploadProgressBar = v.findViewById(R.id.progressBarUploadImage);
        editTextEmergencyPhone = v.findViewById(R.id.editTextEmergencyPhone);
        editTextDeskNumber = v.findViewById(R.id.editTextDeskNumber);
        editTextSapId = v.findViewById(R.id.editTextSapId);
        editTextEmployeeId = v.findViewById(R.id.editTextEmployeeId);
        fab = v.findViewById(R.id.fab);
        uploadInfoTextView = v.findViewById(R.id.uploadInfoTextView);





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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap = getResizedBitmap(bitmap, 800, 600);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                chooseImageView.setImageBitmap(bitmap);
                newProfileCacheUri = uploadCachedImage(id);
                newProfileUri = uploadImage(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String uploadCachedImage(String id) {
        chooseImageView.setDrawingCacheEnabled(true);
        chooseImageView.buildDrawingCache();

        final StorageReference nameReference = storageRef.child(id + "cache.jpg");
        Bitmap bitmap = ((BitmapDrawable) chooseImageView.getDrawable()).getBitmap();
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
                        newProfileCacheUri = uri.toString();
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.e(TAG, "onComplete: " + task.getResult().getMetadata().getPath());

            }
        });

        return newProfileCacheUri;
    }

    String uploadImage(String id) {
        chooseImageView.setDrawingCacheEnabled(true);
        chooseImageView.buildDrawingCache();
        uploadProgressBar.setVisibility(View.VISIBLE);
        uploadInfoTextView.setVisibility(View.VISIBLE);

        final StorageReference nameReference = storageRef.child(id + ".jpg");
        Bitmap bitmap = ((BitmapDrawable) chooseImageView.getDrawable()).getBitmap();
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
                        newProfileUri = uri.toString();
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                uploadProgressBar.setVisibility(View.GONE);
                uploadInfoTextView.setText("Completed");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uploadInfoTextView.setVisibility(View.GONE);

                    }
                }, 2000);   //2000ms->2s




            }
        });

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
                int currentprogress = (int) progress;
                uploadProgressBar.setProgress(currentprogress);
                uploadInfoTextView.setText("Uploading...." + currentprogress + "%");



            }
        });


        return newProfileUri;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);




        findViews(v);
        disableKeyListeners();
        fillData();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit == 0) {
                    edit = 1;
                    showAllEditBox();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
                } else {
                    edit = 0;
                    updateData();


                }

            }
        });


        addImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        return v;
    }

    //fetch data from text views
    private void updateData() {
        firstName = editTextFirstName.getText().toString();
        lastName = editTextLastName.getText().toString();
        location = editTextLocation.getText().toString();
        designation = editTextDesignation.getText().toString();
        department = editTextDepartment.getText().toString();
        phone_1 = editTextPhoneNo1.getText().toString();
        phone_2 = editTextPhoneNo2.getText().toString();
        phone_3 = editTextPhoneNo3.getText().toString();
        email_1 = editTextEmail.getText().toString();
        email_2 = editTextEmail2.getText().toString();
        emergencyNumber = editTextEmergencyPhone.getText().toString();
        deskNumber = editTextDeskNumber.getText().toString();
        sapId = editTextSapId.getText().toString();
        employeeId = editTextEmployeeId.getText().toString();
        int validationCounter = 0;
        if (!TextUtils.isEmpty(phone_1)) {
            validationCounter = 1;
            if (validatePhoneNumber(phone_1, editTextPhoneNo1))
                validationCounter = 0;

        }
        if (!TextUtils.isEmpty(phone_2)) {
            validationCounter = 1;
            if (validatePhoneNumber(phone_2, editTextPhoneNo2))
                validationCounter = 0;

        }
        if (!TextUtils.isEmpty(phone_3)) {
            validationCounter = 1;
            if (validatePhoneNumber(phone_3, editTextPhoneNo3))
                validationCounter = 0;

        }
        if (validationCounter == 0) {
            uploadToFirebase();
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
            addImageTextView.setVisibility(View.GONE);
            if (TextUtils.isEmpty(newEmail_2))
                email_2Layout.setVisibility(View.GONE);
            if (TextUtils.isEmpty(newPhone_2))
                phoneno_2Layout.setVisibility(View.GONE);
            if (TextUtils.isEmpty(newPhone_3))
                phoneno_3Layout.setVisibility(View.GONE);
        }


    }

    private void uploadToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();

        if (!TextUtils.isEmpty(currentUser.getUserId()))
            mDatabaseRef.child(FIREBASE_USERINFO).child(currentUser.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().child(COLUMN_FIRST_NAME).setValue(firstName);
                    dataSnapshot.getRef().child(COLUMN_LAST_NAME).setValue(lastName);
                    dataSnapshot.getRef().child(COLUMN_EMAIL_1).setValue(email_1);
                    dataSnapshot.getRef().child(COLUMN_EMAIL_2).setValue(email_2);
                    dataSnapshot.getRef().child(COLUMN_PHONENO_1).setValue(phone_1);
                    dataSnapshot.getRef().child(COLUMN_PHONENO_2).setValue(phone_2);
                    dataSnapshot.getRef().child(COLUMN_PHONENO_3).setValue(phone_3);
                    dataSnapshot.getRef().child(COLUMN_LOCATION).setValue(location);
                    dataSnapshot.getRef().child(COLUMN_DEPARTMENT).setValue(department);
                    dataSnapshot.getRef().child(COLUMN_DESIGNATION).setValue(designation);
                    dataSnapshot.getRef().child(COLUMN_SAP_ID).setValue(sapId);
                    dataSnapshot.getRef().child(COLUMN_EMPLOYEE_ID).setValue(employeeId);
                    dataSnapshot.getRef().child(COLUMN_DESK_NUMBER).setValue(deskNumber);
                    dataSnapshot.getRef().child(COLUMN_EMERGENCY_NUMBER).setValue(emergencyNumber);
                    if (filePath != null && !TextUtils.isEmpty(newProfileUri)) {
                        saveImageLocally = 1;
                        dataSnapshot.getRef().child("profileUri").setValue(newProfileUri);
                        dataSnapshot.getRef().child("profileCacheUri").setValue(newProfileCacheUri);
                        Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getActivity(), "Profile Image was not updated, other data uploaded successfully", Toast.LENGTH_SHORT).show();

                    chooseImageView.setFocusable(true);

                    disableKeyListeners();
                    saveUserInfo();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }

    //Prevent User from Typing in below Text Views
    private void disableKeyListeners() {
        nameKeyListener = editTextFirstName.getKeyListener();
        editTextFirstName.setKeyListener(null);
        lastNameKeyListener = editTextLastName.getKeyListener();
        editTextLastName.setKeyListener(null);
        locationKeyListener = editTextLocation.getKeyListener();
        editTextLocation.setKeyListener(null);
        designationKeyListener = editTextDesignation.getKeyListener();
        editTextDesignation.setKeyListener(null);
        departmentKeyListener = editTextDepartment.getKeyListener();
        editTextDepartment.setKeyListener(null);
        phoneno_1KeyListener = editTextPhoneNo1.getKeyListener();
        editTextPhoneNo1.setKeyListener(null);
        phoneno_2KeyListener = editTextPhoneNo2.getKeyListener();
        editTextPhoneNo2.setKeyListener(null);
        phoneno_3KeyListener = editTextPhoneNo3.getKeyListener();
        editTextPhoneNo3.setKeyListener(null);
        email_1KeyListener = editTextEmail.getKeyListener();
        editTextEmail.setKeyListener(null);
        email_2KeyListener = editTextEmail2.getKeyListener();
        editTextEmail2.setKeyListener(null);
        employeeIdKeyListener = editTextEmployeeId.getKeyListener();
        editTextEmployeeId.setKeyListener(null);
        sapIdKeyListener = editTextSapId.getKeyListener();
        editTextSapId.setKeyListener(null);
        emergencyNumberKeyListener = editTextEmergencyPhone.getKeyListener();
        editTextEmergencyPhone.setKeyListener(null);
        deskNumberKeyListener = editTextDeskNumber.getKeyListener();
        editTextDeskNumber.setKeyListener(null);

        if (!TextUtils.isEmpty(phone_2))
            phoneno_2Layout.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(phone_3))
            phoneno_3Layout.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(email_2))
            email_2Layout.setVisibility(View.VISIBLE);
    }

    //Show edit box for field which previously were empty and also enable the ability to type in textboxes
    private void showAllEditBox() {
        phoneno_2Layout.setVisibility(View.VISIBLE);
        phoneno_3Layout.setVisibility(View.VISIBLE);
        email_2Layout.setVisibility(View.VISIBLE);
        addImageTextView.setVisibility(View.VISIBLE);

        editTextFirstName.setKeyListener(nameKeyListener);
        editTextDepartment.setKeyListener(departmentKeyListener);
        editTextLocation.setKeyListener(locationKeyListener);
        editTextDesignation.setKeyListener(designationKeyListener);
        editTextEmail.setKeyListener(email_1KeyListener);
        editTextEmail2.setKeyListener(email_2KeyListener);
        editTextPhoneNo1.setKeyListener(phoneno_1KeyListener);
        editTextPhoneNo2.setKeyListener(phoneno_2KeyListener);
        editTextPhoneNo3.setKeyListener(phoneno_3KeyListener);
        editTextDeskNumber.setKeyListener(deskNumberKeyListener);
        editTextEmergencyPhone.setKeyListener(emergencyNumberKeyListener);
        editTextSapId.setKeyListener(sapIdKeyListener);
        editTextEmployeeId.setKeyListener(employeeIdKeyListener);
        editTextLastName.setKeyListener(lastNameKeyListener);

    }

    //Fetch
    private void fillData() {
        firstName = currentUser.getFirstName();
        phone_1 = currentUser.getPhoneno_1();
        phone_2 = currentUser.getPhoneno_2();
        phone_3 = currentUser.getPhoneno_3();
        email_1 = currentUser.getEmail1();
        email_2 = currentUser.getEmail2();
        department = currentUser.getDepartment();
        designation = currentUser.getDesignation();
        location = currentUser.getLocation();
        id = currentUser.getUserId();
        profileUri = currentUser.getProfileUri();
        profileCacheUri = currentUser.getProfileCacheUri();
        deskNumber = currentUser.getDeskNumber();
        sapId = currentUser.getSapId();
        employeeId = currentUser.getEmployeeId();
        emergencyNumber = currentUser.getEmergencyNumber();
        lastName = currentUser.getLastName();

        editTextFirstName.setText("" + firstName);
        editTextLastName.setText("" + lastName);
        editTextDepartment.setText("" + department);
        editTextDesignation.setText("" + designation);
        editTextPhoneNo1.setText("" + phone_1);
        editTextEmail.setText("" + email_1);

        if (TextUtils.isEmpty(employeeId)) editTextEmployeeId.setText("");
        else editTextEmployeeId.setText("" + employeeId);


        if (TextUtils.isEmpty(sapId)) editTextSapId.setText("");
        else editTextSapId.setText("" + sapId);

        if (TextUtils.isEmpty(deskNumber)) editTextDeskNumber.setText("");
        else editTextDeskNumber.setText("" + deskNumber);

        if (TextUtils.isEmpty(emergencyNumber)) editTextEmergencyPhone.setText("");
        else editTextEmergencyPhone.setText("" + emergencyNumber);

        if (TextUtils.isEmpty(location)) editTextLocation.setText("");
        else editTextLocation.setText("" + location);
        if (TextUtils.isEmpty(designation)) editTextDesignation.setText("");
        else editTextDesignation.setText("" + designation);
        if (TextUtils.isEmpty(department)) editTextDepartment.setText("");
        else editTextDepartment.setText("" + department);


        setMaxLength(editTextPhoneNo3, PHONE_NUMBER_LENGTH);
        setMaxLength(editTextPhoneNo2, PHONE_NUMBER_LENGTH);
        setMaxLength(editTextPhoneNo1, PHONE_NUMBER_LENGTH);
        setMaxLength(editTextEmergencyPhone, PHONE_NUMBER_LENGTH);


        if (!TextUtils.isEmpty(phone_2)) {
            editTextPhoneNo2.setText(phone_2);
            phoneno_2Layout.setVisibility(View.VISIBLE);
        } else {
            phoneno_2Layout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(phone_3)) {
            editTextPhoneNo3.setText(phone_3);
            phoneno_3Layout.setVisibility(View.VISIBLE);
        } else {
            phoneno_3Layout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(email_2)) {
            editTextEmail2.setText(email_2);
            email_2Layout.setVisibility(View.VISIBLE);
        } else {
            email_2Layout.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(profileUri)) {
            Picasso.get().load(Uri.parse(profileUri)).placeholder(R.drawable.bg_placeholder).into(chooseImageView);
        }

    }


    void saveUserInfo() {
        ArrayList<String> set = new ArrayList<>();
        set.add(id);
        set.add(firstName);
        set.add(lastName);
        set.add(designation);
        set.add(location);
        set.add(email_1);
        set.add(email_2);
        set.add(phone_1);
        set.add(phone_2);
        set.add(phone_3);
        set.add(emergencyNumber);
        set.add(department);
        if (filePath != null && !TextUtils.isEmpty(newProfileUri) && saveImageLocally == 1) {
            set.add(newProfileUri);
            set.add(newProfileCacheUri);
            saveImageLocally = 0;
        } else {

            set.add(profileUri);
            set.add(profileCacheUri);
        }

        set.add(employeeId);
        set.add(currentUser.getAdminRights());
        set.add(deskNumber);
        set.add(sapId);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);

        sharedPreferences.edit().putString(CURRENT_USER, ObjectSerializer.serialize(set)).apply();
        sharedPreferences.edit().apply();
    }


}
