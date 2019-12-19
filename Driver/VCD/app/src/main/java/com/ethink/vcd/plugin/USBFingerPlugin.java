package com.ethink.vcd.plugin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.App;
import com.ethink.vcd.Const;
import com.ethink.vcd.SPUtils;
import com.ethink.vcd.controller.FingerCommon;
import com.ethink.vcd.controller.FingerUtil;
import com.ethink.vcd.event.FingerPushMessage;
import com.ethink.vcd.service.HttpUtils;
import com.ethink.vcd.service.api.NetWorkUtils;
import com.ethink.vcd.service.api.RxManager;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * USB指纹插件
 */
public class USBFingerPlugin extends BasePlugin implements FunctionHandler, FingerPushMessage {
    private final RxManager rxManager;
    private Context context;
    private String fingerUrl;

    public USBFingerPlugin(Context context) {
        super("FINGER");
        this.context = context;
        rxManager = new RxManager();
        logger.info("--------连接指纹设备-----------");
        fingerUrl= SPUtils.getSharedStringData(App.getAppContext(), Const.FINGER_URL);

    }

    @Override
    public void onStart() {
        logger.info("FINGER-----------register");
        registerFunction("REGISTER", this);
        registerFunction("UN_REGISTER", this);
        registerFunction("VERIFY", this);
        registerFunction("UN_SEARCH", this);

        registerFunction("DEL_TEMPLATE_All", this);
        registerFunction("TEMPLATE_TOTAL", this);
        registerFunction("DEL_ONE_TEMPLATE", this);
        registerFunction("CLOSE", this);
        registerFunction("SEARCH", this);
        registerFunction("DOWNLOAD", this);
    }

    @Override
    public void onStop() {

    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        if (pluginMessage == null) return pluginMessage;
        logger.info("pluginName " + pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        switch (functionName) {
            case "OPEN":
                break;
            case "REGISTER":
                String uid = pluginMessage.getString("userId");
                logger.info("录入指纹 id：{}",uid);
                break;
            case "UN_REGISTER":
                //清除指纹
                String userId= pluginMessage.getString("userId");
                logger.info("删除指纹 id：{}",userId);
                pluginMessage = NetWorkUtils.post(fingerUrl+"/unregister?tag="+userId,null ,context, pluginMessage);
                break;
            case "VERIFY":
                String num = pluginMessage.getString("number");
                break;
            case "UN_SEARCH":
                //终止指纹录取
                break;
            case "DEL_TEMPLATE_ALL":
                break;
            case "TEMPLATE_TOTAL":
                break;
            case "CLOSE":
                break;
            case "SEARCH":
                break;
            case "DOWNLOAD":
                break;
            case "DEL_ONE_TEMPLATE":
                break;
        }
        logger.info("plug {}", JSON.toJSONString(pluginMessage));
        return pluginMessage;
    }

    @Override
    public void message(int type,String msg) {
        EventMessage eventMessage = new EventMessage("FINGER_MESSAGE");
        eventMessage.setString("msg", msg);
        eventMessage.setInt("type", type);
        logger.info("推送消息：" + JSON.toJSONString(eventMessage));
        pluginManager.post(eventMessage);
    }


    @Override
    public String upload(String path, String finger) {
        try {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), finger);
            Request request = new Request.Builder().url(String.format(fingerUrl+"%s", path)).post(body).build();
            Response response = HttpUtils.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                logger.info("指纹上传返回：" + result);
                JSONObject jsonObject=JSON.parseObject(result);
                if(jsonObject.getIntValue("code")==0){
                  //  message(2,jsonObject.getString("data"));
                    return jsonObject.getString("data");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
