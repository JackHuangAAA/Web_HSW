package com.ethink.snd.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ethink.snd.App;
import com.ethink.snd.Const;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
private static String TAG="AddCookiesInterceptor";
    @Override
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        String cookie = getCookie(request.url().toString(), request.url().host());
        if (!TextUtils.isEmpty(cookie)) {
            builder.addHeader("Cookie", cookie);
            Log.i(TAG, "interceptor addHeader Cookie: "+cookie);
        }
        return chain.proceed(builder.build());
    }

    private String getCookie(String url, String domain) {
        SharedPreferences sp = App.getAppContext().getSharedPreferences(Const.COOKIE_PREF,  Context.MODE_PRIVATE);
        String cookie = sp.getString(domain, "");
        Log.i(TAG, "interceptor getCookie: "+cookie);
        if (!TextUtils.isEmpty(domain) && sp.contains(domain) && !
                TextUtils.isEmpty(sp.getString(domain, ""))) {
            return sp.getString(domain, "");
        }
        return null;
    }

}