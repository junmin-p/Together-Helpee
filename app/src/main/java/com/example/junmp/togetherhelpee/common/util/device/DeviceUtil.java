package com.example.junmp.togetherhelpee.common.util.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceUtil {

    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 225;

    public static String getPhoneNumber(Activity context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);


        try {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephony.getLine1Number() != null) {
                    return telephony.getLine1Number();
                } else {
                    if (telephony.getSimSerialNumber() != null) {
                        return telephony.getSimSerialNumber();
                    }
                }
            } else {
                Log.d("phony", "Current app does not have READ_PHONE_STATE permission");
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_PHONE_STATE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("phoneNumber is null");
    }
}
