package com.ethink.vcd.finger;

import android.os.SystemClock;

import com.ethink.plugin.message.PluginMessage;
import com.ethink.tools.serialport.usb.util.HexDump;
import com.ethink.vcd.event.FingerPushMessage;
import com.ethink.vcd.utils.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FingerUtil {
    private FingerCommon fingerCommon;
    private int m_nMaxFpCount = 500;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static boolean m_bCancel = true;
    private long m_nPassedTime;
    //当前输入的id
    private int m_nUserID;
    // private DialogHandler dialogHandler;
    private ExecutorService executorService;
    private byte[] m_binImage = new byte[1024 * 100];
    //bitmap转换为string
    private String bitmap = null;
    private String testFinger = null;
    private FingerPushMessage fingerPushMessage;

    public FingerUtil(FingerPushMessage fingerPushMessage, FingerCommon fc) {
        this.fingerCommon = fc;
        this.fingerPushMessage = fingerPushMessage;
        executorService = Executors.newCachedThreadPool();
    }

    /**
     * 取消掉，可以结束whil循环
     **/
    private void cancel() {
        m_bCancel = true;
    }


    //获取可用id
    public int getOneEmptyId() {
        cancel();
        int[] w_nEmptyID = new int[1];
        int w_nRet = fingerCommon.Run_GetEmptyID(1, m_nMaxFpCount, w_nEmptyID);
        if (w_nRet != FingerCommon.ERR_SUCCESS) {
            return -1;
        }
        logger.info("可用id======" + w_nEmptyID[0]);
        m_nUserID = w_nEmptyID[0];
        return m_nUserID;
    }

    //获取模板总数
    public void templateTotal() {
        cancel();
        int w_nRet;
        int[] w_nEnrollCount = new int[1];
        w_nRet = fingerCommon.Run_GetEnrollCount(1, m_nMaxFpCount, w_nEnrollCount);
        logger.info("模板res{} ",w_nRet);
        if (w_nRet != FingerCommon.ERR_SUCCESS) {
            //显示错误信息
            logger.error("获取失败" + GetErrorMsg(w_nRet));
            fingerPushMessage.message("获取失败" + GetErrorMsg(w_nRet));
        } else {
            logger.info("模板总数======" + w_nEnrollCount[0]);
            fingerPushMessage.pushData("成功", String.valueOf(w_nEnrollCount[0]), "/");
        }
    }

    public void close() {
        if (fingerCommon != null) {
            fingerCommon.CloseComm();
            fingerPushMessage.message("关闭成功");
        } else {
            fingerPushMessage.message("关闭失败");
        }

    }

    /*
     * 下载指纹到模块
     * */
    public void downloadChar() {
        //      byte[] p_pbyTemplate=   fingerCommon.toByteArray(testFinger);
        byte[] p_pbyTemplate = HexDump.hexStringToByteArray(testFinger);
        int ret = fingerCommon.Run_DownChar(1, p_pbyTemplate);
        if (ret != FingerCommon.ERR_SUCCESS) {
            logger.info("下载失败");
            return;
        }
        int id = getOneEmptyId();
        if (id < 0) {
            logger.info("获取空id失败");
            return;
        }
        int[] w_nDupID = new int[1];
        int re = fingerCommon.Run_StoreChar(id, 1, w_nDupID);
        if (re == FingerCommon.ERR_SUCCESS) {
            logger.info("下载成功{}", w_nDupID[0]);
        } else
            logger.info("下载失败");
    }

    //下载指纹数据到设备
    public void download() {


    }
    //验证指定指纹
    public void verify(){

    }
    //删除指定指纹
    public void delOne(){

    }

    public void search() {
        cancel();
        m_bCancel = false;
        executorService.execute(() -> {
            int[] w_nID = new int[1];
            int[] w_nLearned = new int[1];
            while (true) {
                fingerPushMessage.message("请按下手指");
                if (Capturing() < 0) return;
                fingerPushMessage.message("请抬起手指");
                // Create template
                m_nPassedTime = SystemClock.elapsedRealtime();
                int w_nRet = fingerCommon.Run_Generate(0);
                if (w_nRet != FingerCommon.ERR_SUCCESS) {
                    fingerPushMessage.message(GetErrorMsg(w_nRet));
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
                if (w_nRet == FingerCommon.ERR_SUCCESS) {
                    fingerPushMessage.pushData("验证通过", "不返回指纹数据", String.valueOf(w_nID[0]));
                } else {
                    String m_strPost = GetErrorMsg(w_nRet) + String.format("Match Time : %sms", m_nPassedTime);
                    fingerPushMessage.message(m_strPost);
                }
                break;
            }

        });


    }


    public void delTemplateAll() {
        int w_nRet;
        cancel();
        w_nRet = fingerCommon.Run_DelChar(1, m_nMaxFpCount);
        if (w_nRet != FingerCommon.ERR_SUCCESS) {
            fingerPushMessage.message(GetErrorMsg(w_nRet));
        } else {
            fingerPushMessage.message("Delete all OK !");
        }

    }


    //注册指纹
    public void register() {
        cancel();
        int[] w_nState = new int[1];
        int emptyId = getOneEmptyId();
        if (emptyId < 1) {
            logger.info("未获取到可用id");
            fingerPushMessage.message("未获取到可用id");
            return;
        }
        int w_nRet = fingerCommon.Run_GetStatus(emptyId, w_nState);
        if (w_nRet != FingerCommon.ERR_SUCCESS) {
            logger.info(GetErrorMsg(w_nRet));
            //显示错误信息
            fingerPushMessage.message(GetErrorMsg(w_nRet));
            return;
        }
        if (w_nState[0] == FingerCommon.GD_TEMPLATE_NOT_EMPTY) {
            logger.info("Template is already exist");
            //显示错误信息
            fingerPushMessage.message("Template is already exist");
            return;
        }
        m_bCancel = false;
        executorService.execute(() -> {
            int w_nUserID, result, w_nEnrollStep = 0, w_nGenCount = 3;
            int[] w_nDupID = new int[1];
            w_nUserID = emptyId;
            while (w_nEnrollStep < w_nGenCount) {
                logger.debug(String.format("请按下手指，第(%s/%s)次!", (w_nEnrollStep + 1), w_nGenCount));
                fingerPushMessage.message(String.format("请按下手指，第(%s/%s)次!", (w_nEnrollStep + 1), w_nGenCount));
                // Capture
                if (Capturing() < 0) {
                    fingerPushMessage.message("采集图像失败！");
                    return;
                }
                fingerPushMessage.message("请抬起手指！");
                // Create Template
                result = fingerCommon.Run_Generate(w_nEnrollStep);
                logger.info("创建模板次数：{}", w_nEnrollStep);
                if (result != FingerCommon.ERR_SUCCESS) {
                    if (result == FingerCommon.ERR_BAD_QUALITY) {
                        fingerPushMessage.message("Bad quality. Try Again!");
                        continue;
                    } else {
                        fingerPushMessage.message(GetErrorMsg(result));
                        return;
                    }
                }
                w_nEnrollStep++;
            }
            logger.info("run: --------------------执行完毕");
            //. Merge Template
            result = fingerCommon.Run_Merge(0, w_nGenCount);
            if (result != FingerCommon.ERR_SUCCESS) {
                fingerPushMessage.message(GetErrorMsg(result));
                logger.info("run:失败原因：" + GetErrorMsg(result));
                return;
            }
            logger.info("run: 存储指纹数据------");
            //. Store template
            result = fingerCommon.Run_StoreChar(w_nUserID, 0, w_nDupID);
            if (result != FingerCommon.ERR_SUCCESS) {
                if (result == FingerCommon.ERR_DUPLICATION_ID) {
                    fingerPushMessage.message(String.format("Result : Fail\r\nDuplication ID = %s", w_nDupID[0]));
                } else {
                    fingerPushMessage.message(GetErrorMsg(result));
                }
            } else {
                //录入成功->下载指纹特征
                byte[] p_pbyTemplate = new byte[512];
                int re = fingerCommon.Run_UpChar(1, p_pbyTemplate);
                //  int re = fingerCommon.Run_DownChar(1, p_pbyTemplate);
                if (re == FingerCommon.ERR_SUCCESS) {
                    //    testFinger=fingerCommon.byte2HexStr(p_pbyTemplate,498);
                    logger.info("指纹数据2： \n" + HexDump.toHexString(p_pbyTemplate));
                    fingerPushMessage.pushData("录入成功", HexDump.toHexString(p_pbyTemplate), String.valueOf(m_nUserID));
                } else fingerPushMessage.message("上传模板错误：" + GetErrorMsg(re));
            }
        });

    }

    private int Capturing() {
        int w_nRet;
        while (true) {
            w_nRet = fingerCommon.Run_GetImage();
            if (w_nRet == FingerCommon.ERR_CONNECTION) {
                return -1;
            } else if (w_nRet == FingerCommon.ERR_SUCCESS)
                break;
            if (m_bCancel) {
                return -1;
            }
        }
        return 0;
    }


    //检测手指
    private int fingerDetect() {
        int[] w_nState = new int[1];
        while (true) {
            int re = fingerCommon.Run_FingerDetect(w_nState);
            logger.info("re ={},w_nState={}", re, w_nState[0]);
            if (re == FingerCommon.ERR_SUCCESS && w_nState[0] == 1) {
                //检测到手指
                //   int res = fingerCommon.Run_TestConnection();
                logger.info("测试结果");
                break;
            }
            if (m_bCancel) {
                return -1;
            }
        }
        return 0;
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


}
