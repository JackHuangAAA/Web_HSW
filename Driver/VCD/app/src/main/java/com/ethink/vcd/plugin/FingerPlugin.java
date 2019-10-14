package com.ethink.vcd.plugin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.tools.serialport.SerialPort;
import com.ethink.tools.serialport.SerialPortManager;
import com.ethink.tools.serialport.SerialPortSetting;
import com.ethink.vcd.finger.FingerCommon;
import com.ethink.vcd.finger.FingerUtil;
import com.ethink.vcd.finger.SerialPortController;
import com.ethink.vcd.service.api.RxManager;
import com.ethink.vcd.utils.ResponseUtil;


/**
 * 指纹插件
 * */
public class FingerPlugin extends BasePlugin implements FunctionHandler    {
    private final RxManager rxManager;
    private Context context;
    private FingerUtil fingerUtil;
    private SerialPort scanner;
    public FingerPlugin(Context context) {
        super("FINGER");
        this.context = context;
        rxManager = new RxManager();
    //    fingerUtil = new FingerUtil(context,new FingerCommon("/dev/ttyS3",115200));
    }

    @Override
    public void onStart()     {
        initSerialPort();
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



    /**
     * 初始化扫码枪
     */
    public void initSerialPort() {
        try {
            //USB
//            UsbSerialPortSetting setting = new UsbSerialPortSetting(UsbSerialPortSetting.PL2303_VENDOR, UsbSerialPortSetting.PL2303_PRODUCT, 0,
//                    115200, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);
//            setting.setReadTimeout(0);
//            scanner = SerialPortManager.getSerialPort(context, setting);
            //串口
//            SerialPortSetting setting = new SerialPortSetting(
//                    115200, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);
//            setting.setReadTimeout(0);
//            scanner = SerialPortManager.getSerialPort("/dev/ttyS3", setting);
            logger.info("SCANNER","--------连接扫码-----------");
        } catch (Exception e) {
            EventMessage eventMessage = new EventMessage("SCANNER_RESULT");
            eventMessage.setString("data", e.getMessage());
            pluginManager.post(eventMessage);
            logger.error("open serial failed", e);
        }
    }

}
