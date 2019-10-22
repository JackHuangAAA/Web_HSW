package com.ethink.fps.exception;

public class ServerException extends RuntimeException {
    private int code;
    private String message;

    public ServerException(int code,String message){
        this.code = code;
        this.message = message;
    }

    public ServerException(String message){
        this.code = 9999;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
