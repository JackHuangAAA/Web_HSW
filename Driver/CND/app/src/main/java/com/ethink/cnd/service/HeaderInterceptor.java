package com.ethink.cnd.service;

import androidx.annotation.NonNull;

import com.ethink.cnd.App;
import com.ethink.cnd.Const;
import com.ethink.cnd.SPUtils;

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
        return chain.proceed(request.newBuilder().addHeader("deviceId", deviceId).build());
    }
}
