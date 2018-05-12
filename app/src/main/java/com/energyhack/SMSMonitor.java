package com.energyhack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSMonitor extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("code", "onRecieve");
        if (intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            Log.i("code", "if");
            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pduArray.length];
            for (int i = 0; i < pduArray.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
            }
            String sms = messages[0].getMessageBody();
            //check first word (3 symbols)
            //if first word - TAG give message from all sms
            //and start service
            if (sms.substring(0,3).equals("TAG")) {
                Log.i("code", "get");
                StringBuilder bodyText = new StringBuilder();
                for (int i = 0; i < messages.length; i++) {
                    bodyText.append(messages[i].getMessageBody());
                }
                String body = bodyText.toString();
                Intent mIntent = new Intent(context, SmsService.class);
                mIntent.putExtra("sms_body", body);
                context.startService(mIntent);

                abortBroadcast();
            }
        }
    }
}
