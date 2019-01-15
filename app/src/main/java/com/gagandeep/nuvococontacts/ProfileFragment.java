package com.gagandeep.nuvococontacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.gagandeep.nuvococontacts.AddUserActivity.TAG;
import static com.gagandeep.nuvococontacts.LoginActivity.currentUser;


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
    KeyListener nameKeyListener, locationKeyListener, designationKeyListener, departmentKeyListener, email_1KeyListener, email_2KeyListener, phoneno_1KeyListener, phoneno_2KeyListener, phoneno_3KeyListener;
    String newName, newDesignation, newDepartment, newEmail_1, newEmail_2, newPhone_1, newPhone_2, newPhone_3, newLocation, newImageString;
    TextView addImageTextView, phoneno_1TextView, phoneno_2TextView, phoneno_3TextView, email_1TextView, email_2TextView, whatsapp_1TextView, whatsapp_2TextView, whatsapp_3TextView;
    FrameLayout frameLayout;
    ProgressBar uploadProgressBar;
    FloatingActionButton fab;
    int edit = 0;

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





    }


    String getImageString() {
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bmp = getResizedBitmap(bmp, 800, 600);
        bmp.compress(Bitmap.CompressFormat.JPEG, 0, bYtE);
//        bmp.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imageFile;
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
                frameLayout.setVisibility(View.GONE);
                chooseImageView.setImageBitmap(bitmap);
                profileCacheUri = uploadCachedImage(id);
                profileUri = uploadImage(id);
                Toast.makeText(getContext(), "Displayed Image", Toast.LENGTH_SHORT).show();
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
        chooseImageView.setDrawingCacheEnabled(true);
        chooseImageView.buildDrawingCache();
        uploadProgressBar.setVisibility(View.VISIBLE);

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
        });


        addImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        return v;
    }

    private void updateData() {
        newName = editTextFirstName.getText().toString();
        newLocation = editTextLocation.getText().toString();
        newDesignation = editTextDesignation.getText().toString();
        newDepartment = editTextDepartment.getText().toString();
        newPhone_1 = editTextPhoneNo1.getText().toString();
        newPhone_2 = editTextPhoneNo2.getText().toString();
        newPhone_3 = editTextPhoneNo3.getText().toString();
        newEmail_1 = editTextEmail.getText().toString();
        newEmail_2 = editTextEmail2.getText().toString();

        if (filePath != null)
            newImageString = getImageString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();

        mDatabaseRef = database.getReference();
        mDatabaseRef.child("userinfo").child(currentUser.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("name").setValue(newName);
                dataSnapshot.getRef().child("email1").setValue(newEmail_1);
                dataSnapshot.getRef().child("email2").setValue(newEmail_2);
                dataSnapshot.getRef().child("phoneno_1").setValue(newPhone_1);
                dataSnapshot.getRef().child("phoneno_2").setValue(newPhone_2);
                dataSnapshot.getRef().child("phoneno_3").setValue(newPhone_3);
                dataSnapshot.getRef().child("location").setValue(newLocation);
                dataSnapshot.getRef().child("department").setValue(newDepartment);
                dataSnapshot.getRef().child("designation").setValue(newDesignation);
                if (filePath != null)
                    dataSnapshot.getRef().child("profileUri").setValue(newImageString);
                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                disableKeyListeners();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
    }

    private void disableKeyListeners() {
        nameKeyListener = editTextFirstName.getKeyListener();
        editTextFirstName.setKeyListener(null);
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
    }


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

    }

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

        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
        editTextDepartment.setText(department);
        editTextDesignation.setText(designation);
        editTextLocation.setText(location);
        editTextPhoneNo1.setText(phone_1);
        editTextEmail.setText(email_1);


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
            Bitmap b = stringToBitMap(profileUri);
            chooseImageView.setImageBitmap(b);
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

    // Allows user to login without credentials if already had Logged in before
    private User getUserInfo() {
        ArrayList<String> newArralist = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.gagandeep.nuvococontacts", Context.MODE_PRIVATE);
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

        return user;
    }

    void saveUserInfo() {
        ArrayList<String> set = new ArrayList<>();
        set.add(id);
        set.add(currentUser.getFirstName());
        set.add(currentUser.getLastName());
        set.add(currentUser.getDesignation());
        set.add(currentUser.getLocation());
        set.add(currentUser.getEmail1());
        set.add(currentUser.getEmail2());
        set.add(currentUser.getPhoneno_1());
        set.add(currentUser.getPhoneno_2());
        set.add(currentUser.getPhoneno_3());
        set.add(currentUser.getEmergencyNumber());
        set.add(currentUser.getDepartment());
        set.add(currentUser.getProfileUri());
        set.add(currentUser.getProfileCacheUri());
        set.add(currentUser.getEmployeeId());
        set.add(currentUser.getAdminRights());
        set.add(currentUser.getDeskNumber());
        set.add(currentUser.getSapId());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.gagandeep.nuvococontacts", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("currentuser", ObjectSerializer.serialize(set)).apply();
        sharedPreferences.edit().apply();
    }


}
