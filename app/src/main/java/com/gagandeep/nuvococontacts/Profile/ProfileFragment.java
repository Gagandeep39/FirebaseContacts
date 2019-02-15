package com.gagandeep.nuvococontacts.Profile;

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

import com.gagandeep.nuvococontacts.Helpers.ObjectSerializer;
import com.gagandeep.nuvococontacts.R;
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
import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_DEPARTMENT;
import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_DESIGNATION;
import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_DESK_NUMBER;
import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_DIVISION;
import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_EMAIL_2;
import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_EMERGENCY_NUMBER;
import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_FIRST_NAME;
import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_LOCATION;
import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_PHONENO_2;
import static com.gagandeep.nuvococontacts.Helpers.Constants.CURRENT_USER;
import static com.gagandeep.nuvococontacts.Helpers.Constants.FIREBASE_USERINFO;
import static com.gagandeep.nuvococontacts.Helpers.Constants.PACKAGE_NAME;
import static com.gagandeep.nuvococontacts.Helpers.Constants.PHONE_NUMBER_LENGTH;
import static com.gagandeep.nuvococontacts.Helpers.HelperClass.setMaxLength;
import static com.gagandeep.nuvococontacts.Helpers.HelperClass.validatePhoneNumber;
import static com.gagandeep.nuvococontacts.Login.SplashScreenActivity.currentUser;

