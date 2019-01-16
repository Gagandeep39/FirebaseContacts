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

import static com.gagandeep.nuvococontacts.Constants.COLUMN_DEPARTMENT;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_DESIGNATION;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_DESK_NUMBER;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_EMAIL_1;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_EMAIL_2;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_EMERGENCY_NUMBER;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_EMPLOYEE_ID;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_FIRST_NAME;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_LAST_NAME;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_LOCATION;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_PHONENO_1;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_PHONENO_2;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_PHONENO_3;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_PROFILE_URI;
import static com.gagandeep.nuvococontacts.Constants.COLUMN_SAP_ID;

//Adapter to show items in Favourites list
public class GridViewAdapter extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;

    public GridViewAdapter(Activity context, List<User> userList) {
        super(context, R.layout.grid_list, userList);
        this.context = context;
        this.userList = userList;
    }


    //Show Text at the bottom of image
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
        String imageString = user.getProfileCacheUri();
        nameTextView.setText(user.getFirstName());
        if (!TextUtils.isEmpty(imageString))
            Picasso.get().load(Uri.parse(imageString)).placeholder(R.drawable.bg_placeholder).into(profileImageView);
        return listViewItem;
    }

    //To pass data to other activity
    private void intentFunction(User user) {
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra(COLUMN_FIRST_NAME, user.getFirstName());
        intent.putExtra(COLUMN_LAST_NAME, user.getLastName());
        intent.putExtra(COLUMN_DESIGNATION, user.getDesignation());
        intent.putExtra(COLUMN_DEPARTMENT, user.getDepartment());
        intent.putExtra(COLUMN_LOCATION, user.getLocation());
        intent.putExtra(COLUMN_EMAIL_1, user.getEmail1());
        intent.putExtra(COLUMN_EMAIL_2, user.getEmail2());
        intent.putExtra(COLUMN_PHONENO_1, user.getPhoneno_1());
        intent.putExtra(COLUMN_PHONENO_2, user.getPhoneno_2());
        intent.putExtra(COLUMN_PHONENO_3, user.getPhoneno_3());
        intent.putExtra(COLUMN_PROFILE_URI, user.getProfileUri());
        intent.putExtra(COLUMN_EMPLOYEE_ID, user.getEmployeeId());
        intent.putExtra(COLUMN_DESK_NUMBER, user.getDeskNumber());
        intent.putExtra(COLUMN_SAP_ID, user.getSapId());
        intent.putExtra(COLUMN_EMERGENCY_NUMBER, user.getEmergencyNumber());
        getContext().startActivity(intent);
    }

}
