package com.ethink.vfd.server;

import com.ethink.plugin.PluginService;
import com.ethink.vfd.plugin.ArkPlugin;
import com.ethink.vfd.plugin.ScannerPlugin;
import com.ethink.vfd.plugin.ServerPlugin;
import com.ethink.vfd.plugin.SocketPlugin;
import com.ethink.vfd.plugin.USBFingerPlugin;

/**
 * ADM链接ADF服务
 */

public class VFDService extends PluginService {
    public VFDService() {
        super("VFD", true);
    }

    @Override
    public void onCreate() {
        //先添加插件再调用父类的方法
      addPlugin(new ArkPlugin(getApplicationContext()));
        addPlugin(new ServerPlugin(getApplicationContext()));
        addPlugin(new SocketPlugin(getApplicationContext()));
          addPlugin(new USBFingerPlugin(getApplicationContext()));
        addPlugin(new ScannerPlugin(getApplicationContext()));
       // addPlugin(new PrintPlugin(getApplicationContext()));
        super.onCreate();
    }
}
