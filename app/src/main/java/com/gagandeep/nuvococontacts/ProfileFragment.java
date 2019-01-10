package com.gagandeep.nuvococontacts;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    Button saveButton;
    TextInputEditText editTextName, editTextDesignation, editTextLocation, editTextDepartment, editTextEmail, editTextPhoneNo1, editTextPhoneNo2;
    DatabaseReference databaseReferenceUser;
    List<User> userList;

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDataToServer(v);
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
            User user = new User(id, name, designation, location, email, null, phoneno1, phoneno2, null, department);
            databaseReferenceUser.child(id).setValue(user);
            Toast.makeText(v.getContext(), "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
            editTextName.setText("");
            editTextDepartment.setText("");
            editTextDesignation.setText("");
            editTextLocation.setText("");
            editTextEmail.setText("");
            editTextPhoneNo1.setText("");
            editTextPhoneNo2.setText("");
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
