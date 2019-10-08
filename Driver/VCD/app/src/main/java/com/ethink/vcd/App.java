package com.ethink.vcd;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

/**
 * 应用初始化入口
 */
public class App extends Application {
    private static App baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        Utils.init(this);

    }


    public static Context getAppContext() {
        return baseApplication;
    }
}
