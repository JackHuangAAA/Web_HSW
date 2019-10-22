package com.ethink.vcd.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ethink.vcd.Const;
import com.ethink.vcd.R;
import com.ethink.vcd.SPUtils;
import com.ethink.vcd.server.VCDService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ButterKnife.bind(this);
        String url = SPUtils.getSharedStringData(this, Const.CONFIG_URL);
        String socketUrl = SPUtils.getSharedStringData(this, Const.SOCKET_IO_URL);
        String serial = PhoneUtils.getSerial();
        //自动上报的地址
        if (url.isEmpty()) {
            etUrl.setText("http://192.168.0.162:8080");
            //   etUrl.setText("http://ads.ethinkbank.com:80");

        } else {
            etUrl.setText(url);
        }
        if (!StringUtils.isEmpty(serial)) {
            etSerial.setText(serial);
        }
        if (socketUrl.isEmpty()) {
            edSocket.setText("http://192.168.0.162:9996");
        } else {
            edSocket.setText(socketUrl);
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


    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick(R.id.bt_ok)
    public void onBtnOkClick() {
        String url = etUrl.getText().toString();
        String socketUrl = edSocket.getText().toString();
        String serial = etSerial.getText().toString();
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
        Toast.makeText(this, "配置地址已经生效", Toast.LENGTH_LONG).show();
        Intent startIntent = new Intent(this, VCDService.class);
        startService(startIntent);

        Intent finger = new Intent(this, FingerActivity.class);
        startActivity(finger);

        finish();
    }
}
