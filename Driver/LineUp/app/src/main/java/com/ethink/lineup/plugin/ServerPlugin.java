package com.ethink.lineup.plugin;

import android.content.Context;

import com.ethink.lineup.App;
import com.ethink.lineup.Const;
import com.ethink.lineup.SPUtils;
import com.ethink.lineup.service.api.NetWorkUtils;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.PluginMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 接口服务插件
 */
public class ServerPlugin extends BasePlugin implements FunctionHandler, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ServerPlugin.class);
    private Context context;
    public ServerPlugin(Context context) {
        super("SERVER");
        this.context = context;
    }


    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        logger.info("OnFunction {} ",pluginMessage == null);
        logger.info("pluginName "+pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        switch (functionName) {
            case "Get": {
                String url = pluginMessage.getString("path");
                //调用请求并返回response
                pluginMessage = NetWorkUtils.get(url, context, pluginMessage);
                break;
            }
            case "Post": {
                String path = pluginMessage.getString("path");
                String data = pluginMessage.getString("data");
//                //调用请求并返回response
                pluginMessage = NetWorkUtils.post(path, data, context, pluginMessage);
                break;
            }
            case "GetDeviceId": {
                //ResponseUtil.success(pluginMessage,"成功",ResponseUtil.data("res", PhoneUtils.getSerial()));
                pluginMessage.changeToResponse();
                String deviceId= SPUtils.getSharedStringData(App.getAppContext(), Const.SERIAL_NO);
                pluginMessage.set("deviceId", deviceId);
                break;
            }
        }
        return pluginMessage;
    }


    /**
     * 接口服务连接断开轮询
     */
    @Override
    public void run() {

    }

    @Override
    public void onStart()     {
        logger.info("FunctionTest");
        registerFunction("Get", this);
        registerFunction("Post", this);
        registerFunction("GetDeviceId",this);
        declareEvent("Test");

        //服务链接检测线程
      //  connectThread = new Thread(this);
       // connectThread.start();


    }

    @Override
    public void onStop()     {
    }
}


