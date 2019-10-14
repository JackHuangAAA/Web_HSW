package com.ethink.vcd.plugin;

import android.content.Context;

import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.message.EventMessage;
import com.ethink.tools.serialport.SerialPort;
import com.ethink.tools.serialport.SerialPortManager;
import com.ethink.tools.serialport.SerialPortSetting;
import com.ethink.vcd.service.api.RxManager;



/**
 * 扫码枪插件
 */
public class ScannerPlugin extends BasePlugin implements Runnable {
private static final String TAG="ScannerPlugin";
    private final RxManager rxManager;
    private Context context;
    private SerialPort scanner;
    private Thread thread;

    public ScannerPlugin(Context context) {
        super("SCANNER");
        this.context = context;
        rxManager = new RxManager();
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
            SerialPortSetting setting = new SerialPortSetting(
                    115200, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);
            setting.setReadTimeout(0);
            scanner = SerialPortManager.getSerialPort("/dev/ttyS3", setting);
            logger.info("SCANNER","--------连接扫码-----------");
            thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            EventMessage eventMessage = new EventMessage("SCANNER_RESULT");
            eventMessage.setString("data", e.getMessage());
            pluginManager.post(eventMessage);
            logger.error("open serial failed", e);
        }
    }

    /**
     * 读取扫码枪数据
     */
    @Override
    public void run() {
        byte[] buffer = new byte[256];
        //     byte[]b=new byte[]{0x16,0x54,0x0D,0x21};
        StringBuilder scantext=new StringBuilder();
        while (!Thread.interrupted()) {
            try {
                int ret = scanner.read(buffer, 0, 256);
                //   scanner.write(b,0,4);
                if (ret > 0) {
                    String tmp = new String(buffer, 0, ret);
                    scantext.append(tmp);
                    //Log.d(TAG, "run: 扫码结果："+scantext);
                   // logger.info("扫码数据"+scantext);
                    //scantext.charAt(scantext.length() - 1) == '\r'
                    if (scantext.charAt(scantext.length() - 1) == 0x0A) {
                        //Log.d(TAG, "run: 扫码枪截取数据："+scantext);
                        //logger.info("扫码枪截取1数据" + scantext);
                        logger.info("扫码枪截取数据" + scantext.substring(0, scantext.length() - 2));
                        EventMessage eventMessage = new EventMessage("SCANNER_RESULT");
                        eventMessage.setString("data", scantext.substring(0, scantext.length() - 2));
                        pluginManager.post(eventMessage);
                        scantext.delete(0,scantext.length());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("读取串口异常", e.getMessage());
                //EventBus.getDefault().post(new WriteConsoleEvent("读串口超时 " + e.getMessage()));
            }
        }
    }

    @Override
    public void onStart() throws Throwable {
//        logger.info("sanner 执行 start");
//        Looper.prepare();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                logger.info("扫码推送执行");
//                EventMessage eventMessage = new EventMessage("SCANNER_RESULT");
//                eventMessage.setString("data", "扫码枪推送过来的数据");
//                pluginManager.post(eventMessage);
//            }
//        },15000);
        logger.info("初始化扫码枪----------------");
        initSerialPort();
    }

    @Override
    public void onStop() throws Throwable {
        scanner.close();
        thread.interrupt();
    }
}


