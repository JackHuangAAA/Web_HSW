package com.ethink.vcd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.App;
import com.ethink.vcd.Const;
import com.ethink.vcd.R;
import com.ethink.vcd.SPUtils;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FingerActivity extends AppCompatActivity implements FingerPushMessage {
    private FingerUtil fingerUtil;
    protected Logger logger = LoggerFactory.getLogger(getClass());


    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.tv_result)
    TextView textView;
    private String fingerUrl;
    private String uid="pppp255";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        ButterKnife.bind(this);
     //  fingerUtil = new FingerUtil(this, new FingerCommon(this, "/dev/ttyS1", 115200));
       fingerUrl= SPUtils.getSharedStringData(App.getAppContext(), Const.FINGER_URL);
    }


    @OnClick({R.id.register, R.id.very, R.id.upload,R.id.finger_del, R.id.remote_register, R.id.finger_cancel,R.id.remote_verify, R.id.door, R.id.tempall, R.id.deltempall, R.id.close, R.id.search, R.id.strand, R.id.start_camera})
    public void onClick(View view) {
        PluginMessage pluginMessage = new PluginMessage("FINGER", "finger---");
        switch (view.getId()) {
            case R.id.register:
                    fingerUtil.register();

                break;
            case R.id.very:
                String num = pluginMessage.getString("number");
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
                break;
            case R.id.tempall:
                    fingerUtil.templateTotal();
                break;
            case R.id.deltempall:
                    fingerUtil.delTemplateAll();
                break;
            case R.id.close:
                fingerUtil.close();
                break;
            case R.id.search:
                    fingerUtil.search();
                break;
            case R.id.start_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.upload:
                    fingerUtil.downloadChar();
                break;
            case R.id.door:
                startActivity(new Intent(this, ArkActivity.class));
                break;
            case R.id.remote_register:
                //注册并上传
                    fingerUtil.remoteRegister(uid);
                break;
            case R.id.remote_verify:
                //后台比对
                    fingerUtil.remoteVerify();
                break;
            case R.id.finger_cancel:
                    fingerUtil.cancel();
                break;
            case R.id.finger_del:
                delFinger(uid);
                break;

        }

    }

    @Override
    public void message(int type,String msg) {
        EventMessage eventMessage = new EventMessage("FINGER_MESSAGE");
        eventMessage.setString("msg", msg);
        eventMessage.setString("type", "1");
        logger.info("推送消息：" + JSON.toJSONString(eventMessage));
    }


    private void delFinger(String id){
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "");
        Request request = new Request.Builder().url( fingerUrl+"/unregister?tag="+id).post(body).build();
        HttpUtils.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure( @NonNull Call call, @NonNull IOException e) {
                logger.error("错误 ",e);
            }
            @Override
            public void onResponse( @NonNull Call call, @NonNull Response response)     {
                try {
                    String   result = response.body().string();
                    runOnUiThread(()->{
                        textView.setText("删除结果："+result);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public String upload(String path, String finger) {
        runOnUiThread(()->{
            imageView.setImageBitmap(Const.stringToBitmap(finger));
        });
        try {
            RequestBody body =RequestBody.create(MediaType.parse("text/plain"),finger);
            Request request = new Request.Builder().url(  String.format("http://192.168.0.65:8080%s",path)).post(body).build();
            Response response = HttpUtils.getOkHttpClient().newCall(request).execute();
            if(response.isSuccessful()){
                String result=response.body().string();
                runOnUiThread(()->{
                    textView.setText("结果：\n"+result);
                });
                JSONObject jsonObject=JSON.parseObject(result);
                if(jsonObject.getIntValue("code")==0){
                    //  message(2,jsonObject.getString("data"));
                    return jsonObject.getString("data");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
