package com.ethink.snd.event;

public interface FingerPushMessage {
    void message(int type,String msg);

    String upload(String path,String finger);


}
