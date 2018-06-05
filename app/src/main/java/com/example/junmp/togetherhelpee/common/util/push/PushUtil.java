package com.example.junmp.togetherhelpee.common.util.push;

import com.google.firebase.iid.FirebaseInstanceId;

public class PushUtil {
    public static String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }
}
