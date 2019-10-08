package com.ethink.vcd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ethink.vcd.Const;
import com.ethink.vcd.R;
import com.ethink.vcd.SPUtils;
import com.ethink.vcd.server.VCDService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigActivity extends AppCompatActivity {

    @BindView(R.id.et_url)
    EditText etUrl;

    @BindView(R.id.et_redisip)
    EditText et_redisip;

    @BindView(R.id.et_redisport)
    EditText et_redisport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ButterKnife.bind(this);
        String url = SPUtils.getSharedStringData(this, Const.CONFIG_URL);
        String redisIp = SPUtils.getSharedStringData(this, Const.REDIS_IP);
        int redisPort = SPUtils.getSharedIntData(this, Const.REDIS_PORT);
        //配置页面是否为空，空设置默认地址
        if(url.isEmpty()) {
            etUrl.setText("http://192.168.0.96:9998/");
            //   etUrl.setText("http://ads.ethinkbank.com:80");

        }else{
            etUrl.setText(url);
        }
        if(redisIp.isEmpty()) {
            et_redisip.setText("ads.ethinkbank.com");

        }else{
            et_redisip.setText(redisIp);
        }

        if(redisPort==0) {
            et_redisport.setText("8081");

        }else{
            et_redisport.setText(redisPort+"");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick(R.id.bt_ok)
    public void onBtnOkClick(){
        String url = etUrl.getText().toString();
       // String redisIp = et_redisip.getText().toString();
        String redisPort= et_redisport.getText().toString();


        if (!url.isEmpty()){
            SPUtils.setSharedStringData(getApplication(),Const.CONFIG_URL,url);
        }else {
            Toast.makeText(this,"请输入服务地址",Toast.LENGTH_LONG).show();
            return;
        }
//        if (!redisIp.isEmpty()){
//            SPUtils.setSharedStringData(getApplication(),Const.REDIS_IP,redisIp);
//        }else {
//            Toast.makeText(this,"请输入推送IP",Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!redisPort.isEmpty()){
//            SPUtils.setSharedIntData(getApplication(),Const.REDIS_PORT,Integer.parseInt(redisPort));
//        }else {
//            Toast.makeText(this,"请输入推送端口",Toast.LENGTH_LONG).show();
//            return;
//        }
        Toast.makeText(this,"配置地址已经生效",Toast.LENGTH_LONG).show();

        Intent startIntent = new Intent(this, VCDService.class);
        startService(startIntent);
    }
}
