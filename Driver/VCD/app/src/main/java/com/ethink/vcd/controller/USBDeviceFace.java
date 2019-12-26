package com.ethink.vcd.controller;

import android.hardware.usb.UsbDevice;

public interface USBDeviceFace {
    String NAME="USB_DEVICE_NAME";
    /**
     * 判断是不是自己所需要的usb设备
     * ***/
    boolean checkUSB(UsbDevice usbDevice);
    /***
     * 已经拥指定有USB权限，可以打开设备
     * */
    void openUSB(UsbDevice usbDevice);
    /**
     * 授权失败
     * */
    void deniedPermission();

}
