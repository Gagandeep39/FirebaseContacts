package com.gagandeep.nuvococontacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
                intentFunction(user);
            }
        });
        String imageString = user.getProfileUri();
        nameTextView.setText(user.getFirstName());
        if (!TextUtils.isEmpty(imageString))
            Picasso.get().load(Uri.parse(imageString)).into(profileImageView);
        return listViewItem;
    }

    private void intentFunction(User user) {
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra("firstname", user.getFirstName());
        intent.putExtra("lastname", user.getFirstName());
        intent.putExtra("designation", user.getDesignation());
        intent.putExtra("department", user.getDepartment());
        intent.putExtra("location", user.getLocation());
        intent.putExtra("email_1", user.getEmail1());
        intent.putExtra("email_2", user.getEmail2());
        intent.putExtra("phoneno_1", user.getPhoneno_1());
        intent.putExtra("phoneno_2", user.getPhoneno_2());
        intent.putExtra("phoneno_3", user.getPhoneno_3());
        intent.putExtra("profileuri", user.getProfileUri());
        intent.putExtra("employeeid", user.getEmployeeId());
        intent.putExtra("desknumber", user.getDeskNumber());
        intent.putExtra("sapid", user.getSapId());
        intent.putExtra("emergencynumber", user.getEmergencyNumber());
        intent.putExtra("employeeid", user.getEmployeeId());
        getContext().startActivity(intent);
    }

}
