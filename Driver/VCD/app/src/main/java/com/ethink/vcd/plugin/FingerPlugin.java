package com.ethink.vcd.plugin;

import android.content.Context;
import android.widget.Toast;

import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.finger.FingerCommon;
import com.ethink.vcd.finger.FingerUtil;
import com.ethink.vcd.finger.IUsbConnState;
import com.ethink.vcd.service.api.RxManager;
import com.ethink.vcd.utils.ResponseUtil;

/**
 * 指纹插件
 * */
public class FingerPlugin extends BasePlugin implements FunctionHandler  , IUsbConnState {
    private final RxManager rxManager;
    private Context context;
    private FingerUtil fingerUtil;
    public FingerPlugin(Context context) {
        super("FINGER");
        this.context = context;
        rxManager = new RxManager();
        FingerCommon fingerCommon = new FingerCommon(context, this);
        fingerUtil = new FingerUtil(context, fingerCommon);
    }

    @Override
    public void onStart()     {
        logger.info("FINGER-----------register");
        registerFunction("open", this);
        registerFunction("register", this);
        registerFunction("verify", this);
        registerFunction("delTemplateAll", this);
        registerFunction("templateTotal", this);
        registerFunction("close", this);
        registerFunction("search", this);
    }
    @Override
    public void onStop()     {
        fingerUtil.close();
    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        if(pluginMessage==null)return pluginMessage;
        logger.info("pluginName "+pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        pluginMessage.changeToResponse();
        switch (functionName){
            case "open":
                fingerUtil.usbOpen(pluginMessage);
                break;
            case "register":
                fingerUtil.register(pluginMessage);
                break;
            case "verify":
                fingerUtil.verify(pluginMessage);
                break;
            case "delTemplateAll":
                fingerUtil.delTemplateAll(pluginMessage);
                break;
            case "templateTotal":
                fingerUtil.templateTotal(pluginMessage);
                break;
            case "close":
                fingerUtil.close();
                ResponseUtil.success(pluginMessage,"ok",null);
                break;
            case "search":
                fingerUtil.search();
                break;
        }
        return pluginMessage;
    }

    @Override
    public void onUsbConnected() {
        Toast.makeText(context, "指纹设备已连接", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUsbPermissionDenied() {
        Toast.makeText(context, "指纹设备无权限", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeviceNotFound() {
        Toast.makeText(context, "没有找到模块", Toast.LENGTH_LONG).show();
    }
}
