package com.ethink.vcd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.R;
import com.ethink.vcd.event.FingerPushMessage;
import com.ethink.vcd.finger.FingerCommon;
import com.ethink.vcd.finger.FingerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FingerActivity extends AppCompatActivity implements FingerPushMessage {
    private FingerUtil fingerUtil;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        ButterKnife.bind(this);
        fingerUtil = new FingerUtil(this, new FingerCommon(this, "/dev/ttyS1", 115200));

    }


    @OnClick({R.id.register, R.id.very, R.id.scanner, R.id.upload, R.id.tempall, R.id.deltempall, R.id.close, R.id.search, R.id.strand, R.id.start_camera})
    public void onClick(View view) {
        PluginMessage pluginMessage = new PluginMessage("FINGER", "finger---");
        switch (view.getId()) {
            case R.id.register:
                executorService.execute(() -> {
                    fingerUtil.register();
                });
                break;
            case R.id.very:
                executorService.execute(() -> {
                    fingerUtil.verify();
                });
                break;
            case R.id.tempall:
                executorService.execute(() -> {
                    fingerUtil.templateTotal();
                });
                break;
            case R.id.deltempall:
                executorService.execute(() -> {
                    fingerUtil.delTemplateAll();
                });
                break;
            case R.id.close:
                fingerUtil.close();
                break;
            case R.id.search:
                executorService.execute(() -> {
                    fingerUtil.search();
                });
                break;
            case R.id.start_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.upload:
                executorService.execute(() -> {
                    fingerUtil.downloadChar();
                });
                break;

        }
        logger.info("指纹结果：" + JSON.toJSONString(pluginMessage));

    }

    @Override
    public void message(String msg) {
        EventMessage eventMessage = new EventMessage("FINGER_MESSAGE");
        eventMessage.setString("msg", msg);
        eventMessage.setString("type", "1");
        logger.info("推送消息：" + JSON.toJSONString(eventMessage));
    }

    @Override
    public void pushData(String msg, String data, String number) {
        EventMessage eventMessage = new EventMessage("FINGER_MESSAGE");
        eventMessage.setString("msg", msg);
        eventMessage.setString("type", "2");
        eventMessage.setString("data", data);
        eventMessage.setString("number", number);
        logger.info("pushData 推送消息：" + JSON.toJSONString(eventMessage));
    }
}
