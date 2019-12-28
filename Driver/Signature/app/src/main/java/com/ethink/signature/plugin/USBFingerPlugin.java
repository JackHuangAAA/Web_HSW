package com.ethink.signature.plugin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.signature.App;
import com.ethink.signature.controller.USBFingerController;
import com.ethink.signature.event.FingerPushMessage;
import com.ethink.signature.service.HttpUtils;
import com.ethink.signature.service.api.NetWorkUtils;
import com.ethink.signature.util.Const;
import com.ethink.signature.util.SPUtils;
import com.yanzhenjie.andserver.util.StringUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * USB指纹插件
 */
public class USBFingerPlugin extends BasePlugin implements FunctionHandler, FingerPushMessage {
    private Context context;
    private String fingerUrl;
    private USBFingerController controller;

    public USBFingerPlugin(Context context) {
        super("FINGER");
        this.context = context;
        logger.info("--------连接指纹设备-----------");
        fingerUrl= SPUtils.getSharedStringData(App.getAppContext(), Const.FINGER_URL);
        controller=new USBFingerController(context,this);
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
       Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(controller!=null){
                    controller.remoteRegister("123");
                }
            }
        },10000,3000);
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
                controller.open();
                break;
            case "REGISTER":
                String uid = pluginMessage.getString("userId");
                logger.info("录入指纹 id：{}",uid);
                controller.remoteRegister(uid);
                pluginMessage.changeToResponse();
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
                controller.cancel();
                pluginMessage.changeToResponse();
                break;
            case "DEL_TEMPLATE_ALL":
                break;
            case "TEMPLATE_TOTAL":
                break;
            case "CLOSE":
                controller.close();
                pluginMessage.changeToResponse();
                break;
            case "SEARCH":
                controller.remoteVerify();
                pluginMessage.changeToResponse();
                break;
            case "DOWNLOAD":
                break;
            case "DEL_ONE_TEMPLATE":
                break;
        }
        return pluginMessage;
    }

    @Override
    public void message(int type,String msg) {
//        EventMessage eventMessage = new EventMessage("FINGER_MESSAGE");
//        eventMessage.setString("msg", msg);
//        eventMessage.setInt("type", type);
//        logger.info("推送消息：" + JSON.toJSONString(eventMessage));
//        pluginManager.post(eventMessage);
    }


    @Override
    public String upload(String path, String finger) {
logger.info("finger length {}",finger.length());
        EventMessage eventMessage = new EventMessage("FINGER_MESSAGE");
        eventMessage.setString("msg", finger);
        eventMessage.setInt("type",2 );
        logger.info("推送消息：" + JSON.toJSONString(eventMessage));
        pluginManager.post(eventMessage);

/*       try {
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
        }*/
        return "成功了";


    }


}
