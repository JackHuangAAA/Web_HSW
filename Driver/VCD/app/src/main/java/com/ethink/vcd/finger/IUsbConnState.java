package com.ethink.vcd.finger;

public interface IUsbConnState {
    void onUsbConnected();
    void onUsbPermissionDenied();
    void onDeviceNotFound();
}
