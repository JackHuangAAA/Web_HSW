package com.ethink.vcd.event;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteConsoleEvent {
    private String message;
    private SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss");

    public WriteConsoleEvent(String message){
        this.message = sf.format(new Date()) + " : "+ message + "\n";
    }

    public WriteConsoleEvent(byte[] buffer){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i< buffer.length; i++){
            sb.append(String.format("%02X",buffer[i] & 0xFF));
        }
        message = sf.format(new Date()) + " : "+ sb.toString() + "\n";
    }

    public WriteConsoleEvent(byte[] buffer, int size){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i< size; i++){
            sb.append(String.format("%02X",buffer[i] & 0xFF));
        }
        message = sf.format(new Date()) + " : "+ sb.toString() + "\n";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
