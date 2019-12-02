package com.ethink.snd.plugin;

import android.content.Context;

import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.snd.App;
import com.ethink.snd.Const;
import com.ethink.snd.SPUtils;
import com.ethink.snd.service.HttpUtils;
import com.ethink.snd.service.api.NetWorkUtils;
import com.ethink.snd.service.api.RxManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * 接口服务插件
 */
public class ServerPlugin extends BasePlugin implements FunctionHandler, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ServerPlugin.class);
    private final RxManager rxManager;
    private Thread connectThread;
    private Context context;
    public ServerPlugin(Context context) {
        super("SERVER");
        this.context = context;
        rxManager = new RxManager();
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
        while (!Thread.interrupted()) {
                logger.info("发送请求");
                try {
                    Thread.sleep(10000*60);
                Request request = new Request.Builder().url(Const.getUrl("/vcf/device")).build();
                Response response = HttpUtils.getOkHttpClient().newCall(request).execute();
                String msg=response.message();
                if (response.isSuccessful()) {
                    EventMessage eventMessage = new EventMessage("SERVER_CONNECT");
                    eventMessage.setString("ip", "192.168.0.129");
                    eventMessage.setString("port", "8080");
                    eventMessage.setString("type", "1");
                    pluginManager.post(eventMessage);
                }
                else {
                    EventMessage eventMessage = new EventMessage("SERVER_DISCONNECT");
                    eventMessage.setString("ip", "192.168.0.129");
                    eventMessage.setString("port", "8080");
                    eventMessage.setString("type", "1");
                    eventMessage.setString("error", msg);
                    pluginManager.post(eventMessage);
                  //  throw new Exception("网络请求失败 HTTP ERROR:" + msg);
                }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                    EventMessage eventMessage = new EventMessage("SERVER_DISCONNECT");
                eventMessage.setString("ip", "192.168.0.129");
                eventMessage.setString("port", "8080");
                eventMessage.setString("type", "1");
                eventMessage.setString("error", e.toString());
                if (pluginManager != null) {
                    pluginManager.post(eventMessage);
                }

                }

//                Api.getDefault(context).get("application/json", "device").compose(RxSchedulers.<String>io_main()).subscribe(new RxSubscriber<String>(context) {
//                    @Override
//                    protected void _onNext(String uid) {
//                        logger.info("_onNext");
//                        EventMessage eventMessage = new EventMessage("SERVER_CONNECT");
//                        eventMessage.setString("ip", "192.168.0.129");
//                        eventMessage.setString("port", "8080");
//                        eventMessage.setString("type", "1");
//                        pluginManager.post(eventMessage);
//                    }
//
//                    @Override
//                    protected void _onError(String message) {
//                        logger.info("OnFunction  网络请求出错" + message);
//                        EventMessage eventMessage = new EventMessage("SERVER_DISCONNECT");
//                        eventMessage.setString("ip", "192.168.0.129");
//                        eventMessage.setString("port", "8080");
//                        eventMessage.setString("type", "1");
//                        eventMessage.setString("error", message);
//                        pluginManager.post(eventMessage);
//
//                    }
//                });
//            }catch (InterruptedException e) {
//                break;
//            } catch (Exception e) {
//                logger.info("OnFunction  网络请求出错" + e.toString());
//                EventMessage eventMessage = new EventMessage("SERVER_DISCONNECT");
//                eventMessage.setString("ip", "192.168.0.129");
//                eventMessage.setString("port", "8080");
//                eventMessage.setString("type", "1");
//                eventMessage.setString("error", e.toString());
//                if (pluginManager != null) {
//                    pluginManager.post(eventMessage);
//                }
//            }
        }
    }

    @Override
    public void onStart()     {
        logger.info("FunctionTest");
        registerFunction("Get", this);
        registerFunction("Post", this);
        registerFunction("GetDeviceId",this);
        declareEvent("Test");

        //服务链接检测线程
        connectThread = new Thread(this);
        connectThread.start();

        //添加药品事件发送
        rxManager.on("MedicineSubscriber", new Action1<String>() {
            @Override
            public void call(String rsp) {
                logger.info("MedicineSubscriberOnFunction  rxManager rsp结果" + rsp);
                EventMessage eventMessage = new EventMessage("SERVER_ADDING_TASK");
                eventMessage.setString("data", rsp);
                pluginManager.post(eventMessage);
                logger.info("MedicineSubscriberOnFunction  rxManager rsp发送结束" + rsp);
            }
        });

        //异常处方事件发送
        rxManager.on("RecipeSubscriber", new Action1<String>() {
            @Override
            public void call(String rsp) {
                logger.info("RecipeSubscriber  rxManager rsp结果" + rsp);
                EventMessage eventMessage = new EventMessage("SERVER_ABNORMAL_RECIPE");
                eventMessage.setString("data", rsp);
                pluginManager.post(eventMessage);
                logger.info("RecipeSubscriber  rxManager rsp发送结束" + rsp);
            }
        });

        rxManager.on("PUSHCONNECT", new Action1<String>() {
            @Override
            public void call(String rsp) {
                while (!Thread.interrupted()) {

                    try {
                        logger.info("发送请求");
                        Thread.sleep(15000);
                        EventMessage eventMessage = new EventMessage("SERVER_CONNECT");
                        eventMessage.setString("ip", "192.168.0.129");
                        eventMessage.setString("port", "8080");
                        eventMessage.setString("type", "2");
                        pluginManager.post(eventMessage);
                    }catch (Exception e){
                    }
                }

            }
        });


    }

    @Override
    public void onStop()     {
        connectThread.interrupt();
    }
}


