package com.ethink.signature.server;

import com.ethink.plugin.PluginService;
import com.ethink.signature.plugin.IDCardPlugin;
import com.ethink.signature.plugin.ServerPlugin;
import com.ethink.signature.plugin.USBFingerPlugin;

/**
 * ADM链接ADF服务
 * 签核板
 */

public class SignatureService extends PluginService {
    public SignatureService() {
        super("Signature", true);
    }

    @Override
    public void onCreate() {
        //先添加插件再调用父类的方法
       addPlugin(new ServerPlugin(getApplicationContext()));
        addPlugin(new USBFingerPlugin(getApplicationContext()));
     //   addPlugin(new IDCardPlugin(getApplicationContext()));
        super.onCreate();
    }
}