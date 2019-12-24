package com.ethink.lineup.plugin;

import android.content.Context;
import android.hardware.usb.UsbDevice;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.ethink.lineup.controller.USBDeviceFace;
import com.ethink.lineup.controller.UsbConnectUtil;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.PluginMessage;

import java.io.UnsupportedEncodingException;

import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;


/**
 * 串口打印机插件
 */
public class USBPrintPlugin extends BasePlugin implements FunctionHandler, USBDeviceFace {
    private static final String TAG = "PrintPlugin";
    private static POSSDK pos_sdk = null;
    private POSInterfaceAPI interface_usb = null;


    private Context context;

    public USBPrintPlugin(Context context) {
        super("PRINT_TICKET");
        this.context = context;
        //申请usb权限
        new UsbConnectUtil(context, this).requestUsb();

    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        switch (pluginMessage.getFunctionName()) {
            case "PRINT":
                String title = pluginMessage.getString("title");
                String num = pluginMessage.getString("number");
                Integer current = pluginMessage.getInt("current");
                logger.info("参数title{}  num {}  current {}",title,num,current);
                if(!StringUtils.isEmpty(title)&&!StringUtils.isEmpty(num)&& !ObjectUtils.isEmpty(current)){
                    strand(title,num,current);
                }
                break;

        }
        pluginMessage.changeToResponse();
        return pluginMessage;
    }


    @Override
    public void onStart() throws Throwable {
        registerFunction("PRINT", this);
    }


    @Override
    public void onStop() {
        if (interface_usb != null) {
            int error_code = interface_usb.CloseDevice();
            logger.info("关闭打印机连接 {}", error_code);
        }

    }


    @Override
    public boolean checkUSB(UsbDevice usbDevice) {
        return usbDevice.getVendorId() == 5455 && usbDevice.getProductId() == 5455;
    }

    @Override
    public void openUSB(UsbDevice usbDevice) {
        interface_usb = new POSUSBAPI(context);
        pos_sdk = new POSSDK(interface_usb);
        int result = interface_usb.OpenDevice(5455, 5455);
        logger.info("打开USB返回 {}", result);
        int error = pos_sdk.systemReset();
        logger.info("打印机复位 {}", error);
    }

    @Override
    public void deniedPermission() {

    }


    private void strand(String title,String num, int current) {
        try {
            byte[] send_buf;
            int error_code = 0;
            error_code = pos_sdk.systemSelectPrintMode(POSSDK.PRINT_MODE_STANDARD);
            error_code = pos_sdk.textStandardModeAlignment(POSSDK.TextAlignmentCenter);
            //set the horizontal and vertical motion units
            error_code = pos_sdk.systemSetMotionUnit(200, 200);
            //set line height
            error_code = pos_sdk.textSetLineHeight(1);
            error_code = pos_sdk.textSelectFont( POSSDK.FontTypeChinese, POSSDK.FontStyleBold);
            //set character size
            error_code = pos_sdk.textSelectFontMagnifyTimes(2, 2);
            error_code = pos_sdk.systemFeedLine(1);
            //print text
            send_buf = title.getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
            //feed line
            error_code = pos_sdk.systemFeedLine(3);
            error_code = pos_sdk.textSelectFontMagnifyTimes(3, 3);
            send_buf = num.getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
            error_code = pos_sdk.systemFeedLine(3);
            //改变显示位置
            error_code = pos_sdk.textStandardModeAlignment(POSSDK.TextAlignmentLeft);
            logger.info("TestPrintText: 打印改变位置 -----------textStandardModeAlignment--------" + error_code);
            error_code = pos_sdk.textSelectFontMagnifyTimes(1, 1);
            send_buf = String.format("您前面还有%s位，请注意叫号!",current).getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
            error_code = pos_sdk.systemFeedLine(2);
            String info="您也可以手机扫码实时关注排队情况，以防过号!";
            send_buf = info.getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
        //    改变显示位置打印条码
            error_code = pos_sdk.systemFeedLine(4);
            error_code = pos_sdk.textStandardModeAlignment(POSSDK.TextAlignmentCenter);
            error_code = pos_sdk.barcodePrintQR(info,info.length(), 0, 6, 1, 0);
            error_code = pos_sdk.systemFeedLine(4);
            error_code = pos_sdk.textStandardModeAlignment(POSSDK.TextAlignmentRight);
            String time= TimeUtils.getNowString();
            send_buf = time.getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
            error_code = pos_sdk.systemFeedLine(1);
            pos_sdk.systemCutPaper(POSSDK.CutPartAfterFeed, 0);
//            logger.info("切纸纸{}", error_code);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}


