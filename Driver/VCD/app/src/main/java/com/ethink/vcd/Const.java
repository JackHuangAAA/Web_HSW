package com.ethink.vcd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Const {
    public static final String CONFIG_URL = "url";
    public static final String SOCKET_IO_URL = "socket_io";
    public static final String SERIAL_NO = "serial_no";
    public static final String COOKIE_PREF = "cookies_prefs";
    public static String getUrl(String path){
        String base=SPUtils.getSharedStringData(App.getAppContext(),CONFIG_URL);
        return base+"/"+path;
    }


    public static String bitmapToString(Bitmap bitmap){
        //将Bitmap转换成字符串
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    public Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
