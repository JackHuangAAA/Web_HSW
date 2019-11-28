package com.ethink.cnd.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/5.
 */

public class LoggerInterceptor implements Interceptor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logger.info(request.toString());
        long t1 = System.nanoTime();
        Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
       logger.info(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, content));

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}
