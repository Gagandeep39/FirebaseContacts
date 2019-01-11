package com.gagandeep.nuvococontacts;

import android.app.Activity;
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

        User user = userList.get(position);


        String imageString = user.getProfileUri();
        nameTextView.setText(user.getName());
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
