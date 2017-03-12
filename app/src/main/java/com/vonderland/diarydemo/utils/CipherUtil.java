package com.vonderland.diarydemo.utils;

import java.security.MessageDigest;

/**
 * Created by Vonderland on 2017/3/11.
 */

public class CipherUtil {

    public static String encodeData(String data) {
        String result;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] mdMsg = md.digest(data.getBytes());
            byte[] encodeContent = android.util.Base64.encode(mdMsg, android.util.Base64.DEFAULT);
            result = new String(encodeContent);

        } catch (Exception e) {
            L.e("CipherUtil exception", e.toString());
            result = "";
        }
        return result;
    }

    public static String encodeDataBase64(String data) {
        return android.util.Base64.encodeToString(data.getBytes(), android.util.Base64.DEFAULT);
    }
}
