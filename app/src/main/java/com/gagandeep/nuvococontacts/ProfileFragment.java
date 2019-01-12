package com.gagandeep.nuvococontacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;


public class ProfileFragment extends Fragment {

    TextInputEditText editTextName, editTextDesignation, editTextLocation, editTextDepartment, editTextEmail, editTextPhoneNo1, editTextPhoneNo2;
    DatabaseReference databaseReferenceUser;
    List<User> userList;
    private static final int PICK_IMAGE_REQUEST = 234;
    Button saveButton, profileButton;
    ImageView profileImageView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Bitmap universalBitmap;
    Uri filePath;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReferenceUser = database.getReference();
    }

    private void findViews(View v) {

        saveButton = v.findViewById(R.id.buttonSave);
        editTextName = v.findViewById(R.id.editTextName);
        editTextDepartment = v.findViewById(R.id.editTextDepartment);
        editTextDesignation = v.findViewById(R.id.editTextDesignation);
        editTextEmail = v.findViewById(R.id.editTextEmailId);
        editTextLocation = v.findViewById(R.id.editTextLocation);
        editTextPhoneNo1 = v.findViewById(R.id.editTextPhoneNo1);
        editTextPhoneNo2 = v.findViewById(R.id.editTextPhoneNo2);
        profileButton = v.findViewById(R.id.profileButton);
        profileImageView = v.findViewById(R.id.profileImageView);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDataToServer(v);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void saveDataToServer(View v) {
        String name = editTextName.getText().toString();
        String department = editTextDepartment.getText().toString();
        String designation = editTextDepartment.getText().toString();
        String email = editTextEmail.getText().toString();
        String location = editTextLocation.getText().toString();
        String phoneno1 = editTextPhoneNo1.getText().toString();
        String phoneno2 = editTextPhoneNo2.getText().toString();

        if (TextUtils.isEmpty(name))
            editTextName.setError("Enter Name");
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
        else {
            String id = databaseReferenceUser.push().getKey();
            String f = uploadImage();
            User user = new User(id, name, designation, location, email, null, phoneno1, phoneno2, null, department, f);
            databaseReferenceUser.child(id).setValue(user);
            Toast.makeText(v.getContext(), "Data Inserted Successfully", Toast.LENGTH_SHORT).show();

            editTextName.setText("");
            editTextDepartment.setText("");
            editTextDesignation.setText("");
            editTextLocation.setText("");
            editTextEmail.setText("");
            editTextPhoneNo1.setText("");
            editTextPhoneNo2.setText("");
//            uploadImage(phoneno1);
        }

    }


    private void uploadImage(String phone) {
        if (filePath != null) {

            final StorageReference childRef = storageRef.child("_" + phone + "_" + ".jpg");

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.i(TAG, "onSuccess: " + uri);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onFailure: " + e);
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Log.i(TAG, "onComplete: " + task.getResult());
                    task.getResult().getMetadata().getPath();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    String uploadImage() {
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bmp = getResizedBitmap(bmp, 800, 600);
        bmp.compress(Bitmap.CompressFormat.JPEG, 0, bYtE);
        bmp.recycle();
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, out);
                profileImageView.setImageBitmap(bitmap);
                universalBitmap = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(v);
        userList = new ArrayList<>();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("userinfo");
        databaseReferenceUser.keepSynced(true);
        return v;
    }


}
