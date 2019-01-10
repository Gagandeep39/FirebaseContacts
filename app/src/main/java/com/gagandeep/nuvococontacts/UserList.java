package com.gagandeep.nuvococontacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;

    public UserList(Activity context, List<User> userList) {
        super(context, R.layout.list_layout, userList);
        this.context = context;
        this.userList = userList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageViewPhone, imageViewMail, imageViewWhatsApp, imageViewDetails;
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView nameTextView = listViewItem.findViewById(R.id.textViewName);
        TextView songTextView = listViewItem.findViewById(R.id.textViewLocation);
        imageViewPhone = listViewItem.findViewById(R.id.phone);
        imageViewMail = listViewItem.findViewById(R.id.email);
        imageViewWhatsApp = listViewItem.findViewById(R.id.whatsapp);
        imageViewDetails = listViewItem.findViewById(R.id.info);

        User user = userList.get(position);

        imageViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(userList.get(position).getName(), "name");
                b.putString(userList.get(position).getDesignation(), "designation");
                b.putString(userList.get(position).getLocation(), "location");
                b.putString(userList.get(position).getPhoneno_1(), "phoneno_1");
                b.putString(userList.get(position).getEmail1(), "email_1");

                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                intent.putExtras(b);
                getContext().startActivity(intent);
            }
        });

        nameTextView.setText(user.getName());
        songTextView.setText(user.getLocation());
        return listViewItem;
    }

}
