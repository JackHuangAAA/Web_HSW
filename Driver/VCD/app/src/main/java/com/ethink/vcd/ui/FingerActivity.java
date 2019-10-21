package com.ethink.vcd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.Const;
import com.ethink.vcd.R;
import com.ethink.vcd.controller.FingerCommon;
import com.ethink.vcd.controller.FingerUtil;
import com.ethink.vcd.event.FingerPushMessage;
import com.ethink.vcd.service.HttpUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FingerActivity extends AppCompatActivity implements FingerPushMessage {
    private FingerUtil fingerUtil;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.tv_result)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        ButterKnife.bind(this);
        fingerUtil = new FingerUtil(this, new FingerCommon(this, "/dev/ttyS1", 115200));

    }


    @OnClick({R.id.register, R.id.very, R.id.upload, R.id.remote_register, R.id.remote_verify, R.id.door, R.id.tempall, R.id.deltempall, R.id.close, R.id.search, R.id.strand, R.id.start_camera})
    public void onClick(View view) {
        PluginMessage pluginMessage = new PluginMessage("FINGER", "finger---");
        switch (view.getId()) {
            case R.id.register:
                executorService.execute(() -> {
                    fingerUtil.register();
                });
                break;
            case R.id.very:
                String num = pluginMessage.getString("number");
                executorService.execute(() -> {
                    if (StringUtils.isEmpty(num)) {
                        logger.info("请输入指纹序号！");
                        return;
                    }
                    try {
                        int id = Integer.parseInt(num);
                        fingerUtil.verify(id);
                    } catch (NumberFormatException e) {
                        logger.info("输入的序号不正确", e);
                    }
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
            case R.id.door:
                startActivity(new Intent(this, ArkActivity.class));
                break;
            case R.id.remote_register:
                //注册并上传
                executorService.execute(()->{
                    fingerUtil.remoteRegister();
                });

                break;
            case R.id.remote_verify:
                //后台比对
                executorService.execute(()->{
                    fingerUtil.remoteVerify();
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

    @Override
    public void upload(String path, String finger) {
        runOnUiThread(()->{
            imageView.setImageBitmap(Const.stringToBitmap(finger));
        });
        try {
            if(path.contains("register")){
                path=path+"?tag="+"用户id";
            }
            RequestBody body =RequestBody.create(MediaType.parse("text/plain"),finger);
            Request request = new Request.Builder().url(  String.format("http://192.168.0.65:8080%s",path)).post(body).build();
            Response response = HttpUtils.getOkHttpClient().newCall(request).execute();
            if(response.isSuccessful()){
                String result=response.body().string();
                runOnUiThread(()->{
                    textView.setText("结果：\n"+result);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
