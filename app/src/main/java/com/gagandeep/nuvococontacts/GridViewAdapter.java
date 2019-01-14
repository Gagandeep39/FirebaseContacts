package com.gagandeep.nuvococontacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class GridViewAdapter extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;

    public GridViewAdapter(Activity context, List<User> userList) {
        super(context, R.layout.grid_list, userList);
        this.context = context;
        this.userList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView profileImageView;
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.grid_list, null, true);
        TextView nameTextView = listViewItem.findViewById(R.id.nameTextView);
        profileImageView = listViewItem.findViewById(R.id.profileImageView);

        final User user = userList.get(position);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + user.getPhoneno_1(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                intent.putExtra("name", user.getFirstName());
                intent.putExtra("designation", user.getDesignation());
                intent.putExtra("department", user.getDepartment());
                intent.putExtra("location", user.getLocation());
                intent.putExtra("email_1", user.getEmail1());
                intent.putExtra("phoneno_1", user.getPhoneno_1());
                intent.putExtra("profileuri", user.getProfileUri());
                getContext().startActivity(intent);
            }
        });
        String imageString = user.getProfileUri();
        nameTextView.setText(user.getFirstName());
        if (!TextUtils.isEmpty(imageString))
            profileImageView.setImageBitmap(stringToBitMap(imageString));
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
