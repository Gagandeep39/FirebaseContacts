package com.gagandeep.nuvococontacts.Groups;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gagandeep.nuvococontacts.DbHelper;
import com.gagandeep.nuvococontacts.R;
import com.gagandeep.nuvococontacts.Search.SearchFragment;
import com.gagandeep.nuvococontacts.User;

import java.util.ArrayList;

import static com.gagandeep.nuvococontacts.Helpers.HelperClass.stringToArray;

public class GroupMemberActivity extends AppCompatActivity {
    public static ArrayList<User> groupMemberList;
    SQLiteDatabase database;
    DbHelper dbHelper;
    String memberName, memberNumber, groupName;
    int count, groupId;
    ListView listView;
    GroupMemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        findViews();
        initializeViews();


    }

    private void initializeViews() {

        groupMemberList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getIntExtra("groupId", 0);
            groupName = intent.getStringExtra("groupName");
            memberName = intent.getStringExtra("groupMemberName");
            memberNumber = intent.getStringExtra("groupMemberPhone");
            count = intent.getIntExtra("groupMemberCount", 0);
            getSupportActionBar().setTitle("" + groupName);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupMemberActivity.this, "Will allow adding more members", Toast.LENGTH_SHORT).show();
            }
        });
        String phoneNumbers[] = stringToArray(memberNumber);


        for (int i = 0; i < SearchFragment.userList.size(); i++) {
            for (int j = 0; j < phoneNumbers.length; j++) {
                if (phoneNumbers[j].equals(SearchFragment.userList.get(i).getPhoneno_1())) {
                    groupMemberList.add(SearchFragment.userList.get(i));
                }

            }
        }
        adapter = new GroupMemberAdapter(this, groupMemberList);
        listView.setAdapter(adapter);


    }


    private void findViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.listView);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_member, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_name:
                showEditDialogue();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEditDialogue() {

        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this);

        final View dialogueView = getLayoutInflater().inflate(R.layout.broadcast_dialogue, null);
        dialogueBuilder.setView(dialogueView);
        final TextInputEditText editTextMessage;
        TextInputLayout textInputLayout;
        Button saveButton, clearButton;
        editTextMessage = dialogueView.findViewById(R.id.editTextMessage);
        saveButton = dialogueView.findViewById(R.id.send);
        clearButton = dialogueView.findViewById(R.id.clear);
        textInputLayout = findViewById(R.id.textInputLayout);

        textInputLayout.setHint("Name");

        saveButton.setText("SAVE");
        dialogueBuilder.setTitle("Enter Group Name");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextMessage.setText("");
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextMessage.getText().toString();
                if (TextUtils.isEmpty(name))
                    Toast.makeText(getApplication(), "Name cannot be Empty", Toast.LENGTH_SHORT).show();
                else {
                    updateGroupNameInDatabase(name);
                    alertDialog.dismiss();
                }

            }
        });


    }

    private void updateGroupNameInDatabase(String name) {

        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
        boolean updated = dbHelper.updateGroupTable(groupId, name, memberName, memberNumber, database);
        if (updated) {
            getSupportActionBar().setTitle("" + name);
        }


    }

}
