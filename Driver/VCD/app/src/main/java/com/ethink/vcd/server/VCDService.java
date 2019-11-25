package com.ethink.vcd.server;

import com.ethink.plugin.PluginService;
import com.ethink.vcd.plugin.FingerPlugin;
import com.ethink.vcd.plugin.ArkPlugin;
import com.ethink.vcd.plugin.ScannerPlugin;
import com.ethink.vcd.plugin.ServerPlugin;
import com.ethink.vcd.plugin.SocketPlugin;

/**
 * ADM链接ADF服务
 */

public class VCDService extends PluginService {
    public VCDService() {
        super("VCD", true);
    }

    @Override
    public void onCreate() {
        //先添加插件再调用父类的方法
        addPlugin(new ArkPlugin(getApplicationContext()));
        addPlugin(new ServerPlugin(getApplicationContext()));
        addPlugin(new SocketPlugin(getApplicationContext()));
        addPlugin(new FingerPlugin(getApplicationContext()));
        addPlugin(new ScannerPlugin(getApplicationContext()));
        super.onCreate();
    }
}