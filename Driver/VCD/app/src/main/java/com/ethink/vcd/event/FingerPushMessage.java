package com.ethink.vcd.event;

public interface FingerPushMessage {
    void message(String msg);
    void pushData(String msg,String data,String number);
    void upload(String path,String finger);


}
