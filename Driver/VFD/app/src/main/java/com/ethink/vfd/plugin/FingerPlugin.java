package com.ethink.vfd.plugin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vfd.App;
import com.ethink.vfd.Const;
import com.ethink.vfd.SPUtils;
import com.ethink.vfd.controller.FingerCommon;
import com.ethink.vfd.event.FingerPushMessage;
import com.ethink.vfd.controller.FingerUtil;
import com.ethink.vfd.service.HttpUtils;
import com.ethink.vfd.service.api.NetWorkUtils;
import com.ethink.vfd.service.api.RxManager;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 指纹插件
 */
public class FingerPlugin extends BasePlugin implements FunctionHandler, FingerPushMessage {
    private final RxManager rxManager;
    private Context context;
    private FingerUtil fingerUtil;
    private String fingerUrl;

    public FingerPlugin(Context context) {
        super("FINGER");
        this.context = context;
        rxManager = new RxManager();
        logger.info("--------连接指纹设备-----------");
        fingerUrl= SPUtils.getSharedStringData(App.getAppContext(), Const.FINGER_URL);
        fingerUtil = new FingerUtil(this, new FingerCommon(context, "/dev/ttyS1", 115200));
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
        fingerUtil.close();
    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        if (pluginMessage == null) return pluginMessage;
        logger.info("pluginName " + pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        switch (functionName) {
            case "OPEN":
                //   fingerUtil = new FingerUtil(this, new FingerCommon(context, "/dev/ttyS1", 115200));
                break;
            case "REGISTER":
                String uid = pluginMessage.getString("userId");
                logger.info("录入指纹 id：{}",uid);
                fingerUtil.remoteRegister(uid);
                break;
            case "UN_REGISTER":
                //清除指纹
                String userId= pluginMessage.getString("userId");
                logger.info("删除指纹 id：{}",userId);
                pluginMessage = NetWorkUtils.post(fingerUrl+"/unregister?tag="+userId,null ,context, pluginMessage);
                break;
            case "VERIFY":
                String num = pluginMessage.getString("number");
                if (StringUtils.isEmpty(num)) {
                    logger.info("请输入指纹序号！");
                } else {
                    try {
                        int id = Integer.parseInt(num);
                        fingerUtil.verify(id);
                    } catch (NumberFormatException e) {
                        logger.info("序号格式不正确", e);
                    }
                }
                break;
            case "UN_SEARCH":
                //终止指纹录取
                fingerUtil.cancel();
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
                fingerUtil.remoteVerify();
                break;
            case "DOWNLOAD":
                fingerUtil.download();
                break;
            case "DEL_ONE_TEMPLATE":
                fingerUtil.delOne();
                break;
        }
        logger.info("plug {}", JSON.toJSONString(pluginMessage));
        pluginMessage.changeToResponse();
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
