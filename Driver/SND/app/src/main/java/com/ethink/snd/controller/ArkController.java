package com.ethink.snd.controller;

import com.ethink.tools.checksum.CheckSum;
import com.ethink.tools.serialport.SerialPort;
import com.ethink.tools.serialport.SerialPortManager;
import com.ethink.tools.serialport.SerialPortSetting;
import com.ethink.tools.serialport.usb.util.HexDump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArkController {
    private SerialPort serialPort;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public ArkController(String path, int baudRate) {
        //串口
        SerialPortSetting setting = new SerialPortSetting(
                baudRate, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);
//   usb方法
//        UsbSerialPortSetting setting = new UsbSerialPortSetting(UsbSerialPortSetting.PL2303_VENDOR, UsbSerialPortSetting.PL2303_PRODUCT, 0,
//                    115200, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);
        try {
            setting.setReadTimeout(8000);
            serialPort = SerialPortManager.getSerialPort(path, setting);
            //usb方法
            //  finger = SerialPortManager.getSerialPort(context, setting);
        } catch (IOException e) {
            logger.error("串口打开失败", e);
        }
    }


    /**
     * 打开抽屉
     **/
    public String openDrawer(Set<Integer> set) {
        ByteBuffer buffer1 = ByteBuffer.allocate(8);
        ByteBuffer buffer2 = ByteBuffer.allocate(8);
        for (Integer door:set){
            if (door > 8) {
                buffer2.put((byte) (0x00000001 << (door - 9)));
            } else {
                buffer1.put((byte) (0x00000001 << (door- 1)));
            }
        }
        byte[] da1 = buffer1.array();
        byte[] da2 = buffer2.array();
        byte re1 = por(da1, 1, da1.length, da1[0]);
        byte re2 = por(da2, 1, da2.length, da2[0]);
        byte[] da = new byte[]{0x55, (byte) 0xAA, 0x03, 0x01};
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(da).put(re1).put(re2);
        byte check = CheckSum.xor(buffer.array(), 3, 3, buffer.get(2));
        buffer.put(check).put((byte) 0xF0);
        byte[] data = buffer.array();
        logger.info("Write: {}", HexDump.toHexString(data));
        if (write(data, data.length)) {
            //读取
            byte[] rec = new byte[256];
            read(rec, 0, 8);
            int a = HexDump.toUInt16LE(rec, 4);
            String s = Integer.toBinaryString(a);
            logger.info("转换后：" + s);
            return s;
        }
        return "000";
    }
    /**
     * 设置冷藏柜温度
     **/
    public boolean setTemperature(int tem) {
        byte[] da = new byte[]{0x55, (byte) 0xAA, 0x03, 0x06};
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(da);
        buffer.put( (byte)(tem & 0xff));
        buffer.put((byte)0x00);
        byte xor = CheckSum.xor(buffer.array(),3, 2, buffer.get(2));
        buffer.put(xor);
        buffer.put((byte) 0xF0);
        byte[] data = buffer.array();
        if (write(data, data.length)) {
            byte[] re = new byte[7];
            read(re, 0, re.length);
            return re[4] == ((byte) 0x01);
        }
        return false;
    }


    public static byte por(byte[] buffer, int offset, int length, byte iv) {
        byte result = iv;
        for (int i = offset; i < Math.min(offset + length, buffer.length); i++) {
            result |= buffer[i];
        }
        return result;
    }
    /**
     *
     * 查询内部温度
     * **/
    public String temperatureArk(){
        byte []da=new byte[]{0x55, (byte) 0xAA, 0x01, 0x08};
        ByteBuffer buffer=ByteBuffer.allocate(7);
        buffer.put(da);
        buffer.put(CheckSum.xor(buffer.array(),3, 1, buffer.get(2)));
        buffer.put((byte)0xF0);
        byte [] data=buffer.array();
        if(write(data,data.length)){
            byte[] re = new byte[8];
            read(re, 0, re.length);
            int result = HexDump.toUInt16LE(re, 4);
            logger.info("温度整数：{}",result);
            int a = HexDump.toUInt16LE(re, 5);
            String s = Integer.toBinaryString(a);
            logger.info("单位正负换后：" + s);
            if(s.substring(1,1).equals("1")){
                //1为负数
                s="-"+s;
            }
          /*  if(s.startsWith("0")){
                //单位摄氏度℃
                s+="℃";
            }else{
                //单位华氏温度℉
                s+="℉";
            }*/
            return  String.valueOf(result);

        }
        return "查询错误！";
    }

    /**
     * 读取开关状态 疫苗柜
     **/
    public void switchStatus() {
        byte[] da = new byte[]{0x55, (byte) 0xAA, 0x01, 0x03};
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(da);
        byte ch = CheckSum.xor(byteBuffer.array(), 3, 1, byteBuffer.get(2));
        byteBuffer.put(ch);
        byteBuffer.put((byte) (0xF0));
        byte[] data = byteBuffer.array();
        if (write(data, data.length)) {
            //读取
            byte[] rec = new byte[256];
            read(rec, 0, 8);
            logger.info(String.format("锁状态：%02X,%02X", rec[4], rec[5]));
            logger.info("转换之后：{}--- {}", hexStringToByte(String.format("%02X", rec[4])), hexStringToByte(String.format("%02X", rec[5])));
        }
    }

    /**
     * 16进制转2进制
     *
     * @param hex
     * @return
     */
    public static String hexStringToByte(String hex) {
        int i = Integer.parseInt(hex, 16);
        return Integer.toBinaryString(i);
    }

    /**
     * 查询主控板状态
     **/
    public int arkStatus() {
        byte[] da = new byte[]{0x55, (byte) 0xAA, 0x01, 0x00};
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(da);
        byte ch = CheckSum.xor(byteBuffer.array(), 3, 1, byteBuffer.get(2));
        byteBuffer.put(ch);
        byteBuffer.put((byte) (0xF0));
        byte[] data = byteBuffer.array();
        if (write(data, data.length)) {
            //读取
            byte[] rec = new byte[256];
            read(rec, 0, 7);
            return rec[4];
        }
        return 0;
    }

    /**
     * 读取温度传感器
     **/
    public List<Double> temperature(Set<Integer> set) {
        logger.info("查询的传感器："+set);
        List<Double> list1 = new ArrayList<>();
        ByteBuffer da1 = ByteBuffer.allocate(6);
        for (Integer s:set) {
            da1.put((byte) (1 << (s - 1)));
        }
        byte[] b_data = da1.array();
        byte t = por(b_data, 1, b_data.length, b_data[0]);

        byte[] da = new byte[]{0x55, (byte) 0xAA, 0x02, 0x02};
        ByteBuffer byteBuffer = ByteBuffer.allocate(7);
        byteBuffer.put(da);
        byteBuffer.put(t);
        byte ch = CheckSum.xor(byteBuffer.array(), 3, 2, byteBuffer.get(2));
        byteBuffer.put(ch);
        byteBuffer.put((byte) (0xF0));
        byte[] data = byteBuffer.array();
        if (write(data, data.length)) {
            //读取
            byte[] rec = new byte[256];
            read(rec, 0, 16);
            //1号探头
            list1.add(wen(rec, 1));
            //2号探头
            list1.add(wen(rec, 2));
            //3号探头
            list1.add(wen(rec, 3));
            //4号探头
            list1.add(wen(rec, 4));
            //4号探头
            list1.add(wen(rec, 5));
        }
        return list1;
    }
    /***
     * 查询单个温度
     * **/
    public double temperature(int num) {
        byte[] da = new byte[]{0x55, (byte) 0xAA, 0x02, 0x02};
        ByteBuffer byteBuffer = ByteBuffer.allocate(7);
        byteBuffer.put(da);
        byteBuffer.put((byte) (1 << (num - 1)));
        byte ch = CheckSum.xor(byteBuffer.array(), 3, 2, byteBuffer.get(2));
        byteBuffer.put(ch);
        byteBuffer.put((byte) (0xF0));
        byte[] data = byteBuffer.array();
        if (write(data, data.length)) {
            //读取
            byte[] rec = new byte[256];
            read(rec, 0, 16);
            return wen(rec, num);
        }
        return 0;

    }


    //获取 温度
    private double wen(byte[] rec, int n) {
        logger.info(String.format(n + "当前温度：%02X,%02X", rec[2 * n + 2], rec[2 * n + 2 + 1]));
        int result = HexDump.toUInt16LE(rec, 2 * n + 2);
        logger.info(n + "当前温度：" + result);
        return new BigDecimal(result).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 大疫苗柜开门指令
     **/
    public boolean openDoor() {
        byte[] da = new byte[]{0x55, (byte) 0xAA, 0x01, 0x04};
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(da);
        byte ch = CheckSum.xor(byteBuffer.array(), 3, 1, byteBuffer.get(2));
        byteBuffer.put(ch);
        byteBuffer.put((byte) (0xF0));
        byte[] data = byteBuffer.array();
        if (write(data, data.length)) {
            //读取
            byte[] rec = new byte[256];
            read(rec, 0, 7);
            int i = Integer.parseInt(HexDump.toHexString(rec[4]), 16);
            return i == 1;
        }
        return false;
    }

    /**
     * 读取大疫苗柜开关状态
     **/
    public void bigSwitchStatus() {
        byte[] da = new byte[]{0x55, (byte) 0xAA, 0x01, 0x05};
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(da);
        byte ch = CheckSum.xor(byteBuffer.array(), 3, 1, byteBuffer.get(2));
        byteBuffer.put(ch);
        byteBuffer.put((byte) (0xF0));
        byte[] data = byteBuffer.array();
        if (write(data, data.length)) {
            //读取
            byte[] rec = new byte[256];
            read(rec, 0, 7);
            logger.info("疫苗柜门状态{}", hexStringToByte(String.format("%02X", rec[4])));
        }
    }

    /**
     * 读取程序版本信息
     **/
    public void sysValue() {
        byte[] da = new byte[]{0x55, (byte) 0xAA, 0x01, 0x06};
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(da);
        byte ch = CheckSum.xor(byteBuffer.array(), 3, 1, byteBuffer.get(2));
        byteBuffer.put(ch);
        byteBuffer.put((byte) (0xF0));
        byte[] data = byteBuffer.array();
        if (write(data, data.length)) {
            //读取
            byte[] rec = new byte[256];
            read(rec, 0, 28);
            if (rec[4] == 0x01) {
                //0x01正常  0x02异常
                // PCB 版本号,其中 100 是可变的，“ YM”表  示疫苗，ZKB表示主控板
                byte[] ban = new byte[11];
                //原数组, 从元数据的起始位置开始, 目标数组,目标数组的开始起始位置,要copy的数组的长度
                //   System.arraycopy(ban,0,rec,5,11);
                logger.info("版本号：" + HexDump.toHexString(ban));
                //程序版本号
                byte[] sys = new byte[5];
                //    System.arraycopy(sys,0,rec,16,5);
                logger.info("程序版本号：" + HexDump.toHexString(sys));
                //通信协议版本号，一般只有 0 是可变
                byte[] xie = new byte[4];
                //   System.arraycopy(xie,0,rec,21,4);
                logger.info("程序版本号：" + HexDump.toHexString(xie));
                //55 AA 16 06 01 5A 59 5F 58 59 4D 47 5F 31 30 30 56 31 2E 30 30 56 31 2E 30 18 F0
            }
        }
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
            serialPort.readFull(buffer, offset, maxlen);
            logger.info("Read: {}", HexDump.toHexString(buffer, offset, maxlen));
        } catch (Exception e) {
            logger.error("串口读取失败", e);
            return false;
        }
        return true;
    }


    public void close() {
        try {
            serialPort.close();
        } catch (IOException e) {
            logger.error("串口关闭失败", e);
        }
    }
}
