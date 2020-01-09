package com.ethink.signature.plugin;

import android.content.Context;

import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.signature.controller.IDCardController;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 身份证读取
 */
public class IDCardPlugin extends BasePlugin implements FunctionHandler {
    private IDCardController controller;

    public IDCardPlugin(Context context) {
        super("IDCARD");
        logger.info("--------连接身份证读取设备-----------");
        controller=new IDCardController(context);
    }

    @Override
    public void onStart() {
        logger.info("IDCARD-----------register");
    registerFunction("READ_IDCARD",this);
       Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(controller!=null){
                controller.startRead();
                }
            }
        },5000,10000);
    }

    @Override
    public void onStop() {
     controller.destroy();
    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        if (pluginMessage == null) return pluginMessage;
        logger.info("pluginName " + pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        switch (functionName) {
            case "READ_IDCARD":
                controller.startRead();
                break;
        }
        return pluginMessage;
    }





}
