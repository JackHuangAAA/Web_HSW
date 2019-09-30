package com.ethink.vcd.server;

import com.ethink.plugin.PluginService;
import com.ethink.vcd.plugin.FingerPlugin;
import com.ethink.vcd.plugin.ServerPlugin;

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
        addPlugin(new ServerPlugin(getApplicationContext()));
        addPlugin(new FingerPlugin(getApplicationContext()));
        super.onCreate();
    }
}
