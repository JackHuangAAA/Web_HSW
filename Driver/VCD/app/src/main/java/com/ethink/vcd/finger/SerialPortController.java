package com.ethink.vcd.finger;

import android.content.Context;

import com.ethink.tools.serialport.SerialPort;
import com.ethink.tools.serialport.SerialPortManager;
import com.ethink.tools.serialport.SerialPortSetting;
import com.ethink.tools.serialport.UsbSerialPortSetting;
import com.ethink.tools.serialport.usb.util.HexDump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SerialPortController {
    private SerialPort finger;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public SerialPortController(Context context,String path,int baudRate){
        logger.info("SerialPortController constructor");
        //串口
       SerialPortSetting setting = new SerialPortSetting(
                baudRate, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);
//
//        UsbSerialPortSetting setting = new UsbSerialPortSetting(UsbSerialPortSetting.PL2303_VENDOR, UsbSerialPortSetting.PL2303_PRODUCT, 0,
//                    115200, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);

        try {
            finger = SerialPortManager.getSerialPort(path, setting);
//            finger = SerialPortManager.getSerialPort(context, setting);
        } catch (IOException e) {
            logger.error("串口打开失败", e);
        }
    }

    public boolean Write(byte[] data,int len){
        int ret = -1;
        try {
            ret = finger.write(data,0,len);
            logger.info("Write: {}", HexDump.toHexString(data,0,len));
        } catch (Exception e) {
            logger.error("串口写入失败",e);
        }
        return ret >=0;
    }

    public boolean Read(byte[] buffer,int maxlen){
        try {
            //finger.readFull(buffer,0,maxlen);
            int count = 0;
            while(count < maxlen){
                count += finger.read(buffer,0 + count,maxlen - count);
            }
            logger.info("Read: {}", HexDump.toHexString(buffer,0,maxlen));
            logger.info("Read count: {}", count);
        } catch (Exception e) {
            logger.error("串口读取失败",e);
            return false;
        }
        return true;
    }

    public void close(){
        try {
            finger.close();
        } catch (IOException e) {
            logger.error("串口关闭失败",e);
        }
    }

}
