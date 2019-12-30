package com.ethink.signature.util;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.ethink.signature.R;

import java.util.Timer;
import java.util.TimerTask;

public class DialogManage extends Dialog {


    private ImageView infoIcon;
    private ProgressBar progress;
    private TextView infoText;
    private Timer timer = new Timer();
    protected Context context;



    public DialogManage(@NonNull Context context) {
        super(context, R.style.dialog_waiting);
        this.context=context;
        View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_info_layout, null, false);
        infoIcon = view.findViewById(R.id.info_icon);
        progress = view.findViewById(R.id.progress);
        infoText = view.findViewById(R.id.info_text);
        int width = ScreenUtils.getScreenWidth();
        int hight = ScreenUtils.getScreenHeight();
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        setContentView(view);
        setCancelable(false);
//        Window window = getWindow();
//        if (window != null) {
//            WindowManager.LayoutParams param = window.getAttributes();
//            param.width = (width / 3);
//            param.height = (width / 3);
//            //alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明，自身不可见。
//            param.alpha = 1.0f;
//            window.setAttributes(param);
//        }

    }


    //加载动画
    public void loading(String str) {
        infoText.setText(str);
        infoIcon.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
//        showAsDropDown(infoIcon, (width / 3), (int) (hight / 2.5));
        if(isShowing()){
            dismiss();
        }
        show();
    }

    //提示信息
    public void showInfo(String msg) {
        infoText.setText(msg);
        progress.setVisibility(View.GONE);
        infoIcon.setVisibility(View.VISIBLE);
        infoIcon.setImageResource(R.drawable.qmui_icon_notify_info);
        //  showAsDropDown(infoIcon);
        if(isShowing()){
            dismiss();
        }
        show();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isShowing()) dismiss();
            }
        }, 1200);
    }

    //成功信息
    public void showSuccess(String msg) {
        infoText.setText(msg);
        progress.setVisibility(View.GONE);
        infoIcon.setVisibility(View.VISIBLE);
        infoIcon.setImageResource(R.drawable.qmui_icon_notify_done);
        //  showAsDropDown(infoIcon);
        if(isShowing()){
            dismiss();
        }
        show();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isShowing()) dismiss();
            }
        }, 1500);
    }
    //成功信息
    public void showError(String msg) {
        infoText.setText(msg);
        progress.setVisibility(View.GONE);
        infoIcon.setVisibility(View.VISIBLE);
        infoIcon.setImageResource(R.drawable.qmui_icon_notify_error);
        //  showAsDropDown(infoIcon);
        if(isShowing()){
            dismiss();
        }
        show();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isShowing()) dismiss();
            }
        }, 1200);
    }
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) return true;
        return super.onKeyDown(keyCode, event);
    }


}
