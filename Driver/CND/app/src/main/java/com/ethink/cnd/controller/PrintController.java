package com.ethink.cnd.controller;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.ethink.tools.serialport.SerialPort;
import com.ethink.tools.serialport.SerialPortManager;
import com.ethink.tools.serialport.SerialPortSetting;
import com.ethink.tools.serialport.usb.util.HexDump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PrintController {
    private SerialPort serialPort;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected volatile boolean ready = false;
    private static final String TAG="PrintController";

    public PrintController(String path, int baudRate) {
        //串口
        SerialPortSetting setting = new SerialPortSetting(
                baudRate, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);
//   usb方法
//        UsbSerialPortSetting setting = new UsbSerialPortSetting(UsbSerialPortSetting.PL2303_VENDOR, UsbSerialPortSetting.PL2303_PRODUCT, 0,
//                    115200, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);
        try {
            setting.setReadTimeout(10000);
            setting.setWriteTimeout(3000);
            serialPort = SerialPortManager.getSerialPort(path, setting);
            logger.info("打印机正常-------------------");
            //usb方法
            //  finger = SerialPortManager.getSerialPort(context, setting);
        } catch (IOException e) {
            logger.error("打印机------串口打开失败", e);
        }
    }

    //初始打印机ID
    public void initPrintID() {
        byte[] by = new byte[]{0x1B, 0x5A};
        if (write(by, by.length)) {
            byte[] rec = new byte[10];
            read(rec, 0, 4);
        }

    }

    //初始打印机
    public void initPrint() {
        byte[] by = new byte[]{0x1B, 0x30};
        if (write(by, by.length)) {
            byte[] rec = new byte[10];
            read(rec, 0, 4);
        }

    }

    public void stop() {
        ready = false;
    }


    //清除错误
    public boolean clearError() {
        byte[] by = new byte[]{0x1B, 0x6C};
        return write(by, by.length);
    }

    //打印机状态请求
    public void printStatus() {
        byte[] by = new byte[]{0x1B, 0x6A};
        if (write(by, by.length)) {
            byte[] rec = new byte[3];
            read(rec, 0, 3);
        }

    }

    //删除打印缓冲区并取消打印
    public void delbuffer() {
        byte[] by = new byte[]{0x1B, 0x6C, 0x7F};
        write(by, by.length);
    }

    /**
     * 测量打印纸宽高
     **/
    public void measurement() {
        byte[] by = new byte[]{0x1B, 0x6C, 0x1B, 0x2D, 0x1B, 0x7D, 0x2D};
        if (write(by, by.length)) {
            byte[] rec = new byte[12];
            read(rec, 0, 12);
        }

    }


    /***
     * 读取磁条信息
     * 1、查询纸状态 1b 6c 1B 20 42
     *
     * */
    public void readStripe() {
        ready = true;
        logger.info("ready状态:{}", ready);
        while (ready) {
            //1B 42 35 43 1B 5A --43表示纸张就绪
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            if (paperStatus() == 0x43) {
                //已经进来了纸开始读取
                byte[] da = new byte[]{0x1B, 0x5D, 0x1B, 0x4F};
                if (write(da, da.length)) {
                    ready = false;
                    //循环读取
                    //  1B73 3130313837313033323536 303137313D3D3030363733323533 1C
                    //  ByteBuffer buffer = ByteBuffer.allocate(256);
                    List<Byte> list = new ArrayList<>();
                    byte[] result = new byte[256];
                    try {
                        serialPort.readEnd(result, 0, 256, (byte) 0x1C);
                        for (int i = 2; i < result.length; i++) {
                            if (result[i] == 0x1C) {
                                break;
                            }
                            list.add(result[i]);
                            // buffer.put(result[i]);
                        }
                        byte[] r = new byte[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            r[i] = list.get(i);
                        }
                        logger.info("磁条：{}", HexDump.toHexString(r));
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                if (!inPaper()) {
                    ready = false;
                    break;
                }

            }
        }

    }

    /****
     * 开始打印
     * **/
    public void print(String str) {
        if (StringUtils.isEmpty(str)) return;
        ready = true;
        logger.info("打印操作ready状态:{},打印文字{}", ready, str);
        while (ready) {
            //1B 42 35 43 1B 5A --43表示纸张就绪
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (paperStatus() == 0x43) {
                boolean re = horizontal();
                logger.info("水平定位：{}", re);
                //开始打印操作
                byte[] da = new byte[0];
                try {
                    da = str.getBytes("GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //logger.info("数组：{},长度：{}",da,da.length);
                ByteBuffer buffer = ByteBuffer.allocate(da.length + 4);
                buffer.put(da);
                //打印+退纸
                buffer.put((byte) 0x0D);
                buffer.put((byte) 0x0A);
                buffer.put((byte) 0x1B);
                buffer.put((byte) 0x4F);
                boolean result = write(buffer.array(), da.length + 4);
                logger.info("打印结果：{}", result);
                ready = false;
                break;
            } else {
                if (!inPaperLarge()) {
                    ready = false;
                    break;
                }

            }
        }

    }


    /**
     * 垂直定位 进纸操作===》打印
     **/
    public boolean inPaperLarge() {
        //进纸命令
        byte[] in = new byte[]{0x1B, 0x6C, 0x1B, 0x2F, 0x31, 0x30, 0x31, 0x31, 0x30};
        return write(in, in.length);
    }
    /**
     * 垂直定位杭州市疫苗本
     **/
    public boolean inPaperCity(int num) {
        byte[] in;
    if(num<=9){
        in = new byte[]{0x1B, 0x6C, 0x1B, 0x2F, 0x31, 0x30, 0x33, 0x32, 0x30};
    }else{
        //底部计算
        in = new byte[]{0x1B, 0x6C, 0x1B, 0x2F, 0x30, 0x30, 0x36, 0x32, 0x30};
    }
        return write(in, in.length);
    }

    /**
     * 垂直定位省疫苗本
     **/
    public boolean inPaperProvince(int num) {
        byte[] in;
        if(num<=9){
            //从顶部计算
            in = new byte[]{0x1B, 0x6C, 0x1B, 0x2F, 0x31, 0x30, 0x33, 0x35, 0x30};
        }else{
            //底部计算
            in = new byte[]{0x1B, 0x6C, 0x1B, 0x2F, 0x30, 0x30, 0x36, 0x31, 0x30};
        }
        return write(in, in.length);
    }


    /**
     * 水平定位
     */
    private boolean horizontal() {
        byte[] butter = new byte[]{0x1B, 0x7C, 0x41, 0x30, 0x31, 0x30};
        return write(butter, butter.length);
    }
    /**
     * 杭州市疫苗本水平定位
     */
    private boolean cityHorizontal() {
        byte[] butter = new byte[]{0x1B, 0x7C, 0x41, 0x30, 0x34, 0x30};
        return write(butter, butter.length);
    }

    /***
     * 设置行间距
     * */
    private boolean verticalSpace() {
        byte[] butter = new byte[]{0x1B, 0x26,   0x36, 0x36};
        return write(butter, butter.length);
    }


    /**
     * 浙江省疫苗本水平定位
     */
    private boolean provinceHorizontal() {
        byte[] butter = new byte[]{0x1B, 0x7C, 0x41, 0x30, 0x34, 0x30};
        return write(butter, butter.length);
    }

    /**
     * 进纸操作
     **/
    public boolean inPaper() {
        //进纸命令
        byte[] in = new byte[]{0x1B, 0x6C, 0x1B, 0x6E, 0x1B, 0x2E, 0x07, 0x1B, 0x51, 0x32, 0x32, 0x35, 0x1B, 0x5A, 0x1B, 0x4C, 0x30, 0x30, 0x30};
        return write(in, in.length);

    }

    /**
     * 查询纸张状态 1B 73 31 30 31 38 37 31 30 33 32 35 36 30 31 37 31 3D
     **/
    public byte paperStatus() {
        byte[] data = new byte[]{0x1B, 0x6C, 0x1B, 0x20, 0x42};
        if (write(data, data.length)) {
            byte[] bu = new byte[6];
            try {
                serialPort.readEnd(bu, 0, 6, (byte) 0x5A);
                logger.info("循环读取纸张状态：{}", HexDump.toHexString(bu));
                return bu[3];
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return 0x00;
    }

    /***
     * 浙江省疫苗本
     * **/
    public void zheProvince(int num,String data) {
        ready = true;
        logger.info("打印操作ready状态:{}", ready);
        while (ready) {
            //1B 42 35 43 1B 5A --43表示纸张就绪
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (paperStatus() == 0x43) {
                boolean r=verticalSpace();
                logger.info("行间距：r={}",r);
                boolean re = provinceHorizontal();
                logger.info("水平定位：{}", re);
                //开始打印操作
                byte[] da =printData(num,data);
                //logger.info("数组：{},长度：{}",da,da.length);
                ByteBuffer buffer = ByteBuffer.allocate(da.length + 4);
                buffer.put(da);
                //打印+退纸
                buffer.put((byte) 0x0D);
                buffer.put((byte) 0x0A);
                buffer.put((byte) 0x1B);
                buffer.put((byte) 0x4F);
                boolean result = write(buffer.array(), da.length + 4);
                logger.info("打印结果：{}", result);
                ready = false;
                break;
            }

            else {

                if (!inPaperProvince(num)) {
                    ready = false;
                    break;
                }

            }
        }

    }
/***
 * @return 获取要打印的byte数组
 * **/
    public static byte[] printData(int num,String data) {
        if (num < 1) num = 1;
        if(num>9){
            //大于9的时候从地下往上打印
            num-=9;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 1; index < num; index++) {
            stringBuilder.append("\r\n");
            Log.d(TAG, "printData: 换行------{}"+index);
        }
        stringBuilder.append(data);
        try {
            return stringBuilder.toString().getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[]{0x00};
    }


    /****
     * 浙江省的打印格式
     * */
    public static  String provinceFormat(boolean need_name,String vaccine,String date,String no,String unit,String position,String name){
     if(need_name){
         return String.format("%s %s   %s     %s        %s   %s",vaccine,date,no,unit,position,name);
     }else{
         return String.format("%s  %s    %s      %s     %s",date,no,unit,position,name);
     }
    }

    /****
     * 杭州市的打印格式
     * */
    public static  String cityFormat(String remark,String date,String no,String unit,String position,String name){
        return String.format("%s %s  %s   %s       %s    %s",remark,date,no,unit,position,name);
    }

    /***
     * 杭州市疫苗本
     * **/
    public void hangCity(int num,String data) {
        ready = true;
        logger.info("打印操作ready状态:{}", ready);
        while (ready) {
            //1B 42 35 43 1B 5A --43表示纸张就绪
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (paperStatus() == 0x43) {
                boolean r=verticalSpace();
                logger.info("行间距：r={}",r);
                boolean re = cityHorizontal();
                logger.info("水平定位：{}", re);
                //开始打印操作
                byte[] da =printData(num,cityFormat("卡介苗","20191111","xx小学","xxx医院","胳膊","张三"));
                //logger.info("数组：{},长度：{}",da,da.length);
                ByteBuffer buffer = ByteBuffer.allocate(da.length + 4);
                buffer.put(da);
                //打印+退纸
                buffer.put((byte) 0x0D);
                buffer.put((byte) 0x0A);
                buffer.put((byte) 0x1B);
                buffer.put((byte) 0x4F);
                boolean result = write(buffer.array(), da.length + 4);
                logger.info("打印结果：{}", result);
                ready = false;
                break;
            }

            else {
                if (!inPaperCity(num)) {
                    ready = false;
                    break;
                }

            }
        }

    }


    /***
     *退纸
     * */
    public void exitPaper() {
        byte[] by = new byte[]{0x1B, 0x4F};
        write(by, by.length);
    }


    private boolean write(byte[] data, int len) {
        int ret = -1;
        try {
            ret = serialPort.write(data, 0, len);
            logger.info("Write: {}", HexDump.toHexString(data, 0, len));
        } catch (Exception e) {
            logger.error("串口写入失败", e);
        }
        return ret >= 0;
    }


    public boolean read(byte[] buffer, int offset, int maxlen) {
        try {
            serialPort.read(buffer, offset, maxlen);
            logger.info("Read: {}", HexDump.toHexString(buffer, offset, maxlen));
        } catch (Exception e) {
            logger.error("串口读取失败", e);
            return false;
        }
        return true;
    }


    public int read(byte[] buffer, int maxlen) {
        try {
            int len = serialPort.read(buffer, 0, maxlen);
            logger.info("Read: {}", HexDump.toHexString(buffer, 0, maxlen));
            return len;
        } catch (Exception e) {
            logger.error("串口读取失败", e);
            return 0;
        }
    }


}
