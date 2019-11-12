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
import com.ethink.vfd.utils.DrawerData;
import com.ethink.vfd.utils.ResponseUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 * 疫苗柜控制插件
 * 包含温度，锁开关 功能
 * **/
public class ArkPlugin extends BasePlugin implements FunctionHandler, Runnable { private Context context;
    private ArkController arkController;
    //温度实时上送
    private Thread temThread;

    public ArkPlugin(Context context) {
        super("CONTROLLER_BOARD");
        logger.info("--------------------连接主控板--------------------");
        this.context = context;
        this.arkController = new ArkController("/dev/ttyUSB0", 115200);
    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        if (pluginMessage == null) return pluginMessage;
        logger.info("pluginName " + pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        switch (functionName) {
            case "OPEN_DRAWER":
                String n = pluginMessage.getString("num");
                pluginMessage.changeToResponse();
                if (StringUtils.isEmpty(n)) {
                    pluginMessage.set("res", "请输入抽屉编号");
                    return pluginMessage;
                }
                String res = arkController.openDrawer(DrawerData.getDrawerList(n));
                pluginMessage.set("res", res);
                break;
            case "TEMPERATURE":
                String b = pluginMessage.getString("num");
                pluginMessage.changeToResponse();
                if (StringUtils.isEmpty(b)) {
                    pluginMessage.set("res", "请输入硬件编号");
                    return pluginMessage;
                }
                String[] a = b.split(",");
                Set<Integer> set = new HashSet<>();
                for (String s : a) {
                    if (!StringUtils.isEmpty(s)) {
                        try {
                            set.add(Integer.parseInt(s));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
                if (!set.isEmpty()) {
                    List<Double> list = arkController.temperature(set);
                    pluginMessage.setString("res", JSON.toJSONString(list));
                }
                break;
            case "ARK_STATUS":
                if (arkController.arkStatus() == 1) {
                    //   ResponseUtil.success(pluginMessage, "状态正常", null);
                }
                break;
            case "SYS_VALUES":
                arkController.sysValue();
                // ResponseUtil.success(pluginMessage, "系统信息", null);
                break;
            case "SWITCH_STATUS":
                arkController.switchStatus();
                break;
            case "OPEN_DOOR":
                pluginMessage.changeToResponse();
               boolean re= arkController.openDoor();
               pluginMessage.setBool("rsp",re);
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
        registerFunction("OPEN_DRAWER", this);
        //冷藏柜开门
        //开锁
        registerFunction("OPEN_DOOR", this);
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
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000 * 60);
                if (arkController != null) {
                    logger.info("查询温度-----");
                    EventMessage eventMessage = new EventMessage("NOW_TEMPERATURE");
                    List<Double> list = arkController.temperature(set);
                    eventMessage.setString("res",JSON.toJSONString(list));
                    eventMessage.setBool("message", true);
                    logger.info("温度主动上报：" + JSON.toJSONString(list));
                    pluginManager.post(eventMessage);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                EventMessage eventMessage = new EventMessage("NOW_TEMPERATURE");
                eventMessage.setBool("message", false);
                eventMessage.setString("message", "查询失败");
                logger.info("温度查询异常：" + JSON.toJSONString(eventMessage));
                //  pluginManager.post(eventMessage);
            }

        }
    }
}
