package com.ethink.lineup.controller;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.ethink.lineup.R;
import com.ethink.lineup.util.Bills;
import com.ethink.lineup.util.T;
import com.printsdk.usbsdk.UsbDriver;

public class PrintController {
    private UsbManager mUsbManager;
    private Context context;
    private UsbDriver mUsbDriver;
    private UsbReceiver mUsbReceiver;
    private final static int PID11 = 8211;
    private final static int VENDORID = 1305;
    private static final String ACTION_USB_PERMISSION = "com.usb.sample.USB_PERMISSION";
    UsbDevice mUsbDev;
    public PrintController(Context context) {
        this.context = context;
        getUsbDriverService();

    }

    private void getUsbDriverService() {
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        mUsbDriver = new UsbDriver(mUsbManager, context);
        PendingIntent permissionIntent1 = PendingIntent.getBroadcast(context, 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        mUsbDriver.setPermissionIntent(permissionIntent1);
        // Broadcast listen for new devices
        mUsbReceiver = new UsbReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(mUsbReceiver, filter);
    }

    /*
     *  BroadcastReceiver when insert/remove the device USB plug into/from a USB port
     *  创建一个广播接收器接收USB插拔信息：当插入USB插头插到一个USB端口，或从一个USB端口，移除装置的USB插头
     */
    public class UsbReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                if (mUsbDriver.usbAttached(intent)) {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if ((device.getProductId() == PID11 && device.getVendorId() == VENDORID) ) {
                        if (mUsbDriver.openUsbDevice(device)) {
                            mUsbDev=device;
                        }
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent
                        .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if ((device.getProductId() == PID11 && device.getVendorId() == VENDORID) ) {
                    mUsbDriver.closeUsbDevice(device);
                        mUsbDev = null;
                }
            } else if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device.getProductId() == PID11 && device.getVendorId() == VENDORID) {
                            if (mUsbDriver.openUsbDevice(device)) {
                                mUsbDev=device;
                            }
                        }
                    } else {
                        T.showShort(context, "permission denied for device");
                        //Log.d(TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    }
    public boolean printConnStatus() {
        boolean blnRtn = false;
        try {
            if (!mUsbDriver.isConnected()) {
                // USB线已经连接
                for (UsbDevice device : mUsbManager.getDeviceList().values()) {
                    if (device.getProductId() == PID11 && device.getVendorId() == VENDORID) {
//						if (!mUsbManager.hasPermission(device)) {
//							break;
//						}
                        blnRtn = mUsbDriver.usbAttached(device);
                        if (!blnRtn) {
                            break;
                        }
                        blnRtn = mUsbDriver.openUsbDevice(device);
                        // 打开设备
                        if (blnRtn) {
                                mUsbDev = device;
                            T.showShort(context, context.getString(R.string.USB_Driver_Success));
                            break;
                        } else {
                            T.showShort(context, context.getString(R.string.USB_Driver_Failed));
                            break;
                        }
                    }
                }
            } else {
                blnRtn = true;
            }
        } catch (Exception e) {
            T.showShort(context, e.getMessage());
        }
        return blnRtn;
    }
    public void print(String title,String num,String coder,int current){
        if(mUsbDriver==null)return;
        Bills.printSmallTicket(mUsbDriver,title,num,coder,current); // 排队票据
    }

}
