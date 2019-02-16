package com.gagandeep.nuvococontacts.Groups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gagandeep.nuvococontacts.R;

import java.util.List;

public class GroupAdapter extends ArrayAdapter<Group> {

    private Activity context;
    private List<Group> groupList;

    public GroupAdapter(Context context, List<Group> groupList) {
        super(context, R.layout.list_layout, groupList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        GroupViewHolder viewHolder = new GroupAdapter.GroupViewHolder();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_list_layout, null, true);

            viewHolder.groupNameTextView = convertView.findViewById(R.id.textViewGroupName);
            viewHolder.memberCountTextView = convertView.findViewById(R.id.textViewCount);
//            viewHolder.detailsImageView = convertView.findViewById(R.id.phone);
            viewHolder.sendImageView = convertView.findViewById(R.id.imageViewSend);


            viewHolder.groupNameTextView.setText(groupList.get(position).getGroupName());
            viewHolder.memberCountTextView.setText(groupList.get(position).getGroupMemberCount());
            viewHolder.sendImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialogue(position);
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupAdapter.GroupViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private void showMessageDialogue(int position) {
        String groupMembers = groupList.get(position).getGroupMembersPhone();

//        Toast.makeText(getContext(), "" +groupMembers, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = context.getLayoutInflater().inflate(R.layout.broadcast_dialogue, null);
        dialogueBuilder.setView(dialogueView);
        final TextInputEditText editTextMessage;
        Button sendButton, clearButton;
        editTextMessage = dialogueView.findViewById(R.id.editTextMessage);
        sendButton = dialogueView.findViewById(R.id.send);
        clearButton = dialogueView.findViewById(R.id.clear);
        dialogueBuilder.setTitle("Advance Search");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextMessage.setText("");
            }
        });

        final String finalNumber = groupMembers;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri sms_uri = Uri.parse("smsto:+91" + finalNumber);
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", "" + editTextMessage.getText().toString());
                getContext().startActivity(sms_intent);
            }
        });
    }

    static class GroupViewHolder {
        TextView groupNameTextView, memberCountTextView;
        ImageView detailsImageView, sendImageView;
        ;
    }

}