//import static com.gagandeep.nuvococontacts.Helpers.Constants.COLUMN_EMPLOYEE_ID;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 234;
    Uri filePath;
    TextInputEditText editTextname, editTextdesignation, editTextLocation, editTextdepartment, editTextEmail, editTextEmail2, editTextDivision, editTextPhoneNo1, editTextPhoneNo2, editTextEmergencyPhone, editTextDeskNumber, editTextSapId, editTextEmployeeId;
    TextInputLayout name_Layout, emergencyNumber_Layout, phoneno_2Layout, email_2Layout, desknumber_Layout, emergencynumber_Layout, designation_Layout, department_Layout, location_Layout, division_Layout;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    ImageView chooseImageView;
    // 14 String Fields Defined....
    String employeeId, sapId, name, department, location, designation, division, email_1, email_2, phoneno_1, phoneno_2, profileCacheUri, emergencyNumber, profileUri, deskNumber;
    KeyListener nameKeyListener, divisionKeyListener, locationKeyListener, designationKeyListener, departmentKeyListener, email_1KeyListener, email_2KeyListener, phoneno_1KeyListener, phoneno_2KeyListener, sapIdKeyListener, employeeIdKeyListener, deskNumberKeyListener, emergencyNumberKeyListener;
    String newEmail_2, newphoneno_2, newProfileUri, newProfileCacheUri;
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
        chooseImageView = v.findViewById(R.id.chooseImageView);
        addImageTextView = v.findViewById(R.id.addImageTextView);
        uploadProgressBar = v.findViewById(R.id.progressBarUploadImage);
        fab = v.findViewById(R.id.fab);
        uploadInfoTextView = v.findViewById(R.id.uploadInfoTextView);
        editTextEmployeeId = v.findViewById(R.id.editTextEmployeeId);
        editTextSapId = v.findViewById(R.id.editTextSapId);
        editTextname = v.findViewById(R.id.editTextName);
        editTextdepartment = v.findViewById(R.id.editTextDepartment);
        editTextLocation = v.findViewById(R.id.editTextLocation);
        editTextdesignation = v.findViewById(R.id.editTextDesignation);
        editTextDivision = v.findViewById(R.id.editTextDivision);
        editTextEmail = v.findViewById(R.id.editTextEmailId);
        editTextEmail2 = v.findViewById(R.id.editTextEmailId2);
        editTextPhoneNo1 = v.findViewById(R.id.editTextPhoneNo1);
        editTextPhoneNo2 = v.findViewById(R.id.editTextPhoneNo2);
        editTextEmergencyPhone = v.findViewById(R.id.editTextEmergencyPhone);
        editTextDeskNumber = v.findViewById(R.id.editTextDeskNumber);

        //phoneno_1Layout = v.findViewById(R.id.phoneno_1EditTextLayout);
        //email_1Layout = v.findViewById(R.id.email_1EditTextLayout);
        name_Layout = v.findViewById(R.id.nameEditTextLayout);
        phoneno_2Layout = v.findViewById(R.id.phoneno_2EditTextLayout);
        email_2Layout = v.findViewById(R.id.email_2EditTextLayout);
        desknumber_Layout = v.findViewById(R.id.DeskNumberEditTextLayout);
        designation_Layout = v.findViewById(R.id.designationEditTextLayout);
        department_Layout = v.findViewById(R.id.departmentEditTextLayout);
        location_Layout = v.findViewById(R.id.locationEditTextLayout);
        division_Layout = v.findViewById(R.id.divisionEditTextLayout);
        emergencyNumber_Layout = v.findViewById(R.id.emergencynumberEditTextLayout);
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
                chooseImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                chooseImageView.setImageBitmap(bitmap);
                newProfileCacheUri = uploadCachedImage(sapId);
                newProfileUri = uploadImage(sapId);
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
        name = editTextname.getText().toString();
        location = editTextLocation.getText().toString();
        division = editTextDivision.getText().toString();
        designation = editTextdesignation.getText().toString();
        department = editTextdepartment.getText().toString();
        phoneno_2 = editTextPhoneNo2.getText().toString();
        email_2 = editTextEmail2.getText().toString();
        emergencyNumber = editTextEmergencyPhone.getText().toString();
        deskNumber = editTextDeskNumber.getText().toString();

        int validationCounter = 0;
        if (!TextUtils.isEmpty(phoneno_1)) {
            validationCounter = 1;
            if (validatePhoneNumber(phoneno_1, editTextPhoneNo1))
                validationCounter = 0;
        }
        if (!TextUtils.isEmpty(phoneno_2)) {
            validationCounter = 1;
            if (validatePhoneNumber(phoneno_2, editTextPhoneNo2))
                validationCounter = 0;
        }
        if (validationCounter == 0) {
            uploadToFirebase();
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
            addImageTextView.setVisibility(View.GONE);
            if (TextUtils.isEmpty(newEmail_2))
                email_2Layout.setVisibility(View.GONE);
            if (TextUtils.isEmpty(newphoneno_2))
                phoneno_2Layout.setVisibility(View.GONE);
        }
    }

    private void uploadToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();

        if (!TextUtils.isEmpty(currentUser.getSapId()))
            mDatabaseRef.child(FIREBASE_USERINFO).child(currentUser.getSapId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().child(COLUMN_FIRST_NAME).setValue(name);
                    dataSnapshot.getRef().child(COLUMN_DEPARTMENT).setValue(department);
                    dataSnapshot.getRef().child(COLUMN_LOCATION).setValue(location);
                    dataSnapshot.getRef().child(COLUMN_DESIGNATION).setValue(designation);
                    dataSnapshot.getRef().child(COLUMN_DIVISION).setValue(division);
                    dataSnapshot.getRef().child(COLUMN_EMAIL_2).setValue(email_2);
                    dataSnapshot.getRef().child(COLUMN_PHONENO_2).setValue(phoneno_2);
                    dataSnapshot.getRef().child(COLUMN_EMERGENCY_NUMBER).setValue(emergencyNumber);
                    dataSnapshot.getRef().child(COLUMN_DESK_NUMBER).setValue(deskNumber);

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
        nameKeyListener = editTextname.getKeyListener();
        editTextname.setKeyListener(null);
        locationKeyListener = editTextLocation.getKeyListener();
        editTextLocation.setKeyListener(null);
        divisionKeyListener = editTextDivision.getKeyListener();
        editTextDivision.setKeyListener(null);
        designationKeyListener = editTextdesignation.getKeyListener();
        editTextdesignation.setKeyListener(null);
        departmentKeyListener = editTextdepartment.getKeyListener();
        editTextdepartment.setKeyListener(null);
        phoneno_1KeyListener = editTextPhoneNo1.getKeyListener();
        editTextPhoneNo1.setKeyListener(null);
        phoneno_2KeyListener = editTextPhoneNo2.getKeyListener();
        editTextPhoneNo2.setKeyListener(null);
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
        if (!TextUtils.isEmpty(phoneno_2))
            phoneno_2Layout.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(email_2))
            email_2Layout.setVisibility(View.VISIBLE);
    }

    //Show edit box for field which previously were empty and also enable the ability to type in textboxes
    private void showAllEditBox() {
        addImageTextView.setVisibility(View.VISIBLE);
        name_Layout.setVisibility(View.VISIBLE);
        phoneno_2Layout.setVisibility(View.VISIBLE);
        email_2Layout.setVisibility(View.VISIBLE);
        department_Layout.setVisibility(View.VISIBLE);
        location_Layout.setVisibility(View.VISIBLE);
        designation_Layout.setVisibility(View.VISIBLE);
        division_Layout.setVisibility(View.VISIBLE);
        emergencyNumber_Layout.setVisibility(View.VISIBLE);
        desknumber_Layout.setVisibility(View.VISIBLE);

        editTextname.setKeyListener(nameKeyListener);
//        editTextEmail.setKeyListener(email_1KeyListener);
//        editTextPhoneNo1.setKeyListener(phoneno_1KeyListener);
        editTextEmail2.setKeyListener(email_2KeyListener);
        editTextPhoneNo2.setKeyListener(phoneno_2KeyListener);
        editTextEmergencyPhone.setKeyListener(emergencyNumberKeyListener);
        editTextDeskNumber.setKeyListener(deskNumberKeyListener);
        editTextdesignation.setKeyListener(designationKeyListener);
        editTextdepartment.setKeyListener(departmentKeyListener);
        editTextLocation.setKeyListener(locationKeyListener);
        editTextdepartment.setKeyListener(departmentKeyListener);
        editTextDivision.setKeyListener(divisionKeyListener);

    }

    //Fetch
    private void fillData() {
        uploadProgressBar.setVisibility(View.GONE);
        employeeId = currentUser.getEmployeeId();
        sapId = currentUser.getSapId();
        name = currentUser.getName();
        department = currentUser.getDepartment();
        location = currentUser.getLocation();
        designation = currentUser.getDesignation();
        division = currentUser.getDivision();
        email_1 = currentUser.getEmail1();
        email_2 = currentUser.getEmail2();
        phoneno_1 = currentUser.getPhoneno_1();
        phoneno_2 = currentUser.getPhoneno_2();
        emergencyNumber = currentUser.getEmergencyNumber();
        deskNumber = currentUser.getDeskNumber();
        profileUri = currentUser.getProfileUri();
        profileCacheUri = currentUser.getProfileCacheUri();

        editTextEmployeeId.setText("" + employeeId);
        editTextSapId.setText("" + sapId);
        editTextname.setText("" + name);
        editTextdepartment.setText("" + department);
        editTextLocation.setText("" + location);
        editTextdesignation.setText("" + designation);
        editTextDivision.setText("" + division);
        editTextEmail.setText("" + email_1);
        editTextPhoneNo1.setText("" + phoneno_1);
        editTextEmergencyPhone.setText("" + emergencyNumber);
        editTextDeskNumber.setText("" + deskNumber);

        setMaxLength(editTextPhoneNo2, PHONE_NUMBER_LENGTH);
        setMaxLength(editTextPhoneNo1, PHONE_NUMBER_LENGTH);
        setMaxLength(editTextEmergencyPhone, PHONE_NUMBER_LENGTH);

        if (!TextUtils.isEmpty(phoneno_2)) {
            editTextPhoneNo2.setText("" + phoneno_2);
            phoneno_2Layout.setVisibility(View.VISIBLE);
        } else {
            phoneno_2Layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(email_2)) {
            editTextEmail2.setText("" + email_2);
            email_2Layout.setVisibility(View.VISIBLE);
        } else {
            email_2Layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(profileUri)) {
            Picasso.get().load(Uri.parse(profileUri)).placeholder(R.drawable.bg_placeholder).into(chooseImageView);
        }
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
        set.add(sapId);
        set.add(name);
        set.add(department);
        set.add(location);
        set.add(designation);
        set.add(division);
        set.add(employeeId);
        set.add(email_1);
        set.add(email_2);
        set.add(phoneno_1);
        set.add(phoneno_2);
        set.add(currentUser.getAdminRights());
        set.add(deskNumber);
        set.add(emergencyNumber);
        if (filePath != null && !TextUtils.isEmpty(newProfileUri) && saveImageLocally == 1) {
            set.add(newProfileCacheUri);
            set.add(newProfileUri);
            saveImageLocally = 0;
        } else {
            set.add(profileCacheUri);
            set.add(profileUri);
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(CURRENT_USER, ObjectSerializer.serialize(set)).apply();
        sharedPreferences.edit().apply();
    }
}
