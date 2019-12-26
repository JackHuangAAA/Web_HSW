package com.ethink.vcd.controller;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.SystemClock;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

public class USBFingerCommon   {

    public static final int GD_RECORD_SIZE = 498;
    public static final int GD_MAX_RECORD_COUNT = 2000;
    public static final int ID_NOTE_SIZE = 64;
    public static final int MODULE_SN_LEN = 16;
    private static final int SCSI_TIMEOUT = 5000;
    private static final int COMM_SLEEP_TIME = 40;
    private static final int CMD_PACKET_LEN = 26;
    private static final int RCM_PACKET_LEN = 26;
    private static final int RCM_DATA_OFFSET = 10;
    private static final int CMD_PREFIX_CODE = 43605;
    private static final int CMD_DATA_PREFIX_CODE = 42330;
    private static final int RCM_PREFIX_CODE = 21930;
    private static final int RCM_DATA_PREFIX_CODE = 23205;
    private static final short CMD_TEST_CONNECTION = 1;
    private static final short CMD_SET_PARAM = 2;
    private static final short CMD_GET_PARAM = 3;
    private static final short CMD_GET_DEVICE_INFO = 4;
    private static final short CMD_ENTER_ISPMODE = 5;
    private static final short CMD_SET_ID_NOTE = 6;
    private static final short CMD_GET_ID_NOTE = 7;
    private static final short CMD_SET_MODULE_SN = 8;
    private static final short CMD_GET_MODULE_SN = 9;
    private static final short CMD_GET_IMAGE = 32;
    private static final short CMD_FINGER_DETECT = 33;
    private static final short CMD_UP_IMAGE = 34;
    private static final short CMD_DOWN_IMAGE = 35;
    private static final short CMD_SLED_CTRL = 36;
    private static final short CMD_STORE_CHAR = 64;
    private static final short CMD_LOAD_CHAR = 65;
    private static final short CMD_UP_CHAR = 66;
    private static final short CMD_DOWN_CHAR = 67;
    private static final short CMD_DEL_CHAR = 68;
    private static final short CMD_GET_EMPTY_ID = 69;
    private static final short CMD_GET_STATUS = 70;
    private static final short CMD_GET_BROKEN_ID = 71;
    private static final short CMD_GET_ENROLL_COUNT = 72;
    private static final short CMD_GENERATE = 96;
    private static final short CMD_MERGE = 97;
    private static final short CMD_MATCH = 98;
    private static final short CMD_SEARCH = 99;
    private static final short CMD_VERIFY = 100;
    private static final short RCM_INCORRECT_COMMAND = 255;
    public static final int ERR_SUCCESS = 0;
    public static final int ERR_FAIL = 1;
    public static final int ERR_CONNECTION = 2;
    public static final int ERR_VERIFY = 16;
    public static final int ERR_IDENTIFY = 17;
    public static final int ERR_TMPL_EMPTY = 18;
    public static final int ERR_TMPL_NOT_EMPTY = 19;
    public static final int ERR_ALL_TMPL_EMPTY = 20;
    public static final int ERR_EMPTY_ID_NOEXIST = 21;
    public static final int ERR_BROKEN_ID_NOEXIST = 22;
    public static final int ERR_INVALID_TMPL_DATA = 23;
    public static final int ERR_DUPLICATION_ID = 24;
    public static final int ERR_BAD_QUALITY = 25;
    public static final int ERR_MERGE_FAIL = 26;
    public static final int ERR_NOT_AUTHORIZED = 27;
    public static final int ERR_MEMORY = 28;
    public static final int ERR_INVALID_TMPL_NO = 29;
    public static final int ERR_INVALID_PARAM = 34;
    public static final int ERR_GEN_COUNT = 37;
    public static final int ERR_INVALID_BUFFER_ID = 38;
    public static final int ERR_INVALID_OPERATION_MODE = 39;
    public static final int ERR_FP_NOT_DETECTED = 40;
    public static final int DP_DEVICE_ID = 0;
    public static final int DP_SECURITY_LEVEL = 1;
    public static final int DP_DUP_CHECK = 2;
    public static final int DP_BAUDRATE = 3;
    public static final int DP_AUTO_LEARN = 4;
    public static final int MIN_DEVICE_ID = 1;
    public static final int MAX_DEVICE_ID = 255;
    public static final int MIN_SECURITY_LEVEL = 1;
    public static final int MAX_SECURITY_LEVEL = 5;
    public static final int GD_TEMPLATE_NOT_EMPTY = 1;
    public static final int GD_TEMPLATE_EMPTY = 0;
    public int m_nPacketSize;
    public byte m_bySrcDeviceID = 1;
    public byte m_byDstDeviceID = 1;
    public byte[] m_abyPacket = new byte[65536];
    public byte[] m_abyPacket2 = new byte[65536];
    private USBDeviceFace usbDeviceFace;

    private Context context;
    private   UsbManager mUsbManager;
    private int m_nEPInSize;
    private int m_nEPOutSize;
    private byte[] m_abyTransferBuf;
    private boolean m_bInit = false;
    private UsbDevice usbDevice;
    private UsbDeviceConnection m_usbConn = null;
    private UsbInterface m_usbIf = null;
    private UsbEndpoint m_epIN = null;
    private UsbEndpoint m_epOUT = null;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
    private static final String TAG = "USBController";




