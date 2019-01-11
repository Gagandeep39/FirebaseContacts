package com.gagandeep.nuvococontacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageView imageViewPhone, imageViewMail, imageViewWhatsApp, imageViewDetails, profileImageView;
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView nameTextView = listViewItem.findViewById(R.id.textViewName);
        TextView songTextView = listViewItem.findViewById(R.id.textViewLocation);
        imageViewPhone = listViewItem.findViewById(R.id.phone);
        imageViewMail = listViewItem.findViewById(R.id.email);
        imageViewWhatsApp = listViewItem.findViewById(R.id.whatsapp);
        imageViewDetails = listViewItem.findViewById(R.id.info);
        profileImageView = listViewItem.findViewById(R.id.imageView);

        User user = userList.get(position);
        String profileString = user.getProfileUri();
        if (!TextUtils.isEmpty(profileString)) {
            Bitmap b = stringToBitMap(profileString);
            profileImageView.setImageBitmap(b);
        }

        imageViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                Toast.makeText(context, "" + userList.get(position).getPhoneno_1(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                intent.putExtra("name", userList.get(position).getName());
                intent.putExtra("designation", userList.get(position).getDesignation());
                intent.putExtra("department", userList.get(position).getDepartment());
                intent.putExtra("location", userList.get(position).getLocation());
                intent.putExtra("email_1", userList.get(position).getEmail1());
                intent.putExtra("phoneno_1", userList.get(position).getPhoneno_1());
                intent.putExtra("profileuri", userList.get(position).getProfileUri());
                getContext().startActivity(intent);
            }
        });

        nameTextView.setText(user.getName());
        songTextView.setText(user.getLocation());
        return listViewItem;
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

}
