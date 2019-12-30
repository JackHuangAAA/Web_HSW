package com.ethink.signature.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ScreenUtils;
import com.ethink.signature.R;

import java.util.Timer;
import java.util.TimerTask;

public class PopupManage {


    private ImageView infoIcon;
    private ProgressBar progress;
    private TextView infoText;
    private Timer timer = new Timer();
    protected Context context;
    private PopupWindow popupWindow;



    public PopupManage(@NonNull Context context) {
        this.context = context;
        View  view = LayoutInflater.from(context).inflate(R.layout.popupwindow_info_layout, null, false);
        infoIcon = view.findViewById(R.id.info_icon);
        progress = view.findViewById(R.id.progress);
        infoText = view.findViewById(R.id.info_text);
        int width = ScreenUtils.getScreenWidth();
        int hight = ScreenUtils.getScreenHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        popupWindow = new PopupWindow(view,300,250);
        // 设置背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context,R.color.black)));
        // 外部点击事件
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(false);

    }


    //加载动画
    public void loading(String str) {
        infoText.setText(str);
        infoIcon.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
//        showAsDropDown(infoIcon, (width / 3), (int) (hight / 2.5));
        if (popupWindow == null) return;
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
      popupWindow.showAsDropDown(infoIcon);
    }

    public void destroy() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            timer.cancel();
            timer = null;
        }

    }

    //提示信息
    public void showInfo(String msg) {
        infoText.setText(msg);
        progress.setVisibility(View.GONE);
        infoIcon.setVisibility(View.VISIBLE);
        infoIcon.setImageResource(R.drawable.qmui_icon_notify_info);
        //  showAsDropDown(infoIcon);
         show(1);

    }

    private void show(double time) {
        if (popupWindow == null) return;
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow.showAsDropDown(infoText,0,0, Gravity.CENTER);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (popupWindow.isShowing()) popupWindow.dismiss();
            }
        }, (int) time * 1000);

    }

    //成功信息
    public void showSuccess(String msg) {
        infoText.setText(msg);
        progress.setVisibility(View.GONE);
        infoIcon.setVisibility(View.VISIBLE);
        infoIcon.setImageResource(R.drawable.qmui_icon_notify_done);
        //  showAsDropDown(infoIcon);
        show(1.5);
    }

    //成功信息
    public void showError(String msg) {
        infoText.setText(msg);
        progress.setVisibility(View.GONE);
        infoIcon.setVisibility(View.VISIBLE);
        infoIcon.setImageResource(R.drawable.qmui_icon_notify_error);
        //  showAsDropDown(infoIcon);
        show(1.5);
    }

}
