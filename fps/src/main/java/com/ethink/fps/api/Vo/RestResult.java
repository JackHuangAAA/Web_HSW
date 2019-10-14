package com.ethink.fps.api.Vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2016/12/23.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult {
    @JsonProperty("Status")
    private int code;
    @JsonProperty("ErrorMessage")
    private String message;
    @JsonProperty("Data")
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
