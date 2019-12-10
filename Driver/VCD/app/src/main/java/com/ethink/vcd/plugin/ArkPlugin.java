package com.ethink.vcd.plugin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.R;
import com.ethink.vcd.controller.ArkController;
import com.ethink.vcd.utils.DrawerData;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/***
 * 疫苗柜控制插件
 * 包含温度，锁开关 功能
 * **/
public class ArkPlugin extends BasePlugin implements FunctionHandler, Runnable {
    private Context context;
    private ArkController arkController;
    //温度实时上送
    private Thread temThread;
    private Timer timer = new Timer();
    private MessageToast messageToast;

    public ArkPlugin(Context context) {
        super("CONTROLLER_BOARD");
        logger.info("--------------------连接主控板--------------------");
        this.context = context;
        messageToast = new MessageToast(context);
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
               /* String b = pluginMessage.getString("num");
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
                    if (!list.isEmpty()) {
                      pluginMessage.set("res",JSON.toJSONString(list));
                    }
                }*/
                pluginMessage.changeToResponse();
                String a = arkController.temperatureArk();
                pluginMessage.set("res", a);
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
            case "SET_TEMPERATURE":
                String tem = pluginMessage.getString("num");
                pluginMessage.changeToResponse();
                boolean result = arkController.setTemperature(Integer.parseInt(tem));
                pluginMessage.setBool("rsp", result);
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
        //温度
        registerFunction("TEMPERATURE", this);
        //疫苗柜状态
        registerFunction("ARK_STATUS", this);
        //系统信息
        registerFunction("SYS_VALUES", this);
        //开关状态
        registerFunction("SWITCH_STATUS", this);
        //设置温度
        registerFunction("SET_TEMPERATURE", this);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (arkController != null) {
                    Set<Integer> set = new HashSet<>();
                    set.add(1);
                    set.add(2);
                    set.add(3);
                    set.add(4);
                    set.add(5);
                    List<Double> list = arkController.temperature(set);
                    messageToast.obtainMessage(1, list).sendToTarget();
                }

            }
        }, 30000, 30000);
    }

    @Override
    public void onStop() throws Throwable {
        temThread.interrupt();
    }

    @Override
    public void run() {
        Set<Integer> set = new HashSet<>();
        set.add(1);
//        set.add(2);
//        set.add(3);
//        set.add(4);
//        set.add(5);
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000 * 60);
                if (arkController != null) {
                    logger.info("查询温度-----");
                    EventMessage eventMessage = new EventMessage("NOW_TEMPERATURE");
//                    List<Double> list = arkController.temperature(set);
//                   eventMessage.setString("res",JSON.toJSONString(list));
                    String a = arkController.temperatureArk();
                    eventMessage.setString("res", a);
                    eventMessage.setBool("message", true);
                    logger.info("温度主动上报：" + a);
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

    public static class MessageToast extends Handler {
        private WeakReference<Context> weakReference;
        private Dialog dialog;
    private TextView textView;
        MessageToast(Context context) {
            weakReference = new WeakReference<>(context);
            if (weakReference.get() != null) {
                View view = LayoutInflater.from(weakReference.get()).inflate(R.layout.popup_layout, null, false);
                textView=view.findViewById(R.id.text);
                dialog=new Dialog(weakReference.get());
                dialog.setCanceledOnTouchOutside(true);
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
               // window.setLayout(ConvertUtils.dp2px(500), ConvertUtils.dp2px(400));
                dialog.setContentView(view);
                WindowManager.LayoutParams attr = window.getAttributes();
                if (attr != null) {
                    attr.height = 200;
                    attr.width = 200;
                    attr.gravity = Gravity.CENTER;
                    window.setAttributes(attr);
                }
            }

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && dialog != null) {
                if (textView != null) {
                    textView.setText("当前温度: \n"+JSON.toJSONString(msg.obj));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0新特性
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1);
                    } else {
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                    }
                    if (!dialog.isShowing())
                        dialog.show();
                }

            }
        }
    }


}
