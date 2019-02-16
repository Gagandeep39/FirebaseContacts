package com.gagandeep.nuvococontacts.Helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Toast;

public class HelperClass {

//Class to keep some commonly used functions

    //    call Intent
    public static void initiateCallTo(String number, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+91" + number));
        context.startActivity(intent);
    }

    //Message Intent
    public static void sendMessageTo(String number, Context context) {
        Uri sms_uri = Uri.parse("smsto:+91" + number);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        context.startActivity(sms_intent);
    }

    //Mail Intent
    public static void sendMailTo(String emailId, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailId, null));
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    //WhatsApp Intent
    public static void sendWhatsAppMessageTo(String number, Context context) {
        String formattedNumber = "91" + number;
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "");
            sendIntent.putExtra("jid", formattedNumber + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //Funtion to set Max input text length of Text box
    public static void setMaxLength(EditText view, int length) {
        InputFilter[] curFilters;
        InputFilter.LengthFilter lengthFilter;
        int idx;

        lengthFilter = new InputFilter.LengthFilter(length);

        curFilters = view.getFilters();
        if (curFilters != null) {
            for (idx = 0; idx < curFilters.length; idx++) {
                if (curFilters[idx] instanceof InputFilter.LengthFilter) {
                    curFilters[idx] = lengthFilter;
                    return;
                }
            }
            // since the length filter was not part of the list, but
            // there are filters, then add the length filter
            InputFilter[] newFilters = new InputFilter[curFilters.length + 1];
            System.arraycopy(curFilters, 0, newFilters, 0, curFilters.length);
            newFilters[curFilters.length] = lengthFilter;
            view.setFilters(newFilters);
        } else {
            view.setFilters(new InputFilter[]{lengthFilter});
        }
    }

    public static boolean validatePhoneNumber(String number, TextInputEditText editText) {
        if (number.length() < 10) {
            editText.setError("Enter a valid number");
            return false;
        } else return true;
    }

    //Removes useless , [ ]
    public static String[] stringToArray(String s) {
        String newString = s.substring(1, s.length() - 1);
        String array[] = newString.split(",");
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replaceAll(" ", "");
        }

        return array;

    }
}
