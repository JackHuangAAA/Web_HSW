package com.ethink.lineup;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

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
