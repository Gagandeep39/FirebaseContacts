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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.gagandeep.nuvococontacts.Login.SplashScreenActivity.currentUser;

public class CreateGroupAdapter extends ArrayAdapter<User> {
    boolean checkBoxState[];
    private Activity context;
    private List<User> userList;

    public CreateGroupAdapter(Activity context, List<User> userList) {
        super(context, R.layout.list_layout, userList);
        this.context = context;
        this.userList = userList;
        checkBoxState = new boolean[userList.size()];
        for (int i = 0; i < userList.size(); i++)
            checkBoxState[i] = false;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.create_group_list_layout, null, true);

            viewHolder.nameTextView = convertView.findViewById(R.id.textViewName);
            viewHolder.locationTextView = convertView.findViewById(R.id.textViewLocation);
            viewHolder.imageViewPhone = convertView.findViewById(R.id.phone);
            viewHolder.imageViewMail = convertView.findViewById(R.id.email);
            viewHolder.imageViewMessage = convertView.findViewById(R.id.message);
            viewHolder.profileImageView = convertView.findViewById(R.id.imageView);
            viewHolder.rootLayout = convertView.findViewById(R.id.rootLayout);

            viewHolder.chkItem = convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.chkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    userList.get(position).setSelected(true);
                else
                    userList.get(position).setSelected(false);

            }
        });



        final User user = userList.get(position);
        if (user.selected) {
            viewHolder.chkItem.setChecked(true);
        } else
            viewHolder.chkItem.setChecked(false);

        String profileString = user.getProfileCacheUri();
        if (!TextUtils.isEmpty(profileString)) {

            Picasso.get().load(Uri.parse(profileString))
                    .error(R.drawable.bg_placeholder)
                    .into(viewHolder.profileImageView);
        } else {
            Picasso.get().load(R.drawable.bg_placeholder)
                    .into(viewHolder.profileImageView);
        }


        String phone = user.getPhoneno_1();
        if (phone != null)
            if (phone.equals(currentUser.getPhoneno_1())) {
                viewHolder.nameTextView.setText("You");
            } else
                viewHolder.nameTextView.setText(user.getName());
        viewHolder.locationTextView.setText(user.getLocation());
        return convertView;
    }


    private void showAlertDialogue(final User user) {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = context.getLayoutInflater().inflate(R.layout.select_phone_dialogue, null);
        dialogueBuilder.setView(dialogueView);

        TextView phoneno_1TextView, phoneno_2TextView;
        phoneno_1TextView = dialogueView.findViewById(R.id.phoneno_1TextView);
        phoneno_2TextView = dialogueView.findViewById(R.id.phoneno_2TextView);

        phoneno_1TextView.setText(user.getPhoneno_1());
        phoneno_2TextView.setVisibility(View.VISIBLE);
        phoneno_2TextView.setText(user.getPhoneno_2());

      /*  if (!TextUtils.isEmpty(user.getPhoneno_3())) {
            phoneno_3TextView.setVisibility(View.VISIBLE);
            phoneno_3TextView.setText(user.getPhoneno_3());
        }
*/
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
      /*  phoneno_3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFunction(user.getPhoneno_3());
            }
        });*/

        dialogueBuilder.setTitle("Call");
        final AlertDialog alertDialog = dialogueBuilder.create();
        alertDialog.show();
    }

    private void showMessageAlertDialogue(final User user) {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View dialogueView = context.getLayoutInflater().inflate(R.layout.select_phone_dialogue, null);
        dialogueBuilder.setView(dialogueView);

        TextView message_1TextView, message_2TextView, message_3TextView;
        message_1TextView = dialogueView.findViewById(R.id.phoneno_1TextView);
        message_2TextView = dialogueView.findViewById(R.id.phoneno_2TextView);

        message_1TextView.setText(user.getPhoneno_1());
        message_2TextView.setVisibility(View.VISIBLE);
        message_2TextView.setText(user.getPhoneno_2());

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

    static class ViewHolder {
        ImageView imageViewPhone, imageViewMail, imageViewMessage;
        CheckBox chkItem;
        ConstraintLayout rootLayout;
        CircleImageView profileImageView;
        TextView nameTextView, locationTextView;
    }

}