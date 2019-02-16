package com.gagandeep.nuvococontacts.Search;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gagandeep.nuvococontacts.CreateGroupAdapter;
import com.gagandeep.nuvococontacts.DbHelper;
import com.gagandeep.nuvococontacts.Groups.Group;
import com.gagandeep.nuvococontacts.R;
import com.gagandeep.nuvococontacts.User;
import com.gagandeep.nuvococontacts.UserAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.gagandeep.nuvococontacts.DbHelper.groupmemberCount;
import static com.gagandeep.nuvococontacts.Search.SearchFragment.userList;

public class CreateGroupActivity extends AppCompatActivity {


    TextInputEditText editTextCreateGroup;
    ListView listView;
    Button buttonCancel, buttonSave;
    CreateGroupAdapter adapter;
    SQLiteDatabase database;
    DbHelper dbHelper;
    ArrayList<User> sortedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        findViews();
        showListView();
        onClickListeners();
    }

    private void onClickListeners() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void showListView() {
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User item, User t1) {
                String s1 = item.getName();
                String s2 = t1.getName();
                return s1.compareToIgnoreCase(s2);
            }
        });

        adapter = new CreateGroupAdapter(this, userList);
        TextView empty = new TextView(this);
        empty.setHeight(150);
        listView.addFooterView(empty);
        listView.setAdapter(adapter);

        listView.setScrollingCacheEnabled(true);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    }

    private void findViews() {
        editTextCreateGroup = findViewById(R.id.editTextCreateGroup);
        buttonCancel = findViewById(R.id.cancel);
        buttonSave = findViewById(R.id.save);
        listView = findViewById(R.id.listView);
        sortedList = new ArrayList<>();



    }


    public void showAlertDialogue() {
        ArrayList<String> numberList = new ArrayList<>();
        String number = "";
        for (int i = 0; i < numberList.size(); i++) {
            number += numberList.get(i);
            if (i != numberList.size() - 1)
                number += ", ";
        }
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this);

        View dialogueView = getLayoutInflater().inflate(R.layout.create_group_dialogue, null);
        dialogueBuilder.setView(dialogueView);
        dialogueBuilder.setTitle("Member List");
        ArrayList<Group> groups = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<User> groupList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            for (int j = 0; j < numberList.size(); j++) {
                if (userList.get(i).getPhoneno_1().equals(numberList.get(j))) {
                    name.add(userList.get(i).getName());


                    groupList.add(userList.get(i));
                }
            }
        }

        Toast.makeText(this, "" + groupList.size(), Toast.LENGTH_SHORT).show();
        EditText editTextGroupName = dialogueView.findViewById(R.id.groupName);
        Button cancelButton = dialogueView.findViewById(R.id.cancel);
        Button sendButton = dialogueView.findViewById(R.id.send);
        ListView listView = dialogueView.findViewById(R.id.listView);
        UserAdapter adapter = new UserAdapter(this, groupList);
        listView.setAdapter(adapter);
        int memberCount = groupmemberCount(number);

        String groupName = editTextGroupName.getText().toString();
        groups.add(new Group(1, groupName, number, name + "", memberCount));
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();
        dialogueBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

    }


    ///rename the functio name
    private void createGroup() {
        String groupName = editTextCreateGroup.getText().toString();
        ArrayList<String> numberList = new ArrayList<>();
        ArrayList<String> nameList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).selected) {
                numberList.add(userList.get(i).getPhoneno_1());
                nameList.add(userList.get(i).getName());

            }
        }

        if (TextUtils.isEmpty(groupName))
            editTextCreateGroup.setError("Name cannot be empty");
        else if (numberList.isEmpty())
            Toast.makeText(this, "Added members to the groups", Toast.LENGTH_SHORT).show();
        else {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            dbHelper.saveToGroupTable(groupName, nameList + "", numberList + "", database);
            nameList.clear();
            numberList.clear();
            for (int i = 0; i < userList.size(); i++) {
                userList.get(i).setSelected(false);
            }
            finish();
        }


//        Toast.makeText(this, "" + numberList, Toast.LENGTH_SHORT).show();
    }

}
