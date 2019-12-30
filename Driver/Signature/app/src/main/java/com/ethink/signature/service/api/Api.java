package com.ethink.signature.service.api;


import android.content.Context;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.PhoneUtils;
import com.ethink.signature.App;
import com.ethink.signature.util.Const;
import com.ethink.signature.util.SPUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


/**
 * des:retorfit api
 */
public class Api {
    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 16000;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 16000;
    public Retrofit retrofit;
    public ApiService movieService;
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private static SparseArray<Api> sRetrofitManager = new SparseArray<>(HostType.TYPE_COUNT);
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    private static final String CACHE_CONTROL_AGE = "max-age=0";


    //构造方法私有
    private Api(Context context) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File cacheFile = new File(App.getAppContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("deviceId", PhoneUtils.getSerial())
                        .build();
                return chain.proceed(build);
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //后台证书不支持ssl2，改为http
                // .sslSocketFactory(SSLHelper.getSSLCertifcation(context))//获取SSLSocketFactory
                //.hostnameVerifier(new UnSafeHostnameVerifier())//添加hostName验证器
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
               // .addInterceptor(mRewriteCacheControlInterceptor)
               // .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(logInterceptor)
                .cache(cache)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NonNull HttpUrl httpUrl,@NonNull List<Cookie> list) {
                        cookieStore.put(httpUrl.host(), list);
                    }
                    @Override
                    @NonNull
                    public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl( SPUtils.getSharedStringData(context, Const.CONFIG_URL))
                .build();
        movieService = retrofit.create(ApiService.class);
    }


    public static ApiService getDefault(Context context) {
        Api retrofitManager = sRetrofitManager.get(0);
        if (retrofitManager == null) {
            retrofitManager = new Api(context);
            sRetrofitManager.put(0, retrofitManager);
        }
        return retrofitManager.movieService;
    }


    /**
     * 根据网络状况获取缓存的策略
     */
    @NonNull
    public static String getCacheControl() {
        return NetWorkUtils.isNetConnected(App.getAppContext()) ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }


    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtils.isNetConnected(App.getAppContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetConnected(App.getAppContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    /**
     * 验证主机地址
     */
    public class UnSafeHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
//            if (hostname.equals("sasasas")){
//                return true;
//            }else {
            return true;
            //    }
            //自行添加判断逻辑，true->Safe，false->unsafe
        }
    }
}