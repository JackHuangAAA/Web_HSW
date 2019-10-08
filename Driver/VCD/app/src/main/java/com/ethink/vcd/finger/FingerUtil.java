package com.ethink.vcd.finger;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.R;
import com.ethink.vcd.utils.ResponseUtil;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FingerUtil {
    private static final String TAG = "FingerUtil";
    private FingerCommon fingerCommon;
    private int m_nMaxFpCount = 500;

    private static boolean m_bCancel = true;
    private long m_nPassedTime;
    //当前输入的id
    private int m_nUserID;
    private Context context;
    private DialogHandler dialogHandler;
    private FingerData fingerData = new FingerData();
    private ExecutorService executorService;
    private byte[] m_binImage = new byte[1024 * 100];

    public FingerUtil(Context context, FingerCommon fc) {
        this.fingerCommon = fc;
        this.context = context;
        dialogHandler = new DialogHandler(this);
        dialogHandler.sendEmptyMessage(0);
        executorService = Executors.newCachedThreadPool();
    }

    /**
     * 取消掉，可以结束whil循环
     **/
    private void cancel() {
        m_bCancel = true;
    }

    public void usbOpen(PluginMessage pluginMessage) {
        if (!fingerCommon.IsInit()) {
            if (!fingerCommon.OpenComm()) {
                Log.e(TAG, "usbOpen: 打开设备出错！");
                 ResponseUtil.fail(pluginMessage,"打开设备出错！");
            }
        } else {
            Log.i(TAG, "usbOpen: usb已连接");
            int result = fingerCommon.Run_TestConnection();
            Log.i(TAG, "usbOpen: usb连接测试结果：result=" + result + "=====" + GetErrorMsg(result));
            if (result == FingerCommon.ERR_SUCCESS) {
                String[] w_strInfo = new String[1];
                Log.i(TAG, "usbOpen: usb测试连接正常");
                if (fingerCommon.Run_GetDeviceInfo(w_strInfo) == FingerCommon.ERR_SUCCESS) {
                    Log.i(TAG, "usbOpen: USB指纹设备正常");
                 ResponseUtil.success(pluginMessage,"指纹设备已连接",null);
                }
            } else {
                Log.i(TAG, "usbOpen: usb指纹设备连接不上");
                ResponseUtil.fail(pluginMessage,"指纹设备连接失败");
            }

        }

    }

    //获取可用id
    public void getOneEmptyId() {
         cancel();
        int[] w_nEmptyID = new int[1];
        if (!fingerCommon.IsInit()) {
            return;
        }
        int w_nRet = fingerCommon.Run_GetEmptyID(1, m_nMaxFpCount, w_nEmptyID);
        Log.i(TAG, "查找可用id结果=" + w_nRet);
        if (w_nRet != FingerCommon.ERR_SUCCESS) {
            return;
        }
        Log.i(TAG, "可用id======" + w_nEmptyID[0]);
        m_nUserID = w_nEmptyID[0];
    }

    //获取模板总数
    public void templateTotal(PluginMessage pluginMessage) {
        cancel();
        int w_nRet;
        int[] w_nEnrollCount = new int[1];
        if (!fingerCommon.IsInit()) {
            Log.i(TAG, "templateTotal: 模块未连接");
            ResponseUtil.fail(pluginMessage,"模块未连接");
        } else {
            w_nRet = fingerCommon.Run_GetEnrollCount(1, m_nMaxFpCount, w_nEnrollCount);
            if (w_nRet != FingerCommon.ERR_SUCCESS) {
                //显示错误信息
                Log.i(TAG, "获取失败" + GetErrorMsg(w_nRet));
                ResponseUtil.fail(pluginMessage,GetErrorMsg(w_nRet));
            }
            Log.i(TAG, "templateTotal: " + String.format("Result : Success\r\nEnroll Count = %d", w_nEnrollCount[0]));
            ResponseUtil.success(pluginMessage,"ok",ResponseUtil.data("count",String.valueOf(w_nEnrollCount[0])));
        }
    }

    public void close(  ) {
        if (fingerCommon != null){
            fingerCommon.CloseComm();
        }

    }

    public void search() {
        if (!fingerCommon.IsInit()) {
            fingerData.setMsg("模块未未连接");
            return;
        }
        cancel();
        //  fingerCommon.Run_SLEDControl(1);
        m_bCancel = false;
        executorService.execute(() -> {
            int[] w_nID = new int[1];
            int[] w_nLearned = new int[1];
            int[] w_nWidth = new int[1];
            int[] w_nHeight = new int[1];
            while (true) {
                fingerData.setMsg("请按下手指");
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                if (Capturing() < 0)
                    return;
                fingerData.setMsg("请抬起手指");
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                // Up Cpatured Image
                int w_nRet = fingerCommon.Run_UpImage(0, m_binImage, w_nWidth, w_nHeight);
                if (w_nRet != FingerCommon.ERR_SUCCESS) {
                    fingerData.setMsg(GetErrorMsg(w_nRet));
                    dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                    return;
                }
                // Draw image
                fingerData.setWidth(w_nWidth[0]);
                fingerData.setHeight(w_nHeight[0]);
                fingerData.setBinImage(m_binImage);
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                // Create template
                m_nPassedTime = SystemClock.elapsedRealtime();
                w_nRet = fingerCommon.Run_Generate(0);
                if (w_nRet != FingerCommon.ERR_SUCCESS) {
                    fingerData.setMsg(GetErrorMsg(w_nRet));
                    dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                    if (w_nRet == FingerCommon.ERR_CONNECTION)
                        return;
                    else {
                        SystemClock.sleep(1000);
                        continue;
                    }
                }
                // Identify
                w_nRet = fingerCommon.Run_Search(0, 1, m_nMaxFpCount, w_nID, w_nLearned);
                m_nPassedTime = SystemClock.elapsedRealtime() - m_nPassedTime;

                if (w_nRet == FingerCommon.ERR_SUCCESS)
                    fingerData.setTips(String.format("Result : Success\r\nTemplate No : %s, Learn Result : %s\r\nMatch Time : %s ms", w_nID[0], w_nLearned[0], m_nPassedTime));
                else {
                    String m_strPost = GetErrorMsg(w_nRet) + String.format("\r\nMatch Time : %sms", m_nPassedTime);
                    fingerData.setTips(m_strPost);
                }
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
            }

        });


    }


    public void delTemplateAll(PluginMessage pluginMessage) {
        int w_nRet;
        cancel();
        if (!fingerCommon.IsInit()) {
            fingerData.setTips("请先初始化连接！");
            Log.i(TAG, "请先初始化连接");
            dialogHandler.obtainMessage(3, fingerData).sendToTarget();
            ResponseUtil.fail(pluginMessage,"请先初始化连接");
            return;
        }
        w_nRet = fingerCommon.Run_DelChar(1, m_nMaxFpCount);
        if (w_nRet != FingerCommon.ERR_SUCCESS) {
            fingerData.setTips(GetErrorMsg(w_nRet));
            Log.i(TAG, fingerData.getTips());
            dialogHandler.obtainMessage(3, fingerData).sendToTarget();
            ResponseUtil.fail(pluginMessage,GetErrorMsg(w_nRet));
            return;
        }
        fingerData.setTips("Delete all OK !");
        Log.i(TAG, fingerData.getTips());
        dialogHandler.obtainMessage(3, fingerData).sendToTarget();
        ResponseUtil.fail(pluginMessage,"Delete all OK !");
    }


    //注册指纹
    public void register(PluginMessage pluginMessage) {
        int[] w_nState = new int[1];
        getOneEmptyId();
        if (m_nUserID < 1) {
            Log.i(TAG, "未获取到可用id");
            pluginMessage.setBool("res",false);
            ResponseUtil.fail(pluginMessage,"未获取到可用id");
            return;
        }
        int w_nRet = fingerCommon.Run_GetStatus(m_nUserID, w_nState);
        if (w_nRet != FingerCommon.ERR_SUCCESS) {
            Log.i(TAG, GetErrorMsg(w_nRet));
            //显示错误信息
            ResponseUtil.fail(pluginMessage,GetErrorMsg(w_nRet));
            return;
        }

        if (w_nState[0] == FingerCommon.GD_TEMPLATE_NOT_EMPTY) {
            Log.i(TAG, "Template is already exist");
            //显示错误信息
        ResponseUtil.fail(pluginMessage,"Template is already exist");
            return;
        }
        fingerCommon.Run_SLEDControl(1);
        m_bCancel = false;
        fingerData.setMsg("请放置手指！");
        //显示默认图片
        fingerData.setWidth(-1);
        dialogHandler.obtainMessage(1, fingerData).sendToTarget();
        executorService.execute(() -> {
            int w_nUserID, result, w_nEnrollStep = 0, w_nGenCount = 3;
            int[] w_nDupID = new int[1];
            int[] w_nWidth = new int[1];
            int[] w_nHeight = new int[1];


            w_nUserID = m_nUserID;
            while (w_nEnrollStep < w_nGenCount) {
                Log.d(TAG, String.format("Input finger #%d!", w_nEnrollStep + 1));
                fingerData.setMsg(String.format("Input finger #%d!", (w_nEnrollStep + 1)));
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                // Capture
                if (Capturing() < 0)
                    return;
                fingerData.setMsg("Release your finger.");
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                // Up Cpatured Image
                result = fingerCommon.Run_UpImage(0, m_binImage, w_nWidth, w_nHeight);
                if (result != FingerCommon.ERR_SUCCESS) {
                    fingerData.setMsg(GetErrorMsg(result));
                    dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                    //  dialogHandler.obtainMessage(2, fingerData).sendToTarget();
                    return;
                }
                Log.i(TAG, "run: 绘制指纹 width=" + w_nWidth[0] + "-----height=" + w_nHeight[0]);
                // Draw image
                fingerData.setWidth(w_nWidth[0]);
                fingerData.setHeight(w_nHeight[0]);
                fingerData.setBinImage(m_binImage);
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                // Create Template
                result = fingerCommon.Run_Generate(w_nEnrollStep);

                if (result != FingerCommon.ERR_SUCCESS) {
                    if (result == FingerCommon.ERR_BAD_QUALITY) {
                        fingerData.setMsg("Bad quality. Try Again!");
                        dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                        continue;
                    } else {
                        fingerData.setMsg(GetErrorMsg(result));
                        Log.i(TAG, fingerData.getMsg());
                        dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                        //   dialogHandler.obtainMessage(2, fingerData).sendToTarget();
                        return;
                    }
                }
                w_nEnrollStep++;
            }
            Log.i(TAG, "run: --------------------执行完毕");
            //m_strPost = "Release Finger";
            //m_FpImageViewer.post(runShowStatus);

            // Merge

            //. Merge Template

            result = fingerCommon.Run_Merge(0, w_nGenCount);
            if (result != FingerCommon.ERR_SUCCESS) {
                fingerData.setMsg(GetErrorMsg(result));
                Log.i(TAG, fingerData.getMsg());
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                //  dialogHandler.obtainMessage(2, fingerData).sendToTarget();
                Log.i(TAG, "run:失败原因：" + GetErrorMsg(result));
                ResponseUtil.fail(pluginMessage,GetErrorMsg(result));
                return;
            }
            Log.i(TAG, "run: 存储指纹数据------");
            //. Store template
            result = fingerCommon.Run_StoreChar(w_nUserID, 0, w_nDupID);

            if (result != FingerCommon.ERR_SUCCESS) {
                if (result == FingerCommon.ERR_DUPLICATION_ID) {
                    fingerData.setMsg(String.format("Result : Fail\r\nDuplication ID = %d", w_nDupID[0]));
                    Log.i(TAG, "run:错误：" + fingerData.getMsg());
                } else {
                    fingerData.setMsg(GetErrorMsg(result));
                    Log.i(TAG, "run:错误信息：" + fingerData.getMsg());
                }
                ResponseUtil.fail(pluginMessage,fingerData.getMsg());
                return;
            } else {
                fingerData.setMsg(String.format("Result : Success\rTemplate No : %d", m_nUserID));
                Log.i(TAG, "run:成功录入指纹：" + fingerData.getMsg());

                ResponseUtil.success(pluginMessage,"录入成功",ResponseUtil.data("no",String.valueOf(m_nUserID)));
            }
            Log.i(TAG, fingerData.getMsg());
            fingerData.setBinImage(m_binImage);
            dialogHandler.obtainMessage(1, fingerData).sendToTarget();
            // dialogHandler.obtainMessage(2, fingerData).sendToTarget();
        });

    }

    /**
     * 指纹对比
     */
    public void verify(PluginMessage pluginMessage) {
        fingerData.setTips("");
        int[] w_nState = new int[1];
        if (!fingerCommon.IsInit()) {
            fingerData.setTips("请先初始化连接！");
            Log.i(TAG, fingerData.getTips());
            dialogHandler.obtainMessage(3, fingerData).sendToTarget();
            ResponseUtil.fail(pluginMessage,"请先初始化连接！");
            return;
        }
        getOneEmptyId();
        if (m_nUserID < 1) {
            Log.i(TAG, "未获取到可用id");
            ResponseUtil.fail(pluginMessage,"未获取到可用id");
            return;
        }

        int w_nRet = fingerCommon.Run_GetStatus(m_nUserID - 1, w_nState);
        if (w_nRet != FingerCommon.ERR_SUCCESS) {
            fingerData.setTips(GetErrorMsg(w_nRet));
            dialogHandler.obtainMessage(3, fingerData).sendToTarget();
            ResponseUtil.fail(pluginMessage,GetErrorMsg(w_nRet));
            return;
        }
        if (w_nState[0] == FingerCommon.GD_TEMPLATE_EMPTY) {
            fingerData.setTips("Template is empty");
            dialogHandler.obtainMessage(3, fingerData).sendToTarget();
            ResponseUtil.fail(pluginMessage,"Template is empty");
            return;
        }
        fingerData.setMsg("Press finger");
        dialogHandler.obtainMessage(1, fingerData).sendToTarget();
        fingerCommon.Run_SLEDControl(1);
        m_bCancel = false;
        executorService.execute(() -> {
            int[] w_nLearned = new int[1];
            int[] w_nWidth = new int[1];
            int[] w_nHeight = new int[1];

            if (Capturing() < 0)
                return;
            fingerData.setMsg("Release your finger.");
            dialogHandler.obtainMessage(1, fingerData).sendToTarget();
            // Up Cpatured Image
            int result = fingerCommon.Run_UpImage(0, m_binImage, w_nWidth, w_nHeight);

            if (result != FingerCommon.ERR_SUCCESS) {
                fingerData.setMsg(GetErrorMsg(result));
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                ResponseUtil.fail(pluginMessage,GetErrorMsg(w_nRet));
                return;
            }

            // Draw image
            fingerData.setWidth(w_nWidth[0]);
            fingerData.setHeight(w_nHeight[0]);
            fingerData.setBinImage(m_binImage);
            dialogHandler.obtainMessage(1, fingerData).sendToTarget();
            // Create template
            m_nPassedTime = SystemClock.elapsedRealtime();
            result = fingerCommon.Run_Generate(0);

            if (result != FingerCommon.ERR_SUCCESS) {
                fingerData.setMsg(GetErrorMsg(result));
                Log.i(TAG, fingerData.getMsg());
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                //     dialogHandler.obtainMessage(2, fingerData).sendToTarget();
                ResponseUtil.fail(pluginMessage,GetErrorMsg(w_nRet));
                return;
            }

            // Verify
            result = fingerCommon.Run_Verify(m_nUserID - 1, 0, w_nLearned);
            m_nPassedTime = SystemClock.elapsedRealtime() - m_nPassedTime;
            if (result == FingerCommon.ERR_SUCCESS){
                fingerData.setMsg(String.format("Result : Success\r\nTemplate No : %d, Learn Result : %d\r\nMatch Time : %dms", m_nUserID - 1, w_nLearned[0], m_nPassedTime));
                ResponseUtil.fail(pluginMessage,fingerData.getMsg());
            }
            else {
                fingerData.setMsg(GetErrorMsg(w_nRet));
                ResponseUtil.fail(pluginMessage,fingerData.getMsg());
            }
            dialogHandler.obtainMessage(1, fingerData).sendToTarget();
            //   dialogHandler.obtainMessage(2, fingerData).sendToTarget();
        });

    }


    private int Capturing() {
        int w_nRet;
        while (true) {
            w_nRet = fingerCommon.Run_GetImage();
            if (w_nRet == FingerCommon.ERR_CONNECTION) {
                fingerData.setMsg("Communication error! status=" + w_nRet);
                Log.i(TAG, "Capturing: 错误：" + fingerData.getMsg());
                dialogHandler.obtainMessage(1, fingerData).sendToTarget();
                //  dialogHandler.obtainMessage(2, fingerData).sendToTarget();
                return -1;
            } else if (w_nRet == FingerCommon.ERR_SUCCESS)
                break;
            if (m_bCancel) {
                StopOperation();
                return -1;
            }
        }
        return 0;
    }

    private void StopOperation() {
        fingerData.setMsg("Canceled");
        dialogHandler.obtainMessage(1, fingerData).sendToTarget();
        //    dialogHandler.obtainMessage(2, fingerData).sendToTarget();
    }

    private String GetErrorMsg(int nErrorCode) {
        String str;
        switch (nErrorCode) {
            case FingerCommon.ERR_SUCCESS:
                str = "Succcess";
                break;
            case FingerCommon.ERR_VERIFY:
                str = "Verify NG";
                break;
            case FingerCommon.ERR_IDENTIFY:
                str = "Identify NG";
                break;
            case FingerCommon.ERR_EMPTY_ID_NOEXIST:
                str = "Empty Template no Exist";
                break;
            case FingerCommon.ERR_BROKEN_ID_NOEXIST:
                str = "Broken Template no Exist";
                break;
            case FingerCommon.ERR_TMPL_NOT_EMPTY:
                str = "Template of this ID Already Exist";
                break;
            case FingerCommon.ERR_TMPL_EMPTY:
                str = "This Template is Already Empty";
                break;
            case FingerCommon.ERR_INVALID_TMPL_NO:
                str = "Invalid Template No";
                break;
            case FingerCommon.ERR_ALL_TMPL_EMPTY:
                str = "All Templates are Empty";
                break;
            case FingerCommon.ERR_INVALID_TMPL_DATA:
                str = "Invalid Template Data";
                break;
            case FingerCommon.ERR_DUPLICATION_ID:
                str = "Duplicated ID : ";
                break;
            case FingerCommon.ERR_BAD_QUALITY:
                str = "Bad Quality Image";
                break;
            case FingerCommon.ERR_MERGE_FAIL:
                str = "Merge failed";
                break;
            case FingerCommon.ERR_NOT_AUTHORIZED:
                str = "Device not authorized.";
                break;
            case FingerCommon.ERR_MEMORY:
                str = "Memory Error ";
                break;
            case FingerCommon.ERR_INVALID_PARAM:
                str = "Invalid Parameter";
                break;
            case FingerCommon.ERR_GEN_COUNT:
                str = "Generation Count is invalid";
                break;
            case FingerCommon.ERR_INVALID_BUFFER_ID:
                str = "Ram Buffer ID is invalid.";
                break;
            case FingerCommon.ERR_INVALID_OPERATION_MODE:
                str = "Invalid Operation Mode!";
                break;
            case FingerCommon.ERR_FP_NOT_DETECTED:
                str = "Finger is not detected.";
                break;
            default:
                str = String.format("Fail, error code=%s", nErrorCode);
                break;
        }
        return str;
    }


    private static class DialogHandler extends Handler {
        private WeakReference<FingerUtil> contextWeakReference;
        private ImageView m_FpImageViewer;
        private TextView m_txtStatus, m_btnCancel, mTips;
        private AlertDialog alertDialog;
        private FingerUtil fingerUtil;
        private byte[] m_bmpImage = new byte[1024 * 100];

        private DialogHandler(FingerUtil fu) {
            contextWeakReference = new WeakReference<>(fu);
            this.fingerUtil = contextWeakReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (fingerUtil == null || fingerUtil.context == null) return;
                    View view = LayoutInflater.from(fingerUtil.context).inflate(R.layout.finger_popupwindow, null, false);
                    m_FpImageViewer = view.findViewById(R.id.finger_image);
                    m_txtStatus = view.findViewById(R.id.tv_status);
                    m_btnCancel = view.findViewById(R.id.cancel);
                    mTips = view.findViewById(R.id.tips);
                    m_btnCancel.setOnClickListener((new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                               m_bCancel = true;
                            if (alertDialog != null) {
                                alertDialog.cancel();
                            }
                        }
                    }));
                    AlertDialog.Builder builder = new AlertDialog.Builder(fingerUtil.context);
                    alertDialog = builder.setView(view).create();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    break;
                case 1:
                    if (alertDialog == null) return;
                    FingerData fingerData = (FingerData) msg.obj;
                    if (fingerData == null) return;
                    m_txtStatus.setText(fingerData.getMsg());
                    if (!TextUtils.isEmpty(fingerData.getTips())) {
                        mTips.setText(fingerData.getTips());
                    } else {
                        mTips.setText("");
                    }
                    if (fingerData.getHeight() > 0 && fingerData.getWidth() > 0) {
                        //绘制图片
                        MakeBMPBuf(fingerData.getBinImage(), m_bmpImage, fingerData.getWidth(), fingerData.getHeight());
                        int nSize;
                        if ((fingerData.getWidth() % 4) != 0)
                            nSize = fingerData.getWidth() + (4 - (fingerData.getWidth() % 4));
                        else {
                            nSize = fingerData.getWidth();
                        }
                        nSize = 1078 + nSize * fingerData.getHeight();
                        //DebugManage.WriteBmp(m_bmpImage, nSize);
                        Log.i(TAG, String.format("handleMessage: 对比指纹绘制：binImage=%s bmpImage=%s width=%s height=%s nsize=%s", fingerData.getBinImage()[0], m_bmpImage[0], fingerData.getWidth(), fingerData.getHeight(), nSize));
                        Bitmap image = BitmapFactory.decodeByteArray(m_bmpImage, 0, nSize);
                        m_FpImageViewer.setImageBitmap(image);
                    } else {
                        m_FpImageViewer.setImageResource(R.drawable.finger_icon);
                    }
                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                    break;
                case 2:
                    if (fingerUtil == null || fingerUtil.context == null) return;
                    //  fingerUtil.fingerCommon.Run_SLEDControl(0);
                    break;
                case 3:
                    FingerData data = (FingerData) msg.obj;
                    if (data != null && !TextUtils.isEmpty(data.getTips())) {
                        Toast.makeText(fingerUtil.context, data.getTips(), Toast.LENGTH_LONG).show();
                    }
                    break;


            }
        }

        private void MakeBMPBuf(byte[] Input, byte[] Output, int iImageX, int iImageY) {
            byte[] w_bTemp = new byte[4];
            byte[] head = new byte[1078];
            byte[] head2 = {
                    /***************************/
                    //file header
                    0x42, 0x4d,//file type
                    //0x36,0x6c,0x01,0x00, //file size***
                    0x0, 0x0, 0x0, 0x00, //file size***
                    0x00, 0x00, //reserved
                    0x00, 0x00,//reserved
                    0x36, 0x4, 0x00, 0x00,//head byte***
                    /***************************/
                    //infoheader
                    0x28, 0x00, 0x00, 0x00,//struct size

                    //0x00,0x01,0x00,0x00,//map width***
                    0x00, 0x00, 0x0, 0x00,//map width***
                    //0x68,0x01,0x00,0x00,//map height***
                    0x00, 0x00, 0x00, 0x00,//map height***

                    0x01, 0x00,//must be 1
                    0x08, 0x00,//color count***
                    0x00, 0x00, 0x00, 0x00, //compression
                    //0x00,0x68,0x01,0x00,//data size***
                    0x00, 0x00, 0x00, 0x00,//data size***
                    0x00, 0x00, 0x00, 0x00, //dpix
                    0x00, 0x00, 0x00, 0x00, //dpiy
                    0x00, 0x00, 0x00, 0x00,//color used
                    0x00, 0x00, 0x00, 0x00,//color important
            };

            int i, j, num, iImageStep;

            Arrays.fill(w_bTemp, (byte) 0);

            System.arraycopy(head2, 0, head, 0, head2.length);

            if ((iImageX % 4) != 0)
                iImageStep = iImageX + (4 - (iImageX % 4));
            else
                iImageStep = iImageX;

            num = iImageX;
            head[18] = (byte) (num & (byte) 0xFF);
            num = num >> 8;
            head[19] = (byte) (num & (byte) 0xFF);
            num = num >> 8;
            head[20] = (byte) (num & (byte) 0xFF);
            num = num >> 8;
            head[21] = (byte) (num & (byte) 0xFF);

            num = iImageY;
            head[22] = (byte) (num & (byte) 0xFF);
            num = num >> 8;
            head[23] = (byte) (num & (byte) 0xFF);
            num = num >> 8;
            head[24] = (byte) (num & (byte) 0xFF);
            num = num >> 8;
            head[25] = (byte) (num & (byte) 0xFF);

            j = 0;
            for (i = 54; i < 1078; i = i + 4) {
                head[i] = head[i + 1] = head[i + 2] = (byte) j;
                head[i + 3] = 0;
                j++;
            }

            System.arraycopy(head, 0, Output, 0, 1078);

            if (iImageStep == iImageX) {
                for (i = 0; i < iImageY; i++) {
                    System.arraycopy(Input, i * iImageX, Output, 1078 + i * iImageX, iImageX);
                }
            } else {
                iImageStep = iImageStep - iImageX;

                for (i = 0; i < iImageY; i++) {
                    System.arraycopy(Input, i * iImageX, Output, 1078 + i * (iImageX + iImageStep), iImageX);
                    System.arraycopy(w_bTemp, 0, Output, 1078 + i * (iImageX + iImageStep) + iImageX, iImageStep);
                }
            }
        }

    }


}
