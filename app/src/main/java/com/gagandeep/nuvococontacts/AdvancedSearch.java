package com.gagandeep.nuvococontacts;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdvancedSearch {
    public static final int NAME = 1;
    public static final int LOCATION = 2;
    public static final int DEPARTMENT = 3;
    public static final int PHONE = 4;
    ArrayList sortedList, userList;
    Query q;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userList.clear();
            for (DataSnapshot artistSnapShot : dataSnapshot.getChildren()) {
                User user = artistSnapShot.getValue(User.class);
                userList.add(user);

            }
            sortedList = userList;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public AdvancedSearch() {
        sortedList = new ArrayList();
        userList = new ArrayList();
    }

    public ArrayList<User> getSortedArrayList(String name) {
        FirebaseDatabase.getInstance().getReference("userinfo")
                .orderByChild("name").startAt(name);
        q.addListenerForSingleValueEvent(valueEventListener);
        return sortedList;
    }
}
