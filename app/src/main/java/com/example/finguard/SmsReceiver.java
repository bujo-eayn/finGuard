package com.example.finguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.example.finguard.AESUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = sms.getOriginatingAddress();
                    String messageBody = sms.getMessageBody();

                    // Log the message for debugging (remove in production)
                    Log.d("SmsReceiver", "Received SMS from " + sender + ": " + messageBody);

                    if (sender != null && sender.contains("MPESA")) {
                        String obfuscatedMessage = obfuscateSmsContent(messageBody);
                        Log.d("SmsReceiver", "Obfuscated SMS: " + obfuscatedMessage);

                        // Encrypt using AESUtils
                        String key = "1234567890123456"; // Replace with secure key
                        String encryptedMessage = AESUtils.INSTANCE.encrypt(key, obfuscatedMessage);

                        saveToLocalStorage(context, encryptedMessage);
                        Toast.makeText(context, "New M-Pesa SMS intercepted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String obfuscateSmsContent(String message) {
        // Obfuscate phone numbers
        message = message.replaceAll("\\b(\\d{4})(\\d{4})\\b", "$1****");

        // Obfuscate monetary values
        message = message.replaceAll("\\bKsh\\s?\\d+[.,]?\\d*\\b", "Ksh [REDACTED]");

        // Obfuscate full names (e.g., "John Doe Jane" -> "J* D* J*")
        message = obfuscateFullNames(message);

        // Obfuscate account numbers (e.g., "0100006016922" -> "0100******")
        message = message.replaceAll("\\b(\\d{4})(\\d{4,})\\b", "$1****");

        return message;
    }

    private String obfuscateFullNames(String message) {
        Pattern namePattern = Pattern.compile("\\b([A-Z][a-z]+(?:\\s[A-Z][a-z]+)+)\\b");
        Matcher matcher = namePattern.matcher(message);
        StringBuffer obfuscated = new StringBuffer();

        while (matcher.find()) {
            String fullName = matcher.group(1);
            String obfuscatedName = obfuscateName(fullName);
            matcher.appendReplacement(obfuscated, obfuscatedName);
        }
        matcher.appendTail(obfuscated);
        return obfuscated.toString();
    }

    private String obfuscateName(String fullName) {
        String[] parts = fullName.split(" ");
        StringBuilder obfuscatedName = new StringBuilder();
        for (String part : parts) {
            obfuscatedName.append(part.charAt(0)).append("* ");
        }
        return obfuscatedName.toString().trim();
    }

    private void saveToLocalStorage(Context context, String encryptedMessage) {
        context.getSharedPreferences("FinGuardMessages", Context.MODE_PRIVATE)
                .edit()
                .putString(String.valueOf(System.currentTimeMillis()), encryptedMessage)
                .apply();
    }
}
