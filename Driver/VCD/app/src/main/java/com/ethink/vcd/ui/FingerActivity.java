package com.ethink.vcd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ethink.plugin.message.PluginMessage;
import com.ethink.vcd.R;
import com.ethink.vcd.finger.FingerCommon;
import com.ethink.vcd.finger.FingerUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FingerActivity extends AppCompatActivity     {
    private FingerUtil fingerUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        ButterKnife.bind(this);
        fingerUtil = new FingerUtil(this,new FingerCommon(this,"/dev/ttyS1",115200));

    }


    @OnClick({R.id.register, R.id.open, R.id.very,R.id.scanner, R.id.tempall,R.id.deltempall,R.id.close,R.id.search,R.id.strand,R.id.start_camera})
    public void onClick(View view) {
        PluginMessage pluginMessage=new PluginMessage("FINGER","finger---");
        switch (view.getId()) {
            case R.id.register:
                new Thread(()->{
                    fingerUtil.register(pluginMessage);
                }).start();

                break;
            case R.id.open:
//                fingerUtil.usbOpen(pluginMessage);
                break;
            case R.id.very:
                fingerUtil.verify( pluginMessage);
                break;
            case R.id.tempall:
                fingerUtil.templateTotal(pluginMessage );
                break;
            case R.id.deltempall:
                fingerUtil.delTemplateAll( pluginMessage);
                break;
            case R.id.close:
                fingerUtil.close( );
                break;
            case R.id.search:
                fingerUtil.search();
                break;
            case R.id.start_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;

        }
    }

}
