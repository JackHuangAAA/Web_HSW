package com.ethink.snd.server;

import com.ethink.plugin.PluginService;
import com.ethink.snd.plugin.ScannerPlugin;
import com.ethink.snd.plugin.ServerPlugin;

/**
 * 挂叫号一体机
 * ADM链接ADF服务
 */

public class SNDService extends PluginService {
    public SNDService() {
        super("SND", true);
    }

    @Override
    public void onCreate() {
        //先添加插件再调用父类的方法
      // addPlugin(new ArkPlugin(getApplicationContext()));
        //  addPlugin(new FingerPlugin(getApplicationContext()));
        addPlugin(new ServerPlugin(getApplicationContext()));
       // addPlugin(new SocketPlugin(getApplicationContext()));
        addPlugin(new ScannerPlugin(getApplicationContext()));
      //   addPlugin(new PrintPlugin(getApplicationContext()));
        super.onCreate();
    }
}
