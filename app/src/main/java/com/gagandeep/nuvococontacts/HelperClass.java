package com.gagandeep.nuvococontacts;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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


}
