package com.ethink.vcd.event;

public interface FingerPushMessage {
    void message(int type,String msg);

    String upload(String path,String finger);


}
