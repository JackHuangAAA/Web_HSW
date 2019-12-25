package com.ethink.lineup.plugin;

import android.content.Context;

import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.PluginMessage;

import java.io.File;
import java.io.UnsupportedEncodingException;

import POSAPI.POSInterfaceAPI;
import POSAPI.POSSerialAPI;
import POSSDK.POSSDK;


/**
 * 串口打印机插件
 */
public class SerialPrintPlugin extends BasePlugin implements FunctionHandler {
    private static final String TAG = "PrintPlugin";
    private static POSSDK pos_com = null;
    private POSInterfaceAPI interface_com = null;

    private int FontType = 0;
    private int FontStyle = 0;
    private int Alignment = 0;
    private int HorStartingPosition = 1;
    private int VerStartingPosition = 1;
    private int LineHeight = 0;
    private int HorizontalTimes = 0;
    private int VerticalTimes = 0;


    private static final int PRINT_MODE_STANDARD = 0;
    private static final int PRINT_MODE_PAGE = 1;


    public SerialPrintPlugin(Context context) {
        super("PRINT_TICKET");

    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        switch (pluginMessage.getFunctionName()) {
            case "PRINT":
                String title = pluginMessage.getString("title");
                String num = pluginMessage.getString("number");
                int current = pluginMessage.getInt("current");
                    //   printController.print(title, num, String.format("您当前号码%s，前面还有%s人", num, current), current);
                    String text_data = String.format("您当前号码%s，前面还有%s人", num, current);
                    int DataLength = 0;
                    try {
                        DataLength = text_data.getBytes("GB18030").length;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
//                    int error_code = printInfo.TestPrintText(pos_com, PrintInfo.PRINT_MODE_PAGE, text_data, FontType, FontStyle,
//                            Alignment, HorStartingPosition, VerStartingPosition, LineHeight, HorizontalTimes, VerticalTimes);

                 //   logger.info("打印返回{}", error_code);
                break;

        }
        return pluginMessage;
    }

    /***
     * 初始化串口打印机设备
     * **/
    private void initSerialPrint() {
        interface_com = new POSSerialAPI();
        pos_com = new POSSDK(interface_com);
        int result = interface_com.OpenDevice(new File("/dev/ttyS1"), 38400);
        logger.info("打开串口返回 {}", result);
        int error_code = pos_com.systemSelectPrintMode(PRINT_MODE_STANDARD);
        logger.info("设置标准模式：{}", error_code);
//        error_code = pos_com.systemSelectPrintMode(PRINT_MODE_PAGE);
//        error_code = pos_com.pageModeSetPrintArea(0, 0, 640, 500, 0);
//        error_code = pos_com.pageModeSetStartingPosition(20, 200);

        String txtbuf = "123456789山东新北洋";
        byte[] send_buf = new byte[0];
        try {
            send_buf = txtbuf.getBytes("GB18030");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        error_code = pos_com.textPrint(send_buf, send_buf.length);

//        String text_data = "测试打印机----------";
//        error_code = printInfo.TestPrintText(pos_com, PrintInfo.PRINT_MODE_PAGE, text_data, FontType, FontStyle,
//                Alignment, HorStartingPosition, VerStartingPosition, LineHeight, HorizontalTimes, VerticalTimes);

        logger.info("打印返回{}", error_code);
    }


    @Override
    public void onStart() throws Throwable {
        registerFunction("PRINT", this);
        initSerialPrint();
    }


    @Override
    public void onStop() {
        if (interface_com != null) {
            int error_code = interface_com.CloseDevice();
            logger.info("关闭打印机连接 {}", error_code);
        }

    }


}


