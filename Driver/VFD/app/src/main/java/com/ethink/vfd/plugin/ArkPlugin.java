package com.ethink.vfd.plugin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vfd.controller.ArkController;
import com.ethink.vfd.utils.ResponseUtil;

/***
 * 疫苗柜控制插件
 * 包含温度，锁开关 功能
 * **/
public class ArkPlugin extends BasePlugin implements FunctionHandler, Runnable {
    private Context context;
    private ArkController arkController;
    //温度实时上送
    private Thread temThread;

    public ArkPlugin(Context context) {
        super("CONTROLLER_BOARD");
        this.context = context;
     //   this.arkController = new ArkController("/dev/ttyUSB0", 115200);
    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        if (pluginMessage == null) return pluginMessage;
        logger.info("pluginName " + pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        pluginMessage.changeToResponse();
        switch (functionName) {
            case "OPEN":
                String a = pluginMessage.getString("num");
                if (!StringUtils.isEmpty(a)) {
                    try {
                        int num = Integer.parseInt(a);
                       // boolean re = arkController.openDoorData(num);
                      //  ResponseUtil.success(pluginMessage, re, null);
                    } catch (NumberFormatException e) {
                        logger.info("序号格式化错误", e);
                        ResponseUtil.fail(pluginMessage, "错误：" + e.getMessage());
                    }
                }
                break;
            case "TEMPERATURE":
                String b = pluginMessage.getString("num");
                if (!StringUtils.isEmpty(b)) {
                    try {
                        int num = Integer.parseInt(b);
                         double re = arkController.temperature(num);
                        ResponseUtil.success(pluginMessage, "成功", ResponseUtil.data("temp", String.valueOf(re)));
                    } catch (NumberFormatException e) {
                        logger.info("序号格式化错误", e);
                        ResponseUtil.fail(pluginMessage, "错误：" + e.getMessage());
                    }
                }
                break;
            case "ARK_STATUS":
                if (arkController.arkStatus() == 1) {
                    ResponseUtil.success(pluginMessage, "状态正常", null);
                } else {
                    ResponseUtil.fail(pluginMessage, "状态异常");
                }
                break;
            case "SYS_VALUES":
                arkController.sysValue();
                ResponseUtil.success(pluginMessage, "系统信息", null);
                break;
            case "SWITCH_STATUS":
                arkController.switchStatus();
                break;

        }

        return pluginMessage;
    }

    @Override
    public void onStart() throws Throwable {
        //服务链接检测线程
        temThread = new Thread(this);
        temThread.start();
        //开锁
        registerFunction("OPEN", this);
        //温度
        registerFunction("TEMPERATURE", this);
        //疫苗柜状态
        registerFunction("ARK_STATUS", this);
        //系统信息
        registerFunction("SYS_VALUES", this);
        //开关状态
        registerFunction("SWITCH_STATUS", this);
    }

    @Override
    public void onStop() throws Throwable {
        temThread.interrupt();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000 * 6);
                if (arkController != null) {
                    logger.info("查询温度-----");
                    EventMessage eventMessage = new EventMessage("NOW_TEMPERATURE");
                    JSONObject object = new JSONObject();
//                    object.put("temp1", arkController.temperature(1));
//                    object.put("temp2", arkController.temperature(2));
//                    object.put("temp3", arkController.temperature(3));
//                    object.put("temp4", arkController.temperature(4));
//                    object.put("temp5", arkController.temperature(5));
//                    eventMessage.setString("data", object.toJSONString());
                    eventMessage.setString("type", "2");
                    eventMessage.setString("message", "查询成功");
                    logger.info("温度上报：" + JSON.toJSONString(eventMessage));
                    pluginManager.post(eventMessage);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                EventMessage eventMessage = new EventMessage("NOW_TEMPERATURE");
                eventMessage.setArrayValue("data", "");
                eventMessage.setString("type", "1");
                eventMessage.setString("message", "查询失败");
                logger.info("温度查询异常：" + JSON.toJSONString(eventMessage));
              //  pluginManager.post(eventMessage);
            }

        }
    }
}
