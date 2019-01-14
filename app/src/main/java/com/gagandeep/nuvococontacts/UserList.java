package com.gagandeep.nuvococontacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.gagandeep.nuvococontacts.LoginActivity.applicationUser;

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
        ImageView imageViewPhone, imageViewMail, imageViewMessage, imageViewDetails;
        ConstraintLayout rootLayout;
        CircleImageView profileImageView;
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView nameTextView = listViewItem.findViewById(R.id.textViewName);
        TextView locationTextView = listViewItem.findViewById(R.id.textViewLocation);
        imageViewPhone = listViewItem.findViewById(R.id.phone);
        imageViewMail = listViewItem.findViewById(R.id.email);
        imageViewMessage = listViewItem.findViewById(R.id.message);
        imageViewDetails = listViewItem.findViewById(R.id.info);
        profileImageView = listViewItem.findViewById(R.id.imageView);
        rootLayout = listViewItem.findViewById(R.id.rootLayout);

        final User user = userList.get(position);
        String profileString = user.getProfileCacheUri();
        if (!TextUtils.isEmpty(profileString)) {

            Picasso.get().load(Uri.parse(profileString))
                    .error(R.drawable.bg_placeholder)
                    .resize(50, 50)
                    .into(profileImageView);
        }

        imageViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentFunction(user);

            }
        });


        imageViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(user.getPhoneno_2()))
                    showAlertDialogue(user);
                else callFunction(user.getPhoneno_1());
            }
        });

        imageViewMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(user.getEmail2()))
                    showEmailAlertDialogue(user);
                else emailFunction(user.getEmail1());
            }
        });

        imageViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(user.getPhoneno_2()))
                    showMessageAlertDialogue(user);
                else messageFunction(user.getPhoneno_1());
            }
        });
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentFunction(user);
            }
        });

        String name = user.getUserId();
        if (name.equals(applicationUser))
            nameTextView.setText("You");
        else
            nameTextView.setText(user.getFirstName() + " " + user.getLastName());
        locationTextView.setText(user.getLocation());
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

    private void showMessageAlertDialogue(final User user) {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = context.getLayoutInflater().inflate(R.layout.select_phone_dialogue, null);
        dialogueBuilder.setView(dialogueView);

        TextView message_1TextView, message_2TextView, message_3TextView;
        message_1TextView = dialogueView.findViewById(R.id.phoneno_1TextView);
        message_2TextView = dialogueView.findViewById(R.id.phoneno_2TextView);
        message_3TextView = dialogueView.findViewById(R.id.phoneno_3TextView);

        message_1TextView.setText(user.getPhoneno_1());
        message_2TextView.setVisibility(View.VISIBLE);
        message_2TextView.setText(user.getPhoneno_2());
        if (!TextUtils.isEmpty(user.getPhoneno_3())) {
            message_3TextView.setVisibility(View.VISIBLE);
            message_3TextView.setText(user.getPhoneno_3());
        }


        message_1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageFunction(user.getPhoneno_1());
            }
        });
        message_2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageFunction(user.getPhoneno_2());
            }
        });
        message_3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageFunction(user.getPhoneno_3());
            }
        });

        dialogueBuilder.setTitle("Message To");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();
    }

    private void showEmailAlertDialogue(final User user) {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = context.getLayoutInflater().inflate(R.layout.select_phone_dialogue, null);
        dialogueBuilder.setView(dialogueView);

        TextView email_1TextView, email_2TextView, email_3TextView;
        email_1TextView = dialogueView.findViewById(R.id.phoneno_1TextView);
        email_2TextView = dialogueView.findViewById(R.id.phoneno_2TextView);
        email_3TextView = dialogueView.findViewById(R.id.phoneno_3TextView);

        email_1TextView.setText(user.getEmail1());
        email_2TextView.setVisibility(View.VISIBLE);
        email_2TextView.setText(user.getEmail2());


        email_1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailFunction(user.getEmail1());
            }
        });
        email_2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailFunction(user.getEmail2());
            }
        });

        dialogueBuilder.setTitle("Email");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();
    }

    private void showAlertDialogue(final User user) {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = context.getLayoutInflater().inflate(R.layout.select_phone_dialogue, null);
        dialogueBuilder.setView(dialogueView);

        TextView phoneno_1TextView, phoneno_2TextView, phoneno_3TextView;
        phoneno_1TextView = dialogueView.findViewById(R.id.phoneno_1TextView);
        phoneno_2TextView = dialogueView.findViewById(R.id.phoneno_2TextView);
        phoneno_3TextView = dialogueView.findViewById(R.id.phoneno_3TextView);

        phoneno_1TextView.setText(user.getPhoneno_1());
        phoneno_2TextView.setVisibility(View.VISIBLE);
        phoneno_2TextView.setText(user.getPhoneno_2());

        if (!TextUtils.isEmpty(user.getPhoneno_3())) {
            phoneno_3TextView.setVisibility(View.VISIBLE);
            phoneno_3TextView.setText(user.getPhoneno_3());
        }

        phoneno_1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFunction(user.getPhoneno_1());
            }
        });
        phoneno_2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFunction(user.getPhoneno_2());
            }
        });
        phoneno_3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFunction(user.getPhoneno_3());
            }
        });

        dialogueBuilder.setTitle("Call");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();
    }

    private void callFunction(String num) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+91" + num));
        context.startActivity(intent);
    }

    private void emailFunction(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void messageFunction(String message) {
        Uri sms_uri = null;
        sms_uri = Uri.parse("smsto:+91" + message);

        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
//        sms_intent.putExtra("sms_body", "Good Morning ! how r U ?");
        context.startActivity(sms_intent);
    }

}
