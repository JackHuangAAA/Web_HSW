package com.ethink.vcd.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.ethink.vcd.Const;
import com.ethink.vcd.event.FingerPushMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsbFingerController implements USBDeviceFace {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private USBFingerCommon common;
    private volatile boolean cancel = true;
 private    FingerPushMessage fingerPushMessage;
    //指纹图片
    private String fingerImage = "";
    public UsbFingerController(Context context,FingerPushMessage fingerPushMessage) {
        this.fingerPushMessage=fingerPushMessage;
        common = new USBFingerCommon(context, this);
        common.requestUsb();
    }
        public void open(){
        common.requestUsb();
        }

    public void close() {
        common.uninit();
    }
    /**
     * 取消掉，可以结束whil循环
     **/
    public void cancel() {
        if (!cancel) {
            cancel = true;
            logger.debug("终止录入=====》：m_bCancel  {}", cancel);
        }
    }

    /***
     * 注册并上传
     * ***/
    public void remoteRegister(String uid) {
        cancel();
        new Thread(() -> {
            int[] w_nWidth = new int[1];
            int[] w_nHeight = new int[1];
            int re = common.Run_TestConnection();
            if (re != USBFingerCommon.ERR_SUCCESS) {
                //显示错误信息
                fingerPushMessage.message(1, common.GetErrorMsg(re));
                return;
            }
            cancel = false;
            int w_nUserID, result, w_nEnrollStep = 0, w_nGenCount = 3;
            int[] w_nDupID = new int[1];
            while (w_nEnrollStep < w_nGenCount) {
                fingerPushMessage.message(1, String.format("请按下手指，第(%s/%s)次!", (w_nEnrollStep + 1), w_nGenCount));
                // Capture
                if (Capturing() < 0) {
                    fingerPushMessage.message(1, "采集图像失败！");
                    return;
                }
                fingerPushMessage.message(1, "请抬起手指！");
                //绘制指纹图片
                byte[] m_binImage = new byte[1024 * 100];
                result = common.Run_UpImage(0, m_binImage, w_nWidth, w_nHeight);
                if (result == USBFingerCommon.ERR_SUCCESS) {
                    //开始绘制图片
                    fingerImage = createImageBase64(m_binImage, w_nWidth[0], w_nHeight[0]);
                    logger.info("指纹图片：" + fingerImage);
                }
                // Create Template
                result = common.Run_Generate(w_nEnrollStep);
                logger.info("创建模板次数：{}", w_nEnrollStep);
                if (result != USBFingerCommon.ERR_SUCCESS) {
                    if (result == USBFingerCommon.ERR_BAD_QUALITY) {
                        fingerPushMessage.message(1, "Bad quality. Try Again!");
                    } else {
                        fingerPushMessage.message(1, common.GetErrorMsg(result));
                        return;
                    }
                } else {
                    //录入指纹成功，开始上传
                    if (fingerPushMessage.upload("/register?tag=" + uid, fingerImage) != null) {
                        w_nEnrollStep++;
                    } else {
                        fingerPushMessage.message(1, "录入失败，请重试！");
                    }

                }

            }
            logger.info("run: --------------------执行完毕");
            fingerPushMessage.message(2, "录入成功");
        }).start();
    }

    /***
     * 后台比对
     * ***/
    public void remoteVerify() {
        new Thread(() -> {
            cancel();
            int[] w_nWidth = new int[1];
            int[] w_nHeight = new int[1];
            int re = common.Run_TestConnection();
            if (re != USBFingerCommon.ERR_SUCCESS) {
                logger.info("连接错误：" + common.GetErrorMsg(re));
                fingerPushMessage.message(1, "设备连接错误！");
                return;
            }
            cancel = false;
            fingerPushMessage.message(1, "请按下手指！");
            if (Capturing() < 0) {
                fingerPushMessage.message(1, "获取指纹信息失败！");
                return;
            }
            fingerPushMessage.message(1, "请抬起手指！");
            //绘制指纹图片
            byte[] m_binImage = new byte[1024 * 100];
            int result = common.Run_UpImage(0, m_binImage, w_nWidth, w_nHeight);
            if (result == USBFingerCommon.ERR_SUCCESS) {
                //开始绘制图片
                fingerImage = createImageBase64(m_binImage, w_nWidth[0], w_nHeight[0]);
                logger.info("指纹图片：" + fingerImage);
                String res = fingerPushMessage.upload("/match", fingerImage);
                if (!StringUtils.isEmpty(res)) {
                    fingerPushMessage.message(2, res);
                    return;
                }

            }
            fingerPushMessage.message(3, "验证失败！");
        }).start();
    }




    private String createImageBase64(byte[] m_binImage, int m_nImgWidth, int m_nImgHeight) {
        //绘制图片
        int nSize;
        byte[] m_bmpImage = new byte[1024 * 100];
        common.MakeBMPBuf(m_binImage, m_bmpImage, m_nImgWidth, m_nImgHeight);
        if ((m_nImgWidth % 4) != 0)
            nSize = m_nImgWidth + (4 - (m_nImgWidth % 4));
        else
            nSize = m_nImgWidth;
        nSize = 1078 + nSize * m_nImgHeight;
        //DebugManage.WriteBmp(m_bmpImage, nSize);
        Log.i("IMAGE", String.format("handleMessage: 对比指纹绘制：binImage=%s bmpImage=%s width=%s height=%s nsize=%s", m_binImage[0], m_bmpImage[0], m_nImgWidth, m_nImgHeight, nSize));
        Bitmap image = BitmapFactory.decodeByteArray(m_bmpImage, 0, nSize);
        return Const.bitmapToString(image);
    }

    private int Capturing() {
        int w_nRet;
        while (true) {
            w_nRet = common.Run_GetImage();
            if (w_nRet == USBFingerCommon.ERR_CONNECTION) {
                logger.info("连接错误---");
                return -1;
            } else if (w_nRet == USBFingerCommon.ERR_SUCCESS) {
                logger.info("获取成功getImage");
                break;
            }
            logger.info("当前状态 cancel ：{}", cancel);
            if (cancel) {
                return -1;
            }
        }
        return 0;
    }



    @Override
    public boolean checkUSB(UsbDevice usbDevice) {
        //   vid=8201  pid=30264
        return usbDevice.getVendorId() == 8201 && usbDevice.getProductId() == 30264;
    }

    @Override
    public void openUSB(UsbDevice usbDevice) {
        //打开usb接口
        common.connectInterface(usbDevice);
        if (!common.IsInit()) {
            common.requestUsb();
        } else {
            String[] w_strInfo = new String[1];
            if (common.Run_TestConnection() == USBFingerCommon.ERR_SUCCESS) {
                if (common.Run_GetDeviceInfo(w_strInfo) == USBFingerCommon.ERR_SUCCESS) {
                    logger.info("设备打开成功：\n Device Info" + w_strInfo[0]);
                } else
                    logger.info("设备打开失败");
            }
        }

    }


    @Override
    public void deniedPermission() {
        logger.info("设备权限被拒绝！");
    }
}
