package com.ethink.cnd.service.api;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * des:ApiService
 * Created by sjy
 * on 2019.03.11
 */
public interface ApiService {
    //登录
    @POST
    Observable<String> post(
            @Header("content-type") String cacheControl,
            @Url String url,
            @Body RequestBody requestBody);

    //查询钞袋信
    @GET
    Observable<String> get(
            @Header("content-type") String cacheControl,
            @Url String url);

    //更新APK
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync();

}
