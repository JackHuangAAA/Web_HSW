package com.ethink.lineup.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.lineup.App;
import com.ethink.lineup.Const;
import com.ethink.lineup.R;
import com.ethink.lineup.SPUtils;
import com.ethink.lineup.print_nh80m.SerialActivity;
import com.ethink.lineup.server.LineUpService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigActivity extends AppCompatActivity {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };
    private static final int REQUEST_CODE = 1;
    private List<String> mMissPermissions = new ArrayList<>();


    @BindView(R.id.et_url)
    EditText etUrl;

    @BindView(R.id.et_serial)
    EditText etSerial;

    @BindView(R.id.et_socket)
    EditText edSocket;
    @BindView(R.id.et_finger)
    EditText etFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ButterKnife.bind(this);
        String url = SPUtils.getSharedStringData(this, Const.CONFIG_URL);
        String socketUrl = SPUtils.getSharedStringData(this, Const.SOCKET_IO_URL);
        String serial = SPUtils.getSharedStringData(App.getAppContext(), Const.SERIAL_NO);
        String finger=SPUtils.getSharedStringData(App.getAppContext(), Const.FINGER_URL);
        //自动上报的地址
        if (url.isEmpty()) {
            etUrl.setText("http://iviotp.ethinkbank.com");
            //   etUrl.setText("http://ads.ethinkbank.com:80");

        } else {
            etUrl.setText(url);
        }
        if (!StringUtils.isEmpty(serial)) {
            etSerial.setText(serial);
        }
        else{
            etSerial.setText(""+ PhoneUtils.getSerial());
        }
        if (socketUrl.isEmpty()) {
            edSocket.setText("http://iviotp.ethinkbank.com");
        } else {
            edSocket.setText(socketUrl);
        }
        if(StringUtils.isEmpty(finger)){
            etFinger.setText("http://192.168.0.65:8080");
        }else{
            etFinger.setText(finger);
        }
        if (isVersionM()) {
            checkAndRequestPermissions();
        }



    }

    private boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private void checkAndRequestPermissions() {
        mMissPermissions.clear();
        for (String permission : REQUIRED_PERMISSION_LIST) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                mMissPermissions.add(permission);
            }
        }
        // check permissions has granted
        if (!mMissPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    mMissPermissions.toArray(new String[mMissPermissions.size()]),
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int i = grantResults.length - 1; i >= 0; i--) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mMissPermissions.remove(permissions[i]);
                }
            }
        }

        if (!mMissPermissions.isEmpty()) {
            Toast.makeText(this, "get permissions failed,exiting...", Toast.LENGTH_SHORT).show();
        }
    }




    @OnClick(R.id.bt_ok)
    public void onBtnOkClick() {
        String url = etUrl.getText().toString();
        String socketUrl = edSocket.getText().toString();
        String serial = etSerial.getText().toString();
        String finger_url=etFinger.getText().toString();
        if (!url.isEmpty()) {
            SPUtils.setSharedStringData(getApplication(), Const.CONFIG_URL, url);
        } else {
            Toast.makeText(this, "请输入服务地址", Toast.LENGTH_LONG).show();
            return;
        }
        if (!StringUtils.isEmpty(socketUrl)) {
            SPUtils.setSharedStringData(getApplication(), Const.SOCKET_IO_URL, socketUrl);
        } else {
            Toast.makeText(this, "请输入socket.io地址", Toast.LENGTH_LONG).show();
            return;
        }
        if (!StringUtils.isEmpty(serial)) {
            SPUtils.setSharedStringData(getApplication(), Const.SERIAL_NO, serial);
        } else {
            Toast.makeText(this, "请输入设备序列号", Toast.LENGTH_LONG).show();
            return;
        }
        if(!StringUtils.isEmpty(finger_url)){
            SPUtils.setSharedStringData(getApplication(), Const.FINGER_URL, finger_url);
        }else {
            Toast.makeText(this, "请输入指纹服务地址！", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "配置地址已经生效", Toast.LENGTH_LONG).show();
        Intent startIntent = new Intent(this, LineUpService.class);
        startService(startIntent);
//
        Intent intent = new Intent(this, SerialActivity.class);
        startActivity(intent);

        finish();
    }
}
