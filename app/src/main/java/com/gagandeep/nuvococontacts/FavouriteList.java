package com.gagandeep.nuvococontacts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FavouriteList extends ArrayAdapter<FavouriteItem> {

    private Activity context;
    private List<FavouriteItem> userList;

    public FavouriteList(Activity context, List<FavouriteItem> userList) {
        super(context, R.layout.list_layout, userList);
        this.context = context;
        this.userList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        FavouriteItem user = userList.get(position);


        nameTextView.setText(user.getName());
        return listViewItem;
    }
}
