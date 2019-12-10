package com.ethink.lineup.event;

public interface FingerPushMessage {
    void message(int type, String msg);

    String upload(String path, String finger);


}
