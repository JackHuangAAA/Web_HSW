package com.ethink.snd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.tools.serialport.SerialPort;
import com.ethink.tools.serialport.SerialPortManager;
import com.ethink.tools.serialport.SerialPortSetting;
import com.ethink.snd.App;
import com.ethink.snd.Const;
import com.ethink.snd.R;
import com.ethink.snd.SPUtils;
import com.ethink.snd.controller.FingerCommon;
import com.ethink.snd.controller.FingerUtil;
import com.ethink.snd.event.FingerPushMessage;
import com.ethink.snd.service.HttpUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/***
 * 指纹测试工具
 * ***/
public class FingerActivity extends AppCompatActivity implements FingerPushMessage, Runnable {
    private FingerUtil fingerUtil;
    protected Logger logger = LoggerFactory.getLogger(getClass());


    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.tv_result)
    TextView textView;
    private String fingerUrl;
    private Thread thread;
    private SerialPort scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        ButterKnife.bind(this);
        fingerUtil = new FingerUtil(this, new FingerCommon(this, "/dev/ttyS1", 115200));
        fingerUrl = SPUtils.getSharedStringData(App.getAppContext(), Const.FINGER_URL);
         initScanner();

    }


    @OnClick({R.id.register, R.id.very, R.id.finger_del, R.id.remote_register, R.id.finger_cancel, R.id.remote_verify, R.id.door, R.id.tempall, R.id.deltempall, R.id.search, R.id.start_camera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                //本地注册
                new Thread(() -> {
                    fingerUtil.register();
                }).start();
                break;
            case R.id.very:
                String num = "1";
                if (StringUtils.isEmpty(num)) {
                    logger.info("请输入指纹序号！");
                    return;
                }
                try {
                    int id = Integer.parseInt(num);
                    new Thread(() -> {
                        fingerUtil.verify(id);
                    }).start();
                } catch (NumberFormatException e) {
                    logger.info("输入的序号不正确", e);
                }
                break;
            case R.id.tempall:
                new Thread(() -> {
                    fingerUtil.templateTotal();
                }).start();

                break;
            case R.id.deltempall:
                new Thread(() -> {
                    fingerUtil.delTemplateAll();
                }).start();

                break;
            case R.id.search:
                fingerUtil.search();
                break;
            case R.id.start_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.door:
                startActivity(new Intent(this, ArkActivity.class));
                break;
            case R.id.remote_register:
                //注册并上传
                fingerUtil.remoteRegister("");
                break;
            case R.id.remote_verify:
                //后台比对
                fingerUtil.remoteVerify();
                break;
            case R.id.finger_cancel:
                fingerUtil.cancel();
                break;
            case R.id.finger_del:
                delFinger("");
                break;

        }

    }

    @Override
    public void message(int type, String msg) {
        logger.info("推送消息：" + msg);
        runOnUiThread(() -> {
            Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
        });

    }


    private void delFinger(String id) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "");
        Request request = new Request.Builder().url(fingerUrl + "/unregister?tag=" + id).post(body).build();
        HttpUtils.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                logger.error("错误 ", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String result = response.body().string();
                    runOnUiThread(() -> {
                        textView.setText("删除结果：" + result);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public String upload(String path, String finger) {
        runOnUiThread(() -> {
            imageView.setImageBitmap(Const.stringToBitmap(finger));
        });
        try {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), finger);
            Request request = new Request.Builder().url(String.format("http://192.168.0.65:8080%s", path)).post(body).build();
            Response response = HttpUtils.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                runOnUiThread(() -> {
                    textView.setText("结果：\n" + result);
                });
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getIntValue("code") == 0) {
                    //  message(2,jsonObject.getString("data"));
                    return jsonObject.getString("data");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //扫码
    @Override
    public void run() {
        byte[] buffer = new byte[256];
        //     byte[]b=new byte[]{0x16,0x54,0x0D,0x21};
        StringBuilder scantext=new StringBuilder();
        while (!Thread.interrupted()&&scanner!=null) {
            try {
                int ret = scanner.read(buffer, 0, 256);
                //   scanner.write(b,0,4);
                if (ret > 0) {
                    String tmp = new String(buffer, 0, ret);
                    scantext.append(tmp);
                    //Log.d(TAG, "run: 扫码结果："+scantext);
                    // logger.info("扫码数据"+scantext);
                    //scantext.charAt(scantext.length() - 1) == '\r'
                    if (scantext.charAt(scantext.length() - 1) == 0x0A) {
                        //Log.d(TAG, "run: 扫码枪截取数据："+scantext);
                        //logger.info("扫码枪截取1数据" + scantext);
                        String str=scantext.substring(0, scantext.length() - 2);
                        logger.info("扫码枪截取数据" + str);
                        runOnUiThread(()->{
                            Toast.makeText(FingerActivity.this,str,Toast.LENGTH_LONG).show();
                        });
                        scantext.delete(0,scantext.length());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("读取串口异常{}", e.getMessage());
                //EventBus.getDefault().post(new WriteConsoleEvent("读串口超时 " + e.getMessage()));
            }
        }
    }

    private void initScanner() {
        try {
            SerialPortSetting setting = new SerialPortSetting(
                    115200, 8, SerialPortSetting.STOPBITS_1, SerialPortSetting.PARITY_NONE);
            setting.setReadTimeout(0);
            scanner = SerialPortManager.getSerialPort("/dev/ttyS4", setting);
            logger.info("--------连接扫码-----------");
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(thread!=null){
            thread.interrupt();
        }
    }
}
