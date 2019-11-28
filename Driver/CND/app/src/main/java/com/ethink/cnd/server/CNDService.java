package com.ethink.cnd.server;

import com.ethink.cnd.plugin.PrintPlugin;
import com.ethink.plugin.PluginService;
import com.ethink.cnd.plugin.FingerPlugin;
import com.ethink.cnd.plugin.ScannerPlugin;
import com.ethink.cnd.plugin.ServerPlugin;
import com.ethink.cnd.plugin.SocketPlugin;

/**
 * ADM链接ADF服务
 */

public class CNDService extends PluginService {
    public CNDService() {
        super("CND", true);
    }

    @Override
    public void onCreate() {
        //先添加插件再调用父类的方法
      // addPlugin(new ArkPlugin(getApplicationContext()));
        //  addPlugin(new FingerPlugin(getApplicationContext()));
        addPlugin(new ServerPlugin(getApplicationContext()));
        addPlugin(new SocketPlugin(getApplicationContext()));
        addPlugin(new ScannerPlugin(getApplicationContext()));
         addPlugin(new PrintPlugin(getApplicationContext()));
        super.onCreate();
    }
}
