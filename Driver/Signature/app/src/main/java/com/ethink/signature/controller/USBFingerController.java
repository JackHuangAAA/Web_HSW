package com.ethink.signature.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.ethink.signature.event.FingerPushMessage;
import com.ethink.signature.util.Const;
import com.ethink.signature.util.USBDeviceFace;
import com.ethink.signature.util.UsbConnectUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.nidfpsensor.NIDFPFactory;
import com.zkteco.android.biometric.nidfpsensor.NIDFPSensor;
import com.zkteco.android.biometric.nidfpsensor.exception.NIDFPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class USBFingerController implements USBDeviceFace {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final int VID = 6997;    //zkteco device VID  6997
    private static int PID = 772;    //NIDFPSensor PID 根据实际设置
    private boolean mbStart = false;
    private NIDFPSensor mNIDFPSensor = null;
    private Context context;

    private boolean mbStop = true;
    private byte[] mBufImage = null;
    private UsbConnectUtil usbConnectUtil;
    private FingerPushMessage fingerPushMessage;
    public USBFingerController(Context context, FingerPushMessage fingerPushMessage) {
        this.context = context;
        this.fingerPushMessage=fingerPushMessage;
        usbConnectUtil=  new UsbConnectUtil(context, this);
        usbConnectUtil.requestUsb();

    }
    public void open(){
        usbConnectUtil.requestUsb();
    }

    @Override
    public boolean checkUSB(UsbDevice device) {
        return device.getVendorId() == VID && device.getProductId() == PID;
    }

    @Override
    public void openUSB(UsbDevice usbDevice) {
        openDevice();
    }

    @Override
    public void deniedPermission() {

    }
    //取消采集
    public void cancel(){
        mbStop = true;
        if (!executorService.isTerminated()) {
            Log.i("SIGNATURE", "线程在运行------");
            mbStop = true;
            try {
                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void openDevice() {
        // Start fingerprint sensor
        Map<String, Object> fingerprintParams = new HashMap<>();
        //set vid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_VID, VID);
        //set pid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_PID, PID);
        mNIDFPSensor = NIDFPFactory.createNIDFPSensor(context, TransportType.USBSCSI, fingerprintParams);
        if (mbStart) {
            return;
        }
        try {
            //连接设备
            mNIDFPSensor.open(0);
            //分配读取指纹图像内存(width*height Bytes)
            mBufImage = new byte[mNIDFPSensor.getFpImgWidth() * mNIDFPSensor.getFpImgHeight()];
          //  entryFinger();
            //   Toast.makeText(this, "设备连接成功，固件版本：" + mNIDFPSensor.getFirmwareVersion()+"，图像宽：" + mNIDFPSensor.getFpImgWidth() + ",图像高：" + mNIDFPSensor.getFpImgHeight(), Toast.LENGTH_SHORT).show();
            mbStart = true;
        } catch (NIDFPException e) {
            //连接失败，尝试重启模块
            try {
                mNIDFPSensor.rebootDevice(0);
            } catch (NIDFPException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            //  Toast.makeText(mContext, "Open device fail.errorcode:\"+ e.getErrorCode() + \"err message:\" + e.getMessage() + \"inner code:\" + e.getInternalErrorCode()", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "指纹设备连接失败", Toast.LENGTH_LONG).show();
            //mTxtReport.setText("Open device fail.errorcode:"+ e.getErrorCode() + "err message:" + e.getMessage() + "inner code:" + e.getInternalErrorCode());
        }
    }

    //开始录入手指
    public void remoteRegister(String uid) {
        //  请按下手指！
          cancel();
        mbStop = false;
        fingerPushMessage.message(1, "请按下手指！");
        executorService.execute(() -> {
            while (!mbStop) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    mNIDFPSensor.GetFPRawData(0, mBufImage);
                } catch (NIDFPException e) {
                    e.printStackTrace();
                    continue;
                }
                byte[] retQualityScore = new byte[1];
                int ret = mNIDFPSensor.getQualityScore(mBufImage, retQualityScore);
                final byte qualityScore = retQualityScore[0];
                if (1 != ret || qualityScore < 45) {
                    continue;
                }
                fingerPushMessage.message(1, "请抬起手指！");
                //开始生成图片
                if (mNIDFPSensor != null) {
                    Bitmap fingerBitmap = ToolUtils.renderCroppedGreyScaleBitmap(mBufImage, mNIDFPSensor.getFpImgWidth(), mNIDFPSensor.getFpImgHeight());
                   String   fingerImage = Const.bitmapToString(fingerBitmap);
                    logger.info("指纹图片：" + fingerImage);
                    //录入指纹成功，开始上传
                    if (fingerPushMessage.upload("/register?tag=" + uid, fingerImage) != null) {
                      //  fingerPushMessage.message(2, "录入成功");
                        //录入成功
                        mbStop = true;
                    }else{
                        fingerPushMessage.message(1, "录入失败，请重试！");
                    }

                }else{
                    fingerPushMessage.message(1, "录入失败，请重新打开设备！");
                    mbStop=true;
                    mbStart=false;
                }
//                byte[] mTestFeature = new byte[512];
//                float score = mNIDFPSensor.ImageMatch(0, mBufImage, mTestFeature);

            }

        });

    }

    /****
     * 后台比对
     * **/
    public void remoteVerify(){
        //  请按下手指！
        cancel();
        mbStop = false;
        fingerPushMessage.message(1, "请按下手指！");
        executorService.execute(() -> {
            while (!mbStop) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    mNIDFPSensor.GetFPRawData(0, mBufImage);
                } catch (NIDFPException e) {
                    e.printStackTrace();
                    continue;
                }
                byte[] retQualityScore = new byte[1];
                int ret = mNIDFPSensor.getQualityScore(mBufImage, retQualityScore);
                final byte qualityScore = retQualityScore[0];
                if (1 != ret || qualityScore < 45) {
                    continue;
                }
                fingerPushMessage.message(1, "请抬起手指！");
                //开始生成图片
                if (mNIDFPSensor != null) {
                    Bitmap fingerBitmap = ToolUtils.renderCroppedGreyScaleBitmap(mBufImage, mNIDFPSensor.getFpImgWidth(), mNIDFPSensor.getFpImgHeight());
                    String   fingerImage = Const.bitmapToString(fingerBitmap);
                    logger.info("指纹图片：" + fingerImage);
                    //录入指纹成功，开始上传
                    String res = fingerPushMessage.upload("/match", fingerImage);
                    if (!StringUtils.isEmpty(res)) {
                        fingerPushMessage.message(2, res);
                        //录入成功
                        mbStop = true;
                    }else{
                        fingerPushMessage.message(1, "验证失败，请重试！");
                    }

                }else{
                    fingerPushMessage.message(1, "验证失败，请重新打开设备！");
                    mbStop=true;
                    mbStart=false;
                }
//                byte[] mTestFeature = new byte[512];
//                float score = mNIDFPSensor.ImageMatch(0, mBufImage, mTestFeature);

            }

        });


    }



    public void close(){
        //尝试关闭设备
        if (mbStart) {
            mbStop = true;  //停止采集线程
            try {
                mNIDFPSensor.close(0);  //关闭设备
            } catch (NIDFPException e) {
                e.printStackTrace();
            }
        }
        mbStart = false;
        // Destroy fingerprint sensor when it's not used
        NIDFPFactory.destroy(mNIDFPSensor);
        mNIDFPSensor = null;

    }



}
