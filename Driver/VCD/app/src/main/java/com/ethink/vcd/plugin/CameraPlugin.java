package com.ethink.vcd.plugin;

import android.content.Context;
import android.content.Intent;

import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.ui.CameraActivity;
/****
 * 人脸识别模块
 * **/
public class CameraPlugin extends BasePlugin implements FunctionHandler {
    private Context context;

    public CameraPlugin(Context context) {
        super("CAMERA");
        this.context = context;
    }

    @Override
    public void onStart() throws Throwable {
        registerFunction("open", this);
    }

    @Override
    public void onStop() throws Throwable {

    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        if (pluginMessage == null) return pluginMessage;
        logger.info("pluginName " + pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        pluginMessage.changeToResponse();
        switch (functionName) {
            case "open":
                context.startActivity(new Intent(context, CameraActivity.class));
                break;
        }
        return pluginMessage;
    }


}
