package com.ethink.signature.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.view.View;

import com.ethink.signature.util.USBDeviceFace;
import com.ethink.signature.util.UsbConnectUtil;
import com.zkteco.android.IDReader.IDPhotoHelper;
import com.zkteco.android.IDReader.WLTService;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.module.idcard.IDCardReader;
import com.zkteco.android.biometric.module.idcard.IDCardReaderFactory;
import com.zkteco.android.biometric.module.idcard.exception.IDCardReaderException;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;
import com.zkteco.android.biometric.module.idcard.meta.IDPRPCardInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IDCardController implements USBDeviceFace {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final int VID = 1024;    //zkteco device VID  6997
    private static int PID = 50010;    //NIDFPSensor PID 根据实际设置
    private Context context;
    private boolean bopen = false;
    private boolean bStoped = false;
    private IDCardReader idCardReader = null;
    private UsbConnectUtil usbConnectUtil;
    public IDCardController(Context context) {
        this.context = context;
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
        bStoped = true;
        if (!executorService.isTerminated()) {
            Log.i("SIGNATURE", "线程在运行------");
            try {
                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void openDevice() {
            if (bopen)
            {
                logger.info("设备已连接");
                return;
            }
            try {
                Map idrparams = new HashMap();
                idrparams.put(ParameterHelper.PARAM_KEY_VID, VID);
                idrparams.put(ParameterHelper.PARAM_KEY_PID, PID);
                idCardReader = IDCardReaderFactory.createIDCardReader(context, TransportType.USB, idrparams);
                idCardReader.open(0);
                bStoped = false;
                logger.info("连接设备成功");
                bopen = true;

            }catch (IDCardReaderException e)
            {
                logger.info("连接设备失败");
                bopen=false;
            }

    }

    /****
     *
     * 销毁设备
     * **/
    public void destroy(){
        stop();
        IDCardReaderFactory.destroy(idCardReader);
    }
    /**
     * 断开设备
     * */
    public void stop( )
    {
        if (!bopen)
        {
            return;
        }
        cancel();
        try {
            idCardReader.close(0);
        } catch (IDCardReaderException e) {
            e.printStackTrace();
        }
       logger.info("设备断开连接");
        bopen = false;
    }


    private boolean authenticate()
    {
        try {
            idCardReader.findCard(0);
            idCardReader.selectCard(0);
            return true;
        }catch (IDCardReaderException e)
        {
            return false;
        }
    }
/****
 * 开始读取
 * **/
public void startRead(){
    cancel();
    bStoped = false;
    if(!bopen){
        logger.info("请先打开设备！");
        return;
    }
        executorService.execute(()->{
            while (!bStoped) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final long nTickstart = System.currentTimeMillis();
                try {
                    idCardReader.findCard(0);
                    idCardReader.selectCard(0);
                }catch (IDCardReaderException e)
                {
                    //continue;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int retType = 0;
                try {
                    retType = idCardReader.readCardEx(0, 0);
                }
                catch (IDCardReaderException e)
                {
                   logger.error("读卡失败，错误信息：" + e.getMessage());
                }
                if (retType == 1 || retType == 2 || retType == 3)
                {
                    bStoped=true;
                    final long nTickUsed = (System.currentTimeMillis()-nTickstart);
                    logger.info("读卡成功：" +   "次" + "，耗时：" + nTickUsed + "毫秒");

                            if (retType == 1)
                            {
                                final IDCardInfo idCardInfo = idCardReader.getLastIDCardInfo();
                                //姓名adb
                                String strName = idCardInfo.getName();
                                //民族
                                String strNation = idCardInfo.getNation();
                                //出生日期
                                String strBorn = idCardInfo.getBirth();
                                //住址
                                String strAddr = idCardInfo.getAddress();
                                //身份证号
                                String strID = idCardInfo.getId();
                                //有效期限
                                String strEffext = idCardInfo.getValidityTime();
                                //签发机关
                                String strIssueAt = idCardInfo.getDepart();
                                logger.info("读取次数："  +  ",耗时："+  nTickUsed +  "毫秒, 卡类型：居民身份证,姓名：" + strName +
                                        "，民族：" + strNation + "，住址：" + strAddr + ",身份证号：" + strID);
                                if (idCardInfo.getPhotolength() > 0) {
                                    byte[] buf = new byte[WLTService.imgLength];
                                    if (1 == WLTService.wlt2Bmp(idCardInfo.getPhoto(), buf)) {
                                        Bitmap bitmap=IDPhotoHelper.Bgr2Bitmap(buf);
                                        logger.info("读取到图片retType {}",retType);
                                    }
                                }
                            }
                            else if (retType == 2)
                            {
                                final IDPRPCardInfo idprpCardInfo = idCardReader.getLastPRPIDCardInfo();
                                //中文名
                                String strCnName = idprpCardInfo.getCnName();
                                //英文名
                                String strEnName = idprpCardInfo.getEnName();
                                //国家/国家地区代码
                                String strCountry = idprpCardInfo.getCountry() + "/" + idprpCardInfo.getCountryCode();//国家/国家地区代码
                                //出生日期
                                String strBorn = idprpCardInfo.getBirth();
                                //身份证号
                                String strID = idprpCardInfo.getId();
                                //有效期限
                                String strEffext = idprpCardInfo.getValidityTime();
                                //签发机关
                                String strIssueAt = "公安部";
                               logger.info("读取次数："  +   ",耗时："+  nTickUsed +  "毫秒, 卡类型：外国人永居证,中文名：" + strCnName + ",英文名：" +
                                        strEnName + "，国家：" + strCountry + ",证件号：" + strID);
                                if (idprpCardInfo.getPhotolength() > 0) {
                                    byte[] buf = new byte[WLTService.imgLength];
                                    if (1 == WLTService.wlt2Bmp(idprpCardInfo.getPhoto(), buf)) {
                                   Bitmap bitmap=IDPhotoHelper.Bgr2Bitmap(buf);
                                        logger.info("读取到图片retType {}",retType);

                                    }
                                }
                            }
                            else
                            {
                                final IDCardInfo idCardInfo = idCardReader.getLastIDCardInfo();
                                //姓名
                                String strName = idCardInfo.getName();
                                //民族,港澳台不支持该项
                                String strNation = "";
                                //出生日期
                                String strBorn = idCardInfo.getBirth();
                                //住址
                                String strAddr = idCardInfo.getAddress();
                                //身份证号
                                String strID = idCardInfo.getId();
                                //有效期限
                                String strEffext = idCardInfo.getValidityTime();
                                //签发机关
                                String strIssueAt = idCardInfo.getDepart();
                                //通行证号
                                String strPassNum = idCardInfo.getPassNum();
                                //签证次数
                                int visaTimes = idCardInfo.getVisaTimes();
                              logger.info("读取次数："  +   ",耗时："+  nTickUsed +  "毫秒, 卡类型：港澳台居住证,姓名：" + strName +
                                        "，住址：" + strAddr + ",身份证号：" + strID + "，通行证号码：" + strPassNum +
                                        ",签证次数：" + visaTimes);
                                if (idCardInfo.getPhotolength() > 0) {
                                    byte[] buf = new byte[WLTService.imgLength];
                                    if (1 == WLTService.wlt2Bmp(idCardInfo.getPhoto(), buf)) {
                                        logger.info("读取到图片retType {}",retType);
                                       Bitmap bitmap=IDPhotoHelper.Bgr2Bitmap(buf);
                                    }
                                }
                            }
                        }
                    }
        });

}





}
