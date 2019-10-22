package com.ethink.vfd.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class HttpUtils {

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggerInterceptor());
        return builder.build();
    }


}
