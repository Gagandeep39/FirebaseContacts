package com.gagandeep.nuvococontacts.Search;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gagandeep.nuvococontacts.Groups.Group;
import com.gagandeep.nuvococontacts.R;
import com.gagandeep.nuvococontacts.User;
import com.gagandeep.nuvococontacts.UserAdapter;

import java.util.ArrayList;

import static com.gagandeep.nuvococontacts.DbHelper.groupmemberCount;
import static com.gagandeep.nuvococontacts.Search.SearchFragment.linearLayoutBroadcast;
import static com.gagandeep.nuvococontacts.Search.SearchFragment.userList;

public class CreateGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        findViews();
    }

    private void findViews() {
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

    private void sendBroadcast() {
        ArrayList<String> numberList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).selected)
                numberList.add(userList.get(i).getPhoneno_1());
        }

        linearLayoutBroadcast.setVisibility(View.GONE);
    }

}
