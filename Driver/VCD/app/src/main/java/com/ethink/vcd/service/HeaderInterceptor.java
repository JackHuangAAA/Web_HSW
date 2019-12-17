package com.ethink.vcd.service;

import androidx.annotation.NonNull;

import com.ethink.vcd.App;
import com.ethink.vcd.Const;
import com.ethink.vcd.SPUtils;

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
private String alias;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public HeaderInterceptor(   ){
        deviceId= SPUtils.getSharedStringData(App.getAppContext(), Const.SERIAL_NO);
        alias= SPUtils.getSharedStringData(App.getAppContext(), Const.ALIAS);
    }

    @Override
    @NonNull
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logger.info("请求头：deviceId   "+deviceId);
        return chain.proceed(request.newBuilder().addHeader("alias",alias).addHeader("deviceId", deviceId).build());
    }
}
