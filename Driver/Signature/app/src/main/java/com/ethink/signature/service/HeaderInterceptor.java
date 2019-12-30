package com.ethink.signature.service;

import androidx.annotation.NonNull;


import com.ethink.signature.App;
import com.ethink.signature.util.Const;
import com.ethink.signature.util.SPUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/5.
 */

public class HeaderInterceptor implements Interceptor {
private String deviceId;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public HeaderInterceptor(   ){
        deviceId= SPUtils.getSharedStringData(App.getAppContext(), Const.SERIAL_NO);
    }

    @Override
    @NonNull
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logger.info("请求头：deviceId   "+deviceId);
        return chain.proceed(request.newBuilder().addHeader("deviceId", deviceId).build());
    }
}
