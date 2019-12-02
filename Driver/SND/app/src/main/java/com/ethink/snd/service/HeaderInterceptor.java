package com.ethink.snd.service;

import androidx.annotation.NonNull;

import com.ethink.snd.App;
import com.ethink.snd.Const;
import com.ethink.snd.SPUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/5.
 */

public class HeaderInterceptor implements Interceptor {
private String deviceId;
    public HeaderInterceptor(   ){
        deviceId= SPUtils.getSharedStringData(App.getAppContext(), Const.SERIAL_NO);

    }

    @Override
    @NonNull
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request.newBuilder().addHeader("deviceid", deviceId).build());
    }
}
