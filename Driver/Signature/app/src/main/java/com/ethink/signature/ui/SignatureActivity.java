package com.ethink.signature.ui;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.ethink.signature.R;
import com.ethink.signature.util.DialogManage;
import com.ethink.signature.util.USBDeviceFace;
import com.ethink.signature.util.UsbConnectUtil;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.nidfpsensor.NIDFPFactory;
import com.zkteco.android.biometric.nidfpsensor.NIDFPSensor;
import com.zkteco.android.biometric.nidfpsensor.exception.NIDFPException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 *
 * Android手写签名板
 * **/
public class SignatureActivity extends AppCompatActivity implements USBDeviceFace {
public final static String TAG="SIGNATURE";
    @BindView(R.id.signature_pad)
    SignaturePad signaturePad;
    @BindView(R.id.clean)
    Button clean;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.image_tran)
    ImageView imageTran;
    @BindView(R.id.notice_info)
    TextView noticeInfo;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.disease)
    TextView disease;
    @BindView(R.id.vaccine_type)
    TextView vaccineType;
    @BindView(R.id.group)
    RadioGroup group;
    @BindView(R.id.iknow)
    TextView textIKnow;
    @BindView(R.id.fingerImage)
    ImageView fingerImage;
    @BindView(R.id.fingerText)
    TextView fingerText;
    @BindView(R.id.check1)
    RadioButton check1;
    @BindView(R.id.check2)
    RadioButton check2;
    @BindView(R.id.check3)
    RadioButton check3;
    private List<String> list = new ArrayList<>();
    private static final int VID = 6997;    //zkteco device VID  6997
    private static int PID = 772;    //NIDFPSensor PID 根据实际设置
    private boolean mbStart = false;
    private NIDFPSensor mNIDFPSensor = null;

    private boolean mbStop = true;
    private byte[] mBufImage = null;
    private DialogManage dialogManage;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemUIVisible(false);
        setContentView(R.layout.activity_signature);
        ButterKnife.bind(this);
        dialogManage = new DialogManage(this);
        rxPermissions = new RxPermissions(this);
        new UsbConnectUtil(this, this).requestUsb();
        ImmersionBar.with(SignatureActivity.this) .navigationBarColor(R.color.transparent) .statusBarColor(R.color.transparent).hideBar(BarHide.FLAG_HIDE_BAR).init();
        ImmersionBar.with(this, dialogManage).navigationBarColor(R.color.transparent).hideBar(BarHide.FLAG_HIDE_BAR).init();

        dialogManage.setOnDismissListener((dialogInterface) -> {
            ImmersionBar.with(SignatureActivity.this).navigationBarColor(R.color.transparent).statusBarColor(R.color.transparent).hideBar(BarHide.FLAG_HIDE_BAR).init();
            ImmersionBar.destroy(this, dialogManage);
        });
        SpannableString spannableString2 = new SpannableString("      " + getResources().getString(R.string.vaccine_type));
        StyleSpan styleSpan2 = new StyleSpan(Typeface.BOLD);
        spannableString2.setSpan(styleSpan2, 0, 12, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        vaccineType.setText(spannableString2);


        SpannableString spannableString = new SpannableString("      " + getResources().getString(R.string.disease));
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan, 0, 12, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        disease.setText(spannableString);

        list.add("________");
        list.add("1");
        list.add("2");
        list.add("3");
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG, "onCheckedChanged: radiogroup 改变---------------");
                switch (checkedId) {
                    case R.id.check1:
                        for (String s : list) {
                            if (textIKnow.getText().toString().indexOf(s)>0) {
                                textIKnow.setText(textIKnow.getText().toString().replace(s, "1"));
                                break;
                            }
                        }
                        break;
                    case R.id.check2:
                        for (String s : list) {
                            if (textIKnow.getText().toString().indexOf(s)>0) {
                                textIKnow.setText(textIKnow.getText().toString().replace(s, "2"));
                                break;
                            }
                        }
                        break;
                    case R.id.check3:
                        for (String s : list) {
                            if (textIKnow.getText().toString().indexOf(s)>0) {
                                textIKnow.setText(textIKnow.getText().toString().replace(s, "3"));
                                break;
                            }
                        }
                        break;
                }
                //    textIKnow.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                //     textIKnow.getPaint().setAntiAlias(true);//抗锯齿
            }
        });

        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (!permission.granted) {
                        Toast.makeText(SignatureActivity.this, "权限申请失败！", Toast.LENGTH_LONG).show();
                    }
                });

    }


    @OnClick({R.id.clean, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clean:
                entryFinger();
                break;
            case R.id.confirm:
                if (!check1.isChecked() && !check2.isChecked() && !check3.isChecked()) {
                    dialogManage.showInfo("请先选择了解事项！");
                    return;
                }
                if (signaturePad.isEmpty()) {
                    dialogManage.showInfo("请签名！");
                    return;
                }
                if (fingerImage.getDrawable() == null) {
                    dialogManage.showInfo("请先录入指纹！");
                    return;
                }

                //   -具有白色背景的签名位图。
                //   Bitmap bitmap = signaturePad.getSignatureBitmap();
                //具有透明背景的签名位图。
                //   Bitmap tran_bitmap = signaturePad.getTransparentSignatureBitmap();
                //签名可缩放矢量图形文档。
                //   String svg = signaturePad.getSignatureSvg();
                //  imageTran.setImageBitmap(tran_bitmap);
                //fixme 暂时不生成图片
//                    View view1 = LayoutInflater.from(this).inflate(R.layout.confirm_ok, null, false);
//                    Dialog dialog = new Dialog(this);
//                    ImmersionBar.with(this, dialog).init();
//                    Window window = dialog.getWindow();
//                    window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//                    window.setLayout(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
//                    dialog.setContentView(view1);
//                    dialog.setOnDismissListener((dialogInterface) -> {
//                        ImmersionBar.destroy(this, dialog);
//                    });
                Bitmap bitmap1 = screenBitmap(scrollView);
//                    if (bitmap1 != null) {
//                        ((ImageView) view1.findViewById(R.id.image)).setImageBitmap(bitmap1);
//                    }
                //  dialog.show();
                dialogManage.showSuccess("已确认！");
                entryFinger();
                break;
        }
    }

    private Bitmap screenBitmap(NestedScrollView scrollView) {
        String time = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd-HH-mm-ss");
        String path = Environment.getExternalStorageDirectory() + "/finger_image/" + time + "-screen.jpg";
      //  String path= PathUtils.getExternalPicturesPath()+ "/finger_image/" + time + "-screen.png";
        Log.i("Signature", "路径：" + path);
        //FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory() + "/finger_image");
        FileUtils.createOrExistsFile(path);
        /**
         * 截取scrollview的屏幕
         * @param scrollView
         * @return
         */
        int h = 0;
        Bitmap bitmap = null;
        // 获取listView实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundResource(R.color.white);
        }

        // 创建对应大小的bitmap 不包含地下按钮
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h - ConvertUtils.dp2px(70), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    @Override
    public boolean checkUSB(UsbDevice device) {
        return device.getVendorId() == VID && device.getProductId() == PID;
    }

    @Override
    public void openUSB(UsbDevice usbDevice) {
        openDevice();
    }

    @Override
    public void deniedPermission() {

    }

    private void openDevice() {
        // Start fingerprint sensor
        Map fingerprintParams = new HashMap();
        //set vid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_VID, VID);
        //set pid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_PID, PID);
        mNIDFPSensor = NIDFPFactory.createNIDFPSensor(this, TransportType.USBSCSI, fingerprintParams);
        if (mbStart) {
            return;
        }
        try {
            //连接设备
            mNIDFPSensor.open(0);
            //分配读取指纹图像内存(width*height Bytes)
            mBufImage = new byte[mNIDFPSensor.getFpImgWidth() * mNIDFPSensor.getFpImgHeight()];
            entryFinger();
            //   Toast.makeText(this, "设备连接成功，固件版本：" + mNIDFPSensor.getFirmwareVersion()+"，图像宽：" + mNIDFPSensor.getFpImgWidth() + ",图像高：" + mNIDFPSensor.getFpImgHeight(), Toast.LENGTH_SHORT).show();
            mbStart = true;
        } catch (NIDFPException e) {
            //连接失败，尝试重启模块
            try {
                mNIDFPSensor.rebootDevice(0);
            } catch (NIDFPException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            //  Toast.makeText(mContext, "Open device fail.errorcode:\"+ e.getErrorCode() + \"err message:\" + e.getMessage() + \"inner code:\" + e.getInternalErrorCode()", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "指纹设备连接失败", Toast.LENGTH_LONG).show();
            //mTxtReport.setText("Open device fail.errorcode:"+ e.getErrorCode() + "err message:" + e.getMessage() + "inner code:" + e.getInternalErrorCode());
        }
    }


    private void entryFinger() {
        fingerText.setText("请按下手指！");
        mbStop = true;
        imageTran.setImageBitmap(null);
        fingerImage.setImageBitmap(null);
        fingerImage.setImageDrawable(null);
        group.clearCheck();
        for (String s : list) {
            if (textIKnow.getText().toString().indexOf(s)>0) {
                textIKnow.setText(textIKnow.getText().toString().replace(s, list.get(0)));
                break;
            }
        }
        signaturePad.clear();
        if(!executorService.isTerminated()){
            Log.i("SIGNATURE","线程在运行------");
            mbStop=true;
            try {
                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mbStop = false;
        executorService.execute(()->{
            while (!mbStop) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                   mNIDFPSensor.GetFPRawData(0, mBufImage);
                } catch (NIDFPException e) {
                    e.printStackTrace();
                    continue;
                }
                byte[] retQualityScore = new byte[1];
                int ret = mNIDFPSensor.getQualityScore(mBufImage, retQualityScore);
                final byte qualityScore = retQualityScore[0];
                if (1 != ret || qualityScore < 45) {
                    continue;
                }
                showLatestImage();
                byte[] mTestFeature = new byte[512];
                float score = mNIDFPSensor.ImageMatch(0, mBufImage, mTestFeature);
                runOnUiThread(new Runnable() {
                    public void run() {
                        fingerText.setText("录入成功");
                    }
                });
                 mbStop = true;
            }
            Log.i("SINGATURE", "线程断开-------------");
        });



    }


    private void showLatestImage() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (mNIDFPSensor != null) {
                    Bitmap fingerBitmap = ToolUtils.renderCroppedGreyScaleBitmap(mBufImage, mNIDFPSensor.getFpImgWidth(), mNIDFPSensor.getFpImgHeight());
                    fingerImage.setImageBitmap(fingerBitmap);
                }

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloseDevice();  //尝试关闭设备
        // Destroy fingerprint sensor when it's not used
        NIDFPFactory.destroy(mNIDFPSensor);
        mNIDFPSensor = null;

    }

    void CloseDevice() {
        if (mbStart) {
            mbStop = true;  //停止采集线程
            try {
                mNIDFPSensor.close(0);  //关闭设备
            } catch (NIDFPException e) {
                e.printStackTrace();
            }
        }
        mbStart = false;
    }

    private void setSystemUIVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }


}
