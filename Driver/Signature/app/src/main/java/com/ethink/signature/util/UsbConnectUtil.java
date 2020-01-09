package com.ethink.signature.util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class UsbConnectUtil {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final static int PID11 = 8211;
    private final static int PID13 = 8213;
    private final static int PID15 = 8215;
    private final static int VENDORID = 1305;

    private static String TAG = "UsbConnectUtil";
    private USBDeviceFace usbDeviceFace;
    private UsbDevice usbDevice;
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
    private static final String USB_NAME = "usb_device_name";
    private Context context;

    public UsbConnectUtil(Context context, USBDeviceFace usbDeviceFace) {
        this.usbDeviceFace = usbDeviceFace;
        this.context = context;


    }

    /***
     *
     * 申请USB权限
     * **/
    public void requestUsb() {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceMap = usbManager.getDeviceList();
        for (UsbDevice device : deviceMap.values()) {
            if (usbDeviceFace != null) {
                logger.info("usb device vid={}  pid={} name={}",device.getVendorId(),device.getProductId(),device.getProductName());
                if (usbDeviceFace.checkUSB(device)) {
                    this.usbDevice = device;
                    Log.i(TAG, "找到所需要的USB设备");
                    break;
                }
            }
        }
        if (usbDevice != null) {
            //开始申请权限
            if (usbManager.hasPermission(usbDevice)) {
                //已拥有权限
                if (usbDeviceFace != null)
                    usbDeviceFace.openUSB(usbDevice);
            } else {
                //申请权限
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                context.registerReceiver(mUsbReceiver, filter);
                usbManager.requestPermission(usbDevice, pendingIntent); // 该代码执行后，系统弹出一个对话框
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
                    if (usbDeviceFace != null&&usbDeviceFace.checkUSB(device)) {
                        usbDeviceFace.openUSB(device);
                    }
                } else {
                    //申请权限失败
                    Log.i(TAG, "申请权限失败了------");
                    if (usbDeviceFace != null) {
                        usbDeviceFace.deniedPermission();
                    }
                }

            }
        }
    };


}
