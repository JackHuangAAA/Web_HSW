package com.ethink.lineup.server;

import com.ethink.lineup.plugin.ScannerPlugin;
import com.ethink.lineup.plugin.ServerPlugin;
import com.ethink.lineup.plugin.USBPrintPlugin;
import com.ethink.lineup.plugin.USBScannerPlugin;
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
      // addPlugin(new USBScannerPlugin(getApplicationContext()));
       addPlugin(new ScannerPlugin(getApplicationContext()));
       addPlugin(new USBPrintPlugin(getApplicationContext()));
        super.onCreate();
    }

//usb device vid=5455  pid=5455 name=null
//usb device vid=8746  pid=1 name=ILITEK-TP
//usb device vid=3034  pid=377 name=802.11n NIC
}
