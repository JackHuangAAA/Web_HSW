package com.ethink.lineup.ui;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.blankj.utilcode.util.TimeUtils;
import com.ethink.lineup.R;
import com.ethink.lineup.controller.USBDeviceFace;
import com.ethink.lineup.controller.UsbConnectUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class USBPrintActivity extends AppCompatActivity implements USBDeviceFace {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @BindView(R.id.swit_but)
    Switch switBut;
    @BindView(R.id.print_text)
    Button printText;
    @BindView(R.id.print_feed_line)
    Button printFeedLine;
    @BindView(R.id.cut_paper)
    Button cutPaper;
    private static POSSDK pos_sdk = null;
    private POSInterfaceAPI interface_usb = null;

    private int FontType = POSSDK.FontTypeChinese;
    private int FontStyle = POSSDK.FontStyleBold;
    private int LineHeight = 50;

    private static final int PRINT_MODE_STANDARD = 0;
    private static final int PRINT_MODE_PAGE = 1;
    private int mode = PRINT_MODE_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usbprint);
        ButterKnife.bind(this);
        //申请usb权限
        new UsbConnectUtil(this, this).requestUsb();
        switBut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mode = isChecked ? PRINT_MODE_STANDARD : PRINT_MODE_PAGE;
            }
        });
    }

    @OnClick({R.id.print_text, R.id.print_feed_line, R.id.cut_paper})
    public void onClick(View view) {
        int error_code = 0;
        switch (view.getId()) {
            case R.id.print_text:
                logger.info("当前mode {}", mode);
                strand("疫苗排队系统","4",8);
                break;
            case R.id.print_feed_line:
                error_code = pos_sdk.systemFeedLine(3);
                logger.info("走纸{}", error_code);
                break;
            case R.id.cut_paper:
                error_code = pos_sdk.systemCutPaper(POSSDK.CutPartAfterFeed, 0);
                logger.info("切纸纸{}", error_code);
                break;
        }


    }


    private void strand(String title,String num, int current) {
        try {
            byte[] send_buf;
            int error_code = 0;
            int error = pos_sdk.systemReset();
            logger.info("打印机复位 {}", error);
            error_code = pos_sdk.textStandardModeAlignment(POSSDK.TextAlignmentCenter);
            logger.info("TestPrintText: 打印 -----------textStandardModeAlignment--------" + error_code);
            //set the horizontal and vertical motion units
            error_code = pos_sdk.systemSetMotionUnit(200, 200);
            logger.info("TestPrintText: 打印 -----------systemSetMotionUnit--------" + error_code);
            //set line height
            error_code = pos_sdk.textSetLineHeight(30);
            logger.info("TestPrintText: 打印 -----------textSetLineHeight--------" + error_code);
            error_code = pos_sdk.textSelectFont( POSSDK.FontTypeChinese, POSSDK.FontStyleBold);
            logger.info("TestPrintText: 打印 -----------textSelectFont--------" + error_code);
            //set character size
            error_code = pos_sdk.textSelectFontMagnifyTimes(2, 2);
              error_code = pos_sdk.systemFeedLine(1);
            //print text
            send_buf = title.getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
            //feed line
            error_code = pos_sdk.systemFeedLine(2);
            error_code = pos_sdk.textSelectFontMagnifyTimes(3, 3);
            send_buf = num.getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
            error_code = pos_sdk.systemFeedLine(2);
            //改变显示位置
            error_code = pos_sdk.textStandardModeAlignment(POSSDK.TextAlignmentLeft);
            logger.info("TestPrintText: 打印改变位置 -----------textStandardModeAlignment--------" + error_code);
            error_code = pos_sdk.textSelectFontMagnifyTimes(1, 1);
            send_buf = String.format("您前面还有%s位，请注意叫号!",current).getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
            error_code = pos_sdk.systemFeedLine(2);
            String info="您也可以手机扫码实时关注排队情况，以防过号!";
            send_buf = info.getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
            //改变显示位置打印条码
            error_code = pos_sdk.systemFeedLine(2);
            error_code = pos_sdk.textStandardModeAlignment(POSSDK.TextAlignmentCenter);
            error_code = pos_sdk.barcodePrintQR(info,info.length(), 0, 7, 1, 0);
            error_code = pos_sdk.systemFeedLine(1);
            error_code = pos_sdk.textStandardModeAlignment(POSSDK.TextAlignmentRight);
            String time= TimeUtils.getNowString()+"  ";
            send_buf = time.getBytes("GB18030");
            error_code = pos_sdk.textPrint(send_buf, send_buf.length);
            error_code = pos_sdk.systemCutPaper(POSSDK.CutFullImmdediately  , 10);
            logger.info("切纸纸{}", error_code);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkUSB(UsbDevice usbDevice) {
        return usbDevice.getVendorId() == 5455 && usbDevice.getProductId() == 5455;
    }

    @Override
    public void openUSB(UsbDevice usbDevice) {
        interface_usb = new POSUSBAPI(this);
        pos_sdk = new POSSDK(interface_usb);
        int result = interface_usb.OpenDevice(5455, 5455);
        logger.info("打开USB返回 {}", result);
        int error = pos_sdk.systemReset();
        logger.info("打印机复位 {}", error);
    }

    @Override
    public void deniedPermission() {

    }
}
