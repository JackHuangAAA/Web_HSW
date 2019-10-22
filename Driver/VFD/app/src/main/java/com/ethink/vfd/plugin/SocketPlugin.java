package com.ethink.vfd.plugin;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.PhoneUtils;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.message.EventMessage;
import com.ethink.vfd.Const;
import com.ethink.vfd.SPUtils;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/***
 *
 * Socket.io
 * ***/
public class SocketPlugin extends BasePlugin {
    private Context context;
    private Socket socket;

    public SocketPlugin(Context context) {
        super("SOCKET");
        this.context = context;
    }

    @Override
    public void onStart() {
        initSocket();
    }

    @Override
    public void onStop() {

    }


    private void initSocket() {
        try {
            String url=SPUtils.getSharedStringData(context, Const.SOCKET_IO_URL);
            logger.info("socket.io连接地址："+url);
            socket = IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                logger.info("PushService connect" + args.length);
                JSONObject obj = new JSONObject();
                obj.put("deviceId", PhoneUtils.getSerial());
                socket.emit("register", obj);
            }
        }).on("timedData", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                logger.info("PushService push");
                if (args.length > 0) {
                    logger.info("PushService push 定时数据timedData" + args[0]);
                    EventMessage eventMessage = new EventMessage("SOCKET_DATA");
                    eventMessage.setString("data", args[0].toString());
                    pluginManager.post(eventMessage);

                }

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                logger.info("PushService disconnect");
                socket.connect();
            }
        });
        logger.info("Socket.io开始连接-----------------");
        socket.connect();
    }


}
