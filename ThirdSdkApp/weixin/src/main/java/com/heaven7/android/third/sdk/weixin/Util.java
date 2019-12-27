package com.heaven7.android.third.sdk.weixin;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*public*/ class Util {

    public static String urlEncode(String src){
        try {
            return URLEncoder.encode(src, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] bitampToByteArray(Bitmap bitmap) {
        byte[] array = null;
        ByteArrayOutputStream os = null;
        try {
            if (null != bitmap) {
                os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
                array = os.toByteArray();
            }
        } finally {
        }
        return array;
    }
}
