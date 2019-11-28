package com.ethink.cnd.plugin;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.message.EventMessage;
import com.ethink.cnd.App;
import com.ethink.cnd.Const;
import com.ethink.cnd.SPUtils;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;

/***
 *
 * Socket.io
 * ***/
public class SocketPlugin extends BasePlugin {
    private Context context;
    private Socket socket;
    private String deviceId;
    private Timer timer;
    private boolean connect = false;

    public SocketPlugin(Context context) {
        super("SOCKET");
        this.context = context;
        deviceId = SPUtils.getSharedStringData(App.getAppContext(), Const.SERIAL_NO);
    }

    @Override
    public void onStart() {
        timer = new Timer();
        initSocket();
    }

    @Override
    public void onStop() {
        timer.cancel();
    }

    private void initSocket() {
        try {
            String url = SPUtils.getSharedStringData(context, Const.SOCKET_IO_URL);
            logger.info("socket.io连接地址：" + url);
            socket = IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, (args) -> {
            logger.info("PushService connect" + args.length);
            connect = true;
            JSONObject obj = new JSONObject();
            obj.put("code", deviceId);
            socket.emit("register", obj);
        }).on("timedData", (args) -> {
            logger.info("PushService push");
            if (args.length > 0) {
                logger.info("PushService push 定时数据timedData" + args[0]);
                EventMessage eventMessage = new EventMessage("SOCKET_DATA");
                eventMessage.setString("data", args[0].toString());
                pluginManager.post(eventMessage);
            }
        }).on("receiveVaccination", (args) -> {
            if (args.length > 0) {
                logger.info("receiveVaccination 数据： {}", args[0]);
                EventMessage eventMessage = new EventMessage("SOCKET_VACCINATION_DATA");
                eventMessage.setString("data", args[0].toString());
                pluginManager.post(eventMessage);

            }

        }).on("receiveVaccinationStatus", (args) -> {
            if (args.length > 0) {
                logger.info("receiveVaccinationStatus 数据：  {}", args[0]);
                EventMessage eventMessage = new EventMessage("SOCKET_VACCINATION_STATUS_DATA");
                eventMessage.setString("data", args[0].toString());
                pluginManager.post(eventMessage);

            }
        }).on(Socket.EVENT_DISCONNECT, (args) -> {
            logger.info("PushService disconnect");
            connect = false;
            socket.connect();
        });
        socket.connect();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!connect) {
                    socket.connect();
                    logger.info("Socket.io断线重新连接-----------------");
                }

            }
        }, 5000, 10000);
    }


}