    public USBFingerCommon(Context context,USBDeviceFace usbDeviceFace) {
        this.context=context;
        this.usbDeviceFace = usbDeviceFace;
        this.mUsbManager = (UsbManager) this.context.getSystemService(Context.USB_SERVICE);
        this.m_abyTransferBuf = new byte[512];
    }

    public void uninit() {
        if (this.m_usbConn != null) {
            this.m_usbConn.releaseInterface(this.m_usbIf);
            this.m_usbConn.close();
            this.m_usbConn = null;
            this.m_bInit = false;
        }

    }



    public boolean IsInit() {
        return this.m_bInit;
    }





    /***
     *
     * 申请USB权限
     * **/
    public void requestUsb() {
        HashMap<String, UsbDevice> deviceMap = mUsbManager.getDeviceList();
        for (UsbDevice device : deviceMap.values()) {
            if (usbDeviceFace != null) {
                logger.info("usb device vid={}  pid={} name={}", device.getVendorId(), device.getProductId(), device.getProductName());
                if (usbDeviceFace.checkUSB(device)) {
                    this.usbDevice = device;
                    Log.i(TAG, "找到所需要的USB设备");
                    break;
                }
            }
        }
        if (usbDevice != null) {
            //开始申请权限
            if (mUsbManager.hasPermission(usbDevice)) {
                //已拥有权限
                this.usbDeviceFace.openUSB(usbDevice);
              //  connectInterface(usbDevice);
            } else {
                //申请权限
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                context.registerReceiver(mUsbReceiver, filter);
                mUsbManager.requestPermission(usbDevice, pendingIntent); // 该代码执行后，系统弹出一个对话框
            }

        }
    }
    public void connectInterface(UsbDevice dev) {
        this.usbDevice = dev;
        this.m_usbConn = this.mUsbManager.openDevice(dev);
        int n = dev.getInterfaceCount();
        if (n > 0) {
            if (this.m_usbConn.claimInterface(dev.getInterface(0), true)) {
                this.m_usbIf = dev.getInterface(0);
                n = this.m_usbIf.getEndpointCount();
                if (n >= 2) {
                    for (int i = 0; i < n; ++i) {
                        if (this.m_usbIf.getEndpoint(i).getType() == 2) {
                            if (this.m_usbIf.getEndpoint(i).getDirection() == 128) {
                                this.m_epIN = this.m_usbIf.getEndpoint(i);
                            } else {
                                this.m_epOUT = this.m_usbIf.getEndpoint(i);
                            }
                        }
                    }

                    this.m_nEPInSize = this.m_epIN.getMaxPacketSize();
                    this.m_nEPOutSize = this.m_epOUT.getMaxPacketSize();
                    this.m_bInit = true;
                }
            }
        }
    }
    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "申请权限receiver action: " + action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                context.unregisterReceiver(mUsbReceiver);
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    Log.i(TAG, "申请权限成功------");
                    // 是我们所需要的设备
                    if (usbDeviceFace != null && usbDeviceFace.checkUSB(device)) {
                        usbDeviceFace.openUSB(device);
                      //  connectInterface(device);
                    }
                } else {
                    //申请权限失败
                    Log.i(TAG, "申请权限失败了------");
                    if (usbDeviceFace != null) {
                        m_bInit = false;
                        usbDeviceFace.deniedPermission();
                    }
                }

            }
        }
    };

    public int Run_TestConnection() {
        this.InitCmdPacket((short) 1, this.m_bySrcDeviceID, this.m_byDstDeviceID, this.m_abyPacket2, 0);
        boolean w_bRet = this.USB_SendPacket((short) 1);
        return !w_bRet ? 2 : this.GetRetCode();
    }

    public int Run_SetParam(int p_nParamIndex, int p_nParamValue) {
        byte[] w_abyData = new byte[]{(byte) p_nParamIndex, (byte) (p_nParamValue & 255), (byte) ((p_nParamValue & '\uff00') >> 8), (byte) ((p_nParamValue & 16711680) >> 16), (byte) ((p_nParamValue & -16777216) >> 24)};
        this.InitCmdPacket((short) 2, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 5);
        boolean w_bRet = this.USB_SendPacket((short) 2);
        return !w_bRet ? 2 : this.GetRetCode();
    }

    public int Run_GetParam(int p_nParamIndex, int[] p_pnParamValue) {
        byte[] w_abyData = new byte[]{(byte) p_nParamIndex};
        this.InitCmdPacket((short) 3, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 1);
        boolean w_bRet = this.USB_SendPacket((short) 3);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            p_pnParamValue[0] = this.m_abyPacket[13] << 24 & -16777216 | this.m_abyPacket[12] << 16 & 16711680 | this.m_abyPacket[11] << 8 & '\uff00' | this.m_abyPacket[10] & 255;
            return 0;
        }
    }

    public int Run_GetDeviceInfo(String[] p_szDevInfo) {
        this.InitCmdPacket((short) 4, this.m_bySrcDeviceID, this.m_byDstDeviceID, this.m_abyPacket2, 0);
        boolean w_bRet = this.USB_SendPacket((short) 4);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            int w_nDevInfoLen = this.MAKEWORD(this.m_abyPacket[10], this.m_abyPacket[11]);
            w_bRet = this.USB_ReceiveDataPacket((short) 4);
            if (!w_bRet) {
                return 2;
            } else if (this.GetRetCode() != 0) {
                return this.GetRetCode();
            } else {
                this.memset(this.m_abyPacket2, (byte) 0, 512);
                System.arraycopy(this.m_abyPacket, 10, this.m_abyPacket2, 0, w_nDevInfoLen);
                String w_strTmp = new String(this.m_abyPacket2);
                p_szDevInfo[0] = w_strTmp;
                return 0;
            }
        }
    }

    public int Run_SetIDNote(int p_nTmplNo, String p_pstrNote) {
        boolean w_bRet = false;
        byte[] w_abyData = new byte[66];
        byte[] w_abyData2 = new byte[2];
        byte[] w_abyNoteBuf = p_pstrNote.getBytes();
        w_abyData2[0] = this.LOBYTE((short) 66);
        w_abyData2[1] = this.HIBYTE((short) 66);
        this.InitCmdPacket((short) 6, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData2, 2);
        w_bRet = this.USB_SendPacket((short) 6);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            this.memset(w_abyData, (byte) 0, 66);
            w_abyData[0] = this.LOBYTE((short) p_nTmplNo);
            w_abyData[1] = this.HIBYTE((short) p_nTmplNo);
            System.arraycopy(w_abyNoteBuf, 0, w_abyData, 2, w_abyNoteBuf.length);
            this.InitCmdDataPacket((short) 6, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 66);
            w_bRet = this.USB_SendDataPacket((short) 6);
            return !w_bRet ? 2 : this.GetRetCode();
        }
    }

    public int Run_GetIDNote(int p_nTmplNo, String[] p_pstrNote) {
        boolean w_bRet = false;
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nTmplNo), this.HIBYTE((short) p_nTmplNo)};
        this.InitCmdPacket((short) 7, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 2);
        w_bRet = this.USB_SendPacket((short) 7);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            w_bRet = this.USB_ReceiveDataPacket((short) 7);
            if (!w_bRet) {
                return 2;
            } else if (this.GetRetCode() != 0) {
                return this.GetRetCode();
            } else {
                this.memset(this.m_abyPacket2, (byte) 0, 512);
                System.arraycopy(this.m_abyPacket, 10, this.m_abyPacket2, 0, 64);
                String w_strTmp = new String(this.m_abyPacket2);
                p_pstrNote[0] = w_strTmp;
                return 0;
            }
        }
    }

    public int Run_SetModuleSN(String p_pstrModuleSN) {
        boolean w_bRet = false;
        byte[] w_abyData = p_pstrModuleSN.getBytes();
        byte[] w_abyModuleSN = new byte[16];
        byte[] w_abyData2 = new byte[2];
        this.memset(w_abyModuleSN, (byte) 0, 16);
        System.arraycopy(w_abyData, 0, w_abyModuleSN, 0, w_abyData.length);
        w_abyData2[0] = this.LOBYTE((short) 16);
        w_abyData2[1] = this.HIBYTE((short) 16);
        this.InitCmdPacket((short) 8, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData2, 2);
        w_bRet = this.USB_SendPacket((short) 8);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            this.InitCmdDataPacket((short) 8, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyModuleSN, 16);
            w_bRet = this.USB_SendDataPacket((short) 8);
            return !w_bRet ? 2 : this.GetRetCode();
        }
    }

    public int Run_GetModuleSN(String[] p_pstrModuleSN) {
        boolean w_bRet = false;
        this.InitCmdPacket((short) 9, this.m_bySrcDeviceID, this.m_byDstDeviceID, this.m_abyPacket2, 0);
        w_bRet = this.USB_SendPacket((short) 9);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            w_bRet = this.USB_ReceiveDataPacket((short) 9);
            if (!w_bRet) {
                return 2;
            } else if (this.GetRetCode() != 0) {
                return this.GetRetCode();
            } else {
                this.memset(this.m_abyPacket2, (byte) 0, 512);
                System.arraycopy(this.m_abyPacket, 10, this.m_abyPacket2, 0, 16);
                String w_strTmp = new String(this.m_abyPacket2);
                p_pstrModuleSN[0] = w_strTmp;
                return 0;
            }
        }
    }

    public int Run_GetImage() {
        this.InitCmdPacket((short) 32, this.m_bySrcDeviceID, this.m_byDstDeviceID, this.m_abyPacket2, 0);
        boolean w_bRet = this.USB_SendPacket((short) 32);
        return !w_bRet ? 2 : this.GetRetCode();
    }

    public int Run_FingerDetect(int[] p_pnDetectResult) {
        this.InitCmdPacket((short) 33, this.m_bySrcDeviceID, this.m_byDstDeviceID, this.m_abyPacket2, 0);
        boolean w_bRet = this.USB_SendPacket((short) 33);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            p_pnDetectResult[0] = this.m_abyPacket[10];
            return 0;
        }
    }

    public int Run_UpImage(int p_nType, byte[] p_pFpData, int[] p_pnImgWidth, int[] p_pnImgHeight) {
        byte[] w_abyData = new byte[]{(byte) p_nType};
        this.InitCmdPacket((short) 34, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 1);
        boolean w_bRet = this.USB_SendPacket((short) 34);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            int w = this.MAKEWORD(this.m_abyPacket[10], this.m_abyPacket[11]);
            int h = this.MAKEWORD(this.m_abyPacket[12], this.m_abyPacket[13]);
            w_bRet = this.USB_ReceiveImage(p_pFpData, w * h);
            if (!w_bRet) {
                return 2;
            } else {
                p_pnImgWidth[0] = w;
                p_pnImgHeight[0] = h;
                return 0;
            }
        }
    }

    public int Run_DownImage(byte[] p_pData, int p_nWidth, int p_nHeight) {
        return 0;
    }

    public int Run_SLEDControl(int p_nState) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nState), this.HIBYTE((short) p_nState)};
        this.InitCmdPacket((short) 36, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 2);
        boolean w_bRet = this.USB_SendPacket((short) 36);
        return !w_bRet ? 2 : this.GetRetCode();
    }

    public int Run_StoreChar(int p_nTmplNo, int p_nRamBufferID, int[] p_pnDupTmplNo) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nTmplNo), this.HIBYTE((short) p_nTmplNo), this.LOBYTE((short) p_nRamBufferID), this.HIBYTE((short) p_nRamBufferID)};
        this.InitCmdPacket((short) 64, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 4);
        boolean w_bRet = this.USB_SendPacket((short) 64);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            if (this.GetRetCode() == 24) {
                p_pnDupTmplNo[0] = this.MAKEWORD(this.m_abyPacket[10], this.m_abyPacket[11]);
            }

            return this.GetRetCode();
        } else {
            return this.GetRetCode();
        }
    }

    public int Run_LoadChar(int p_nTmplNo, int p_nRamBufferID) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nTmplNo), this.HIBYTE((short) p_nTmplNo), this.LOBYTE((short) p_nRamBufferID), this.HIBYTE((short) p_nRamBufferID)};
        this.InitCmdPacket((short) 65, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 4);
        boolean w_bRet = this.USB_SendPacket((short) 65);
        return !w_bRet ? 2 : this.GetRetCode();
    }

    public int Run_UpChar(int p_nRamBufferID, byte[] p_pbyTemplate) {
        boolean w_bRet = false;
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nRamBufferID), this.HIBYTE((short) p_nRamBufferID)};
        this.InitCmdPacket((short) 66, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 2);
        w_bRet = this.USB_SendPacket((short) 66);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            w_bRet = this.USB_ReceiveDataPacket((short) 66);
            if (!w_bRet) {
                return 2;
            } else if (this.GetRetCode() != 0) {
                return this.GetRetCode();
            } else {
                System.arraycopy(this.m_abyPacket, 10, p_pbyTemplate, 0, 498);
                return 0;
            }
        }
    }

    public int Run_DownChar(int p_nRamBufferID, byte[] p_pbyTemplate) {
        boolean w_bRet = false;
        byte[] w_abyData = new byte[500];
        byte[] w_abyData2 = new byte[]{this.LOBYTE((short) 500), this.HIBYTE((short) 500)};
        this.InitCmdPacket((short) 67, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData2, 2);
        w_bRet = this.USB_SendPacket((short) 67);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            w_abyData[0] = this.LOBYTE((short) p_nRamBufferID);
            w_abyData[1] = this.HIBYTE((short) p_nRamBufferID);
            System.arraycopy(p_pbyTemplate, 0, w_abyData, 2, 498);
            this.InitCmdDataPacket((short) 67, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 500);
            w_bRet = this.USB_SendDataPacket((short) 67);
            return !w_bRet ? 2 : this.GetRetCode();
        }
    }

    public int Run_DelChar(int p_nSTmplNo, int p_nETmplNo) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nSTmplNo), this.HIBYTE((short) p_nSTmplNo), this.LOBYTE((short) p_nETmplNo), this.HIBYTE((short) p_nETmplNo)};
        this.InitCmdPacket((short) 68, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 4);
        boolean w_bRet = this.USB_SendPacket((short) 68);
        return !w_bRet ? 2 : this.GetRetCode();
    }

    public int Run_GetEmptyID(int p_nSTmplNo, int p_nETmplNo, int[] p_pnEmptyID) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nSTmplNo), this.HIBYTE((short) p_nSTmplNo), this.LOBYTE((short) p_nETmplNo), this.HIBYTE((short) p_nETmplNo)};
        this.InitCmdPacket((short) 69, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 4);
        boolean w_bRet = this.USB_SendPacket((short) 69);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            p_pnEmptyID[0] = this.MAKEWORD(this.m_abyPacket[10], this.m_abyPacket[11]);
            return 0;
        }
    }

    public int Run_GetStatus(int p_nTmplNo, int[] p_pnStatus) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nTmplNo), this.HIBYTE((short) p_nTmplNo)};
        this.InitCmdPacket((short) 70, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 2);
        boolean w_bRet = this.USB_SendPacket((short) 70);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            p_pnStatus[0] = this.m_abyPacket[10];
            return 0;
        }
    }

    public int Run_GetBrokenID(int p_nSTmplNo, int p_nETmplNo, int[] p_pnCount, int[] p_pnFirstID) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nSTmplNo), this.HIBYTE((short) p_nSTmplNo), this.LOBYTE((short) p_nETmplNo), this.HIBYTE((short) p_nETmplNo)};
        this.InitCmdPacket((short) 71, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 4);
        boolean w_bRet = this.USB_SendPacket((short) 71);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            p_pnCount[0] = this.MAKEWORD(this.m_abyPacket[10], this.m_abyPacket[11]);
            p_pnFirstID[0] = this.MAKEWORD(this.m_abyPacket[12], this.m_abyPacket[13]);
            return 0;
        }
    }

    public int Run_GetEnrollCount(int p_nSTmplNo, int p_nETmplNo, int[] p_pnEnrollCount) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nSTmplNo), this.HIBYTE((short) p_nSTmplNo), this.LOBYTE((short) p_nETmplNo), this.HIBYTE((short) p_nETmplNo)};
        this.InitCmdPacket((short) 72, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 4);
        boolean w_bRet = this.USB_SendPacket((short) 72);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            p_pnEnrollCount[0] = this.MAKEWORD(this.m_abyPacket[10], this.m_abyPacket[11]);
            return 0;
        }
    }

    public int Run_Generate(int p_nRamBufferID) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nRamBufferID), this.HIBYTE((short) p_nRamBufferID)};
        this.InitCmdPacket((short) 96, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 2);
        boolean w_bRet = this.USB_SendPacket((short) 96);
        return !w_bRet ? 2 : this.GetRetCode();
    }

    public int Run_Merge(int p_nRamBufferID, int p_nMergeCount) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nRamBufferID), this.HIBYTE((short) p_nRamBufferID), (byte) p_nMergeCount};
        this.InitCmdPacket((short) 97, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 3);
        boolean w_bRet = this.USB_SendPacket((short) 97);
        return !w_bRet ? 2 : this.GetRetCode();
    }

    public int Run_Match(int p_nRamBufferID0, int p_nRamBufferID1) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nRamBufferID0), this.HIBYTE((short) p_nRamBufferID0), this.LOBYTE((short) p_nRamBufferID1), this.HIBYTE((short) p_nRamBufferID1)};
        this.InitCmdPacket((short) 98, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 4);
        boolean w_bRet = this.USB_SendPacket((short) 98);
        return !w_bRet ? 2 : this.GetRetCode();
    }

    public int Run_Search(int p_nRamBufferID, int p_nStartID, int p_nSearchCount, int[] p_pnTmplNo, int[] p_pnLearnResult) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nRamBufferID), this.HIBYTE((short) p_nRamBufferID), this.LOBYTE((short) p_nStartID), this.HIBYTE((short) p_nStartID), this.LOBYTE((short) p_nSearchCount), this.HIBYTE((short) p_nSearchCount)};
        this.InitCmdPacket((short) 99, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 6);
        boolean w_bRet = this.USB_SendPacket((short) 99);
        if (!w_bRet) {
            return 2;
        } else if (this.GetRetCode() != 0) {
            return this.GetRetCode();
        } else {
            p_pnTmplNo[0] = this.MAKEWORD(this.m_abyPacket[10], this.m_abyPacket[11]);
            p_pnLearnResult[0] = this.m_abyPacket[12];
            return this.GetRetCode();
        }
    }

    public int Run_Verify(int p_nTmplNo, int p_nRamBufferID, int[] p_pnLearnResult) {
        byte[] w_abyData = new byte[]{this.LOBYTE((short) p_nTmplNo), this.HIBYTE((short) p_nTmplNo), this.LOBYTE((short) p_nRamBufferID), this.HIBYTE((short) p_nRamBufferID)};
        this.InitCmdPacket((short) 100, this.m_bySrcDeviceID, this.m_byDstDeviceID, w_abyData, 4);
        boolean w_bRet = this.USB_SendPacket((short) 100);
        if (!w_bRet) {
            return 2;
        } else {
            p_pnLearnResult[0] = this.m_abyPacket[12];
            return this.GetRetCode();
        }
    }

    public boolean GetDeviceInformation(String[] deviceInfo) {
        int[] w_nRecvLen = new int[1];
        byte[] w_abyPCCmd = new byte[6];
        byte[] w_abyData = new byte[32];
        Arrays.fill(w_abyPCCmd, (byte) 0);
        w_abyPCCmd[2] = 4;
        boolean w_bRet = this.SendPackage(w_abyPCCmd, w_abyData);
        if (!w_bRet) {
            return false;
        } else {
            w_bRet = this.RecvPackage(w_abyData, w_nRecvLen);
            if (!w_bRet) {
                return false;
            } else {
                String w_strTmp = new String(w_abyData);
                deviceInfo[0] = w_strTmp;
                return true;
            }
        }
    }

    private boolean SendPackage(byte[] pPCCmd, byte[] pData) {
        pPCCmd[0] = -17;
        pPCCmd[1] = 1;
        int nDataLen = pPCCmd[5] << 8 & '\uff00' | pPCCmd[4] & 255;
        return this.UsbSCSIWrite(pPCCmd, 6, pData, nDataLen, 5000);
    }

    private boolean RecvPackage(byte[] pData, int[] pLevRen) {
        byte[] w_abyPCCmd = new byte[6];
        byte[] w_abyRespond = new byte[4];
        w_abyPCCmd[0] = -17;
        w_abyPCCmd[1] = 2;
        w_abyPCCmd[2] = 0;
        w_abyPCCmd[3] = 0;
        w_abyPCCmd[4] = 0;
        w_abyPCCmd[5] = 0;
        boolean w_bRet = this.UsbSCSIRead(w_abyPCCmd, 6, w_abyRespond, 4, 5000);
        if (!w_bRet) {
            return false;
        } else {
            int w_nLen = w_abyRespond[3] << 8 & '\uff00' | w_abyRespond[2] & 255;
            if (w_nLen > 0) {
                w_abyPCCmd[1] = 3;
                w_bRet = this.UsbSCSIRead(w_abyPCCmd, 6, pData, w_nLen, 5000);
                if (!w_bRet) {
                    return false;
                }

                pLevRen[0] = w_nLen;
            }

            return true;
        }
    }

    private short GetRetCode() {
        return (short) (this.m_abyPacket[9] << 8 & '\uff00' | this.m_abyPacket[8] & 255);
    }

    private short CalcChkSumOfPkt(byte[] pDataPkt, int nSize) {
        int nChkSum = 0;

        for (int i = 0; i < nSize; ++i) {
            if (pDataPkt[i] < 0) {
                nChkSum += pDataPkt[i] + 256;
            } else {
                nChkSum += pDataPkt[i];
            }
        }

        return (short) nChkSum;
    }

    void InitCmdPacket(short wCMDCode, byte bySrcDeviceID, byte byDstDeviceID, byte[] pbyData, int nDataLen) {
        this.memset(this.m_abyPacket, (byte) 0, 26);
        this.m_abyPacket[0] = 85;
        this.m_abyPacket[1] = -86;
        this.m_abyPacket[2] = bySrcDeviceID;
        this.m_abyPacket[3] = byDstDeviceID;
        this.m_abyPacket[4] = (byte) (wCMDCode & 255);
        this.m_abyPacket[5] = (byte) (wCMDCode >> 8 & 255);
        this.m_abyPacket[6] = (byte) (nDataLen & 255);
        this.m_abyPacket[7] = (byte) (nDataLen >> 8 & 255);
        if (nDataLen > 0) {
            System.arraycopy(pbyData, 0, this.m_abyPacket, 8, nDataLen);
        }

        short w_wCheckSum = this.CalcChkSumOfPkt(this.m_abyPacket, 24);
        this.m_abyPacket[24] = (byte) (w_wCheckSum & 255);
        this.m_abyPacket[25] = (byte) (w_wCheckSum >> 8 & 255);
        this.m_nPacketSize = 26;
    }

    void InitCmdDataPacket(short wCMDCode, byte bySrcDeviceID, byte byDstDeviceID, byte[] pbyData, int nDataLen) {
        this.m_abyPacket[0] = 90;
        this.m_abyPacket[1] = -91;
        this.m_abyPacket[2] = bySrcDeviceID;
        this.m_abyPacket[3] = byDstDeviceID;
        this.m_abyPacket[4] = (byte) (wCMDCode & 255);
        this.m_abyPacket[5] = (byte) (wCMDCode >> 8 & 255);
        this.m_abyPacket[6] = (byte) (nDataLen & 255);
        this.m_abyPacket[7] = (byte) (nDataLen >> 8 & 255);
        System.arraycopy(pbyData, 0, this.m_abyPacket, 8, nDataLen);
        short w_wCheckSum = this.CalcChkSumOfPkt(this.m_abyPacket, nDataLen + 8);
        this.m_abyPacket[nDataLen + 8] = (byte) (w_wCheckSum & 255);
        this.m_abyPacket[nDataLen + 9] = (byte) (w_wCheckSum >> 8 & 255);
        this.m_nPacketSize = nDataLen + 10;
    }

    boolean CheckReceive(byte[] pbyPacket, int nPacketLen, short wPrefix, short wCMDCode) {
        short w_wTmp = (short) (pbyPacket[1] << 8 & '\uff00' | pbyPacket[0] & 255);
        if (wPrefix != w_wTmp) {
            return false;
        } else {
            short w_wCheckSum = (short) (pbyPacket[nPacketLen - 1] << 8 & '\uff00' | pbyPacket[nPacketLen - 2] & 255);
            short w_wCalcCheckSum = this.CalcChkSumOfPkt(pbyPacket, nPacketLen - 2);
            if (w_wCheckSum != w_wCalcCheckSum) {
                return false;
            } else {
                w_wTmp = (short) (pbyPacket[5] << 8 & '\uff00' | pbyPacket[4] & 255);
                return wCMDCode == w_wTmp;
            }
        }
    }

    private boolean USB_SendPacket(short wCMD) {
        byte[] btCDB = new byte[8];
        Arrays.fill(btCDB, (byte) 0);
        btCDB[0] = -17;
        btCDB[1] = 17;
        btCDB[4] = (byte) this.m_nPacketSize;
        boolean w_bRet = this.UsbSCSIWrite(btCDB, 8, this.m_abyPacket, this.m_nPacketSize, 5000);
        return !w_bRet ? false : this.USB_ReceiveAck(wCMD);
    }

    private boolean USB_ReceiveAck(short wCMD) {
        byte[] btCDB = new byte[8];
        byte[] w_abyWaitPacket = new byte[26];
        Arrays.fill(btCDB, (byte) 0);
        int c = 0;
        Arrays.fill(w_abyWaitPacket, (byte) -81);

        byte w_nLen;
        do {
            Arrays.fill(this.m_abyPacket, (byte) 0);
            btCDB[0] = -17;
            btCDB[1] = 18;
            w_nLen = 26;
            if (!this.UsbSCSIRead(btCDB, 8, this.m_abyPacket, w_nLen, 5000)) {
                return false;
            }

            SystemClock.sleep(40L);
            ++c;
        } while (this.memcmp(this.m_abyPacket, w_abyWaitPacket, 26));

        this.m_nPacketSize = w_nLen;
        if (!this.CheckReceive(this.m_abyPacket, this.m_nPacketSize, (short) 21930, wCMD)) {
            return false;
        } else {
            return true;
        }
    }

    boolean USB_ReceiveDataAck(short wCMD) {
        byte[] btCDB = new byte[8];
        byte[] w_WaitPacket = new byte[10];
        this.memset(btCDB, (byte) 0, 8);
        this.memset(w_WaitPacket, (byte) -81, 10);

        do {
            btCDB[0] = -17;
            btCDB[1] = 21;
            int w_nLen = 8;
            if (!this.UsbSCSIRead(btCDB, 8, this.m_abyPacket, w_nLen, 5000)) {
                return false;
            }

            SystemClock.sleep(40L);
        } while (this.memcmp(this.m_abyPacket, w_WaitPacket, 8));

        int w_nLen = (short) (this.m_abyPacket[7] << 8 & '\uff00' | this.m_abyPacket[6] & 255) + 2;
        if (!this.USB_ReceiveRawData(this.m_abyPacket2, w_nLen)) {
            return false;
        } else {
            System.arraycopy(this.m_abyPacket2, 0, this.m_abyPacket, 8, w_nLen);
            this.m_nPacketSize = 8 + w_nLen;
            if (!this.CheckReceive(this.m_abyPacket, this.m_nPacketSize, (short) 23205, wCMD)) {
                return false;
            } else {
                return true;
            }
        }
    }

    boolean USB_SendDataPacket(short wCMD) {
        byte[] btCDB = new byte[8];
        this.memset(btCDB, (byte) 0, 8);
        btCDB[0] = -17;
        btCDB[1] = 19;
        btCDB[4] = (byte) (this.m_nPacketSize & 255);
        btCDB[5] = (byte) (this.m_nPacketSize >> 8 & 255);
        return !this.UsbSCSIWrite(btCDB, 8, this.m_abyPacket, this.m_nPacketSize, 5000) ? false : this.USB_ReceiveDataAck(wCMD);
    }

    boolean USB_ReceiveDataPacket(short wCMD) {
        return this.USB_ReceiveDataAck(wCMD);
    }

    boolean USB_ReceiveRawData(byte[] pBuffer, int nDataLen) {
        byte[] btCDB = new byte[8];
        this.memset(btCDB, (byte) 0, 8);
        btCDB[0] = -17;
        btCDB[1] = 20;
        return this.UsbSCSIRead(btCDB, 8, pBuffer, nDataLen, 5000);
    }

    boolean USB_ReceiveImage(byte[] p_pBuffer, int nDataLen) {
        byte[] btCDB = new byte[8];
        byte[] w_WaitPacket = new byte[8];
        this.memset(btCDB, (byte) 0, 8);
        this.memset(w_WaitPacket, (byte) -81, 8);
        if (nDataLen < 65536) {
            btCDB[0] = -17;
            btCDB[1] = 22;
            if (!this.UsbSCSIRead(btCDB, 8, p_pBuffer, nDataLen, 5000)) {
                return false;
            }
        } else if (nDataLen == 73728) {
            btCDB[0] = -17;
            btCDB[1] = 22;
            btCDB[2] = 0;
            if (!this.UsbSCSIRead(btCDB, 8, p_pBuffer, nDataLen / 2, 5000)) {
                return false;
            }

            btCDB[0] = -17;
            btCDB[1] = 22;
            btCDB[2] = 1;
            if (!this.UsbSCSIRead(btCDB, 8, this.m_abyPacket2, nDataLen / 2, 5000)) {
                return false;
            }

            System.arraycopy(this.m_abyPacket2, 0, p_pBuffer, nDataLen / 2, nDataLen / 2);
        }

        return true;
    }

    private boolean memcmp(byte[] p1, byte[] p2, int nLen) {
        for (int i = 0; i < nLen; ++i) {
            if (p1[i] != p2[i]) {
                return false;
            }
        }

        return true;
    }

    private void memset(byte[] p1, byte nValue, int nLen) {
        Arrays.fill(p1, 0, nLen, nValue);
    }

    private void memcpy(byte[] p1, byte nValue, int nLen) {
        Arrays.fill(p1, 0, nLen, nValue);
    }

    private short MAKEWORD(byte low, byte high) {
        short s = (short) (high << 8 & '\uff00' | low & 255);
        return s;
    }

    private byte LOBYTE(short s) {
        return (byte) (s & 255);
    }

    private byte HIBYTE(short s) {
        return (byte) (s >> 8 & 255);
    }

    public boolean UsbSCSIWrite(byte[] pCDB, int nCDBLen, byte[] pData, int nDataLen, int nTimeOut) {
        byte[] w_abyTmp = new byte[31];
        byte[] w_abyCSW = new byte[13];
        w_abyTmp[0] = 85;
        w_abyTmp[1] = 83;
        w_abyTmp[2] = 66;
        w_abyTmp[3] = 67;
        w_abyTmp[4] = 40;
        w_abyTmp[5] = 43;
        w_abyTmp[6] = 24;
        w_abyTmp[7] = -119;
        w_abyTmp[8] = 0;
        w_abyTmp[9] = 0;
        w_abyTmp[10] = 0;
        w_abyTmp[11] = 0;
        w_abyTmp[12] = 0;
        w_abyTmp[13] = 0;
        w_abyTmp[14] = 10;
        System.arraycopy(pCDB, 0, w_abyTmp, 15, nCDBLen);
        boolean w_bRet = this.UsbBulkSend(w_abyTmp, 31, nTimeOut);
        if (!w_bRet) {
            return false;
        } else {
            w_bRet = this.UsbBulkSend(pData, nDataLen, nTimeOut);
            if (!w_bRet) {
                return false;
            } else {
                w_bRet = this.UsbBulkReceive(w_abyCSW, 13, nTimeOut);
                return w_bRet;
            }
        }
    }

    private boolean UsbBulkSend(byte[] pBuf, int nLen, int nTimeOut) {
        int n = nLen / this.m_nEPOutSize;
        int r = nLen % this.m_nEPOutSize;

        int i;
        int w_nRet;
        for (i = 0; i < n; ++i) {
            System.arraycopy(pBuf, i * this.m_nEPOutSize, this.m_abyTransferBuf, 0, this.m_nEPOutSize);
            w_nRet = this.m_usbConn.bulkTransfer(this.m_epOUT, this.m_abyTransferBuf, this.m_nEPOutSize, nTimeOut);
            if (w_nRet != this.m_nEPOutSize) {
                return false;
            }
        }

        if (r > 0) {
            System.arraycopy(pBuf, i * this.m_nEPOutSize, this.m_abyTransferBuf, 0, r);
            w_nRet = this.m_usbConn.bulkTransfer(this.m_epOUT, this.m_abyTransferBuf, r, nTimeOut);
            if (w_nRet != r) {
                return false;
            }
        }

        return true;
    }

    private boolean UsbBulkReceive(byte[] pBuf, int nLen, int nTimeOut) {
        int n = nLen / this.m_nEPInSize;
        int r = nLen % this.m_nEPInSize;

        int i;
        int w_nRet;
        for (i = 0; i < n; ++i) {
            w_nRet = this.m_usbConn.bulkTransfer(this.m_epIN, this.m_abyTransferBuf, this.m_nEPInSize, nTimeOut);
            if (w_nRet != this.m_nEPInSize) {
                return false;
            }

            System.arraycopy(this.m_abyTransferBuf, 0, pBuf, i * this.m_nEPInSize, this.m_nEPInSize);
        }

        if (r > 0) {
            w_nRet = this.m_usbConn.bulkTransfer(this.m_epIN, this.m_abyTransferBuf, r, nTimeOut);
            if (w_nRet != r) {
                return false;
            }

            System.arraycopy(this.m_abyTransferBuf, 0, pBuf, i * this.m_nEPInSize, r);
        }

        return true;
    }

    public boolean UsbSCSIRead(byte[] pCDB, int nCDBLen, byte[] pData, int nDataLen, int nTimeOut) {
        byte[] w_abyTmp = new byte[31];
        byte[] w_abyCSW = new byte[13];
        w_abyTmp[0] = 85;
        w_abyTmp[1] = 83;
        w_abyTmp[2] = 66;
        w_abyTmp[3] = 67;
        w_abyTmp[4] = 40;
        w_abyTmp[5] = 43;
        w_abyTmp[6] = 24;
        w_abyTmp[7] = -119;
        w_abyTmp[8] = 0;
        w_abyTmp[9] = 0;
        w_abyTmp[10] = 0;
        w_abyTmp[11] = 0;
        w_abyTmp[12] = -128;
        w_abyTmp[13] = 0;
        w_abyTmp[14] = 10;
        System.arraycopy(pCDB, 0, w_abyTmp, 15, nCDBLen);
        boolean w_bRet = this.UsbBulkSend(w_abyTmp, 31, nTimeOut);
        if (!w_bRet) {
            return false;
        } else {
            long w_nTime = SystemClock.elapsedRealtime();
            w_bRet = this.UsbBulkReceive(pData, nDataLen, nTimeOut);
            w_nTime = SystemClock.elapsedRealtime() - w_nTime;
            if (!w_bRet) {
                return false;
            } else {
                w_bRet = this.UsbBulkReceive(w_abyCSW, 13, nTimeOut);
                return w_bRet;
            }
        }
    }
    public String GetErrorMsg(int nErrorCode) {
        String str;
        switch (nErrorCode) {
            case FingerCommon.ERR_SUCCESS:
                str = "Succcess";
                break;
            case FingerCommon.ERR_VERIFY:
                str = "Verify NG";
                break;
            case FingerCommon.ERR_IDENTIFY:
                str = "验证不通过！";
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
                str = "该指纹已存在！";
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
    public   void MakeBMPBuf(byte[] Input, byte[] Output, int iImageX, int iImageY) {
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
