package com.ethink.vcd.service.api;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.Const;
import com.ethink.vcd.service.HttpUtils;
import com.yanzhenjie.andserver.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * des:网络管理工具
 */
public class NetWorkUtils {
    private static final Logger logger = LoggerFactory.getLogger(NetWorkUtils.class);
    /**
     * 检查网络是否可用
     *
     * @param paramContext
     * @return
     */
    public static boolean isNetConnected(Context paramContext) {
        boolean i = false;
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable()))
            return true;
        return false;
    }
    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测3G是否连接
     */
    public static boolean is3gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断网址是否有效
     */
    public static boolean isLinkAvailable(String link) {
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    public static PluginMessage get(final String path, Context context, PluginMessage pluginMessage) {
        try {
            logger.info("请求地址："+path);
            pluginMessage.changeToResponse();
            Request request = new Request.Builder().url(Const.getUrl(path)).build();
            Response response = HttpUtils.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String body = response.body().string();
                if (!StringUtils.isEmpty(body)) {
                    pluginMessage.setString("rsp", body);
                }
            } else {
                logger.info("OnFunction  网络请求出错" + response.message());
            }
            return pluginMessage;
        } catch (Exception e) {
            logger.error("OnFunction  网络请求出错" + e.toString());
        }
        return null;
    }

    /**
     * post请求
     */
    public static PluginMessage post(String path, String data, Context context, PluginMessage pluginMessage) {
        try {
            logger.info("  网络请求加载的地址" + path);
            pluginMessage.changeToResponse();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data.toString());
            Request request = new Request.Builder().post(requestBody).url(Const.getUrl(path)).build();
            Response response = HttpUtils.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String body = response.body().string();
                if (!StringUtils.isEmpty(body)) {
                    pluginMessage.setString("rsp", body);
                }
            } else {
                logger.info("OnFunction  网络请求出错" + response.message());
            }
            return pluginMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 返回当前程序版本名
     */
    public static String getVersionCode(Context context) {
        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode+"";
    }
}
