package com.ethink.vfd.plugin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vfd.event.FingerPushMessage;
import com.ethink.vfd.controller.FingerUtil;
import com.ethink.vfd.service.api.RxManager;


/**
 * 指纹插件
 */
public class FingerPlugin extends BasePlugin implements FunctionHandler, FingerPushMessage {
    private final RxManager rxManager;
    private Context context;
    private FingerUtil fingerUtil;

    public FingerPlugin(Context context) {
        super("FINGER");
        this.context = context;
        rxManager = new RxManager();
        logger.info("--------连接指纹设备-----------");
        //   fingerUtil = new FingerUtil(this, new FingerCommon(context, "/dev/ttyS1", 115200));
    }

    @Override
    public void onStart() {
        logger.info("FINGER-----------register");
        registerFunction("REGISTER", this);
        registerFunction("VERIFY", this);
        registerFunction("DEL_TEMPLATE_All", this);
        registerFunction("TEMPLATE_TOTAL", this);
        registerFunction("DEL_ONE_TEMPLATE", this);
        registerFunction("CLOSE", this);
        registerFunction("SEARCH", this);
        registerFunction("DOWNLOAD", this);
    }

    @Override
    public void onStop() {
        fingerUtil.close();
    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        if (pluginMessage == null) return pluginMessage;
        logger.info("pluginName " + pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        pluginMessage.changeToResponse();
        switch (functionName) {
            case "REGISTER":
                fingerUtil.register();
                break;
            case "VERIFY":
                String num=pluginMessage.getString("number");
                if(StringUtils.isEmpty(num)){
                    logger.info("请输入指纹序号！");
                }else{
                    try {
                        int id=Integer.parseInt(num);
                        fingerUtil.verify(id);
                    }catch (NumberFormatException e){
                        logger.info("序号格式不正确",e);
                    }
                }
                break;
            case "DEL_TEMPLATE_ALL":
                fingerUtil.delTemplateAll();
                break;
            case "TEMPLATE_TOTAL":
                fingerUtil.templateTotal();
                break;
            case "CLOSE":
                fingerUtil.close();
                break;
            case "SEARCH":
                fingerUtil.search();
                break;
            case "DOWNLOAD":
                fingerUtil.download();
                break;
            case "DEL_ONE_TEMPLATE":
                fingerUtil.delOne();
                break;
        }
        logger.info("plug {}", JSON.toJSONString(pluginMessage));
        return pluginMessage;
    }

    @Override
    public void message(String msg) {
        EventMessage eventMessage = new EventMessage("FINGER_MESSAGE");
        eventMessage.setString("msg", msg);
        eventMessage.setString("type", "1");
        logger.info("推送消息：" + JSON.toJSONString(eventMessage));
        pluginManager.post(eventMessage);
    }

    @Override
    public void pushData(String msg, String data, String number) {
        EventMessage eventMessage = new EventMessage("FINGER_MESSAGE");
        eventMessage.setString("msg", msg);
        eventMessage.setString("type", "2");
        eventMessage.setString("data", data);
        eventMessage.setString("number", number);
        logger.info("pushData 推送消息：" + JSON.toJSONString(eventMessage));
        pluginManager.post(eventMessage);
    }

    @Override
    public void upload(String path, String finger) {

    }


}
