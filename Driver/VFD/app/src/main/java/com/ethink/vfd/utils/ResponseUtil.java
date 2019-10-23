package com.ethink.vfd.utils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ethink.plugin.message.PluginMessage;

public class ResponseUtil {

    public static PluginMessage success(@NonNull PluginMessage pluginMessage, Object msg, Object data) {
        pluginMessage.changeToResponse();
        pluginMessage.setString("code", "0000");
        pluginMessage.setString("message", String.format("%s", msg));
        if (data != null) {
            pluginMessage.setString("data", JSON.toJSONString(data));
        }
        return pluginMessage;
    }

    public static PluginMessage fail(@NonNull PluginMessage pluginMessage, Object msg) {
        pluginMessage.changeToResponse();
        pluginMessage.setString("code", "9999");
        pluginMessage.setString("message", String.format("%s", msg));
        return pluginMessage;
    }

    public static JSONObject data(String... data) {
        JSONObject object = new JSONObject();
        if (data != null) {
            for (int i = 0; i < data.length; i+=2) {
                object.put(data[i],data[i+1]);
            }
        }
        return object;
    }


}
