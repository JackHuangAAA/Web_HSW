package com.ethink.lineup.server;

import com.ethink.lineup.plugin.ScannerPlugin;
import com.ethink.lineup.plugin.ServerPlugin;
import com.ethink.plugin.PluginService;

/**
 *排队取号机
 * ADM链接ADF服务
 */

public class LineUpService extends PluginService {
    public LineUpService() {
        super("LineUp", true);
    }


    @Override
    public void onCreate() {
        //先添加插件再调用父类的方法
      // addPlugin(new ArkPlugin(getApplicationContext()));
        //  addPlugin(new FingerPlugin(getApplicationContext()));
        addPlugin(new ServerPlugin(getApplicationContext()));
       // addPlugin(new PrintPlugin(getApplicationContext()));
        addPlugin(new ScannerPlugin(getApplicationContext()));
      //   addPlugin(new PrintPlugin(getApplicationContext()));
        super.onCreate();
    }


}
