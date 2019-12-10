package com.ethink.lineup.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ethink.lineup.Const;
import com.ethink.lineup.R;
import com.ethink.lineup.adapter.ViewPagerAdapter;
import com.ethink.lineup.controller.PrintController;
import com.ethink.lineup.ui.fragment.ImageFragment;
import com.ethink.plugin.message.EventMessage;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrintActivity extends AppCompatActivity {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private PrintController printController;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private int[] images = {R.drawable.print1, R.drawable.print2, R.drawable.print3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setSystemUIVisible(false);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);
        //  printController = new PrintController(this);
        //   printController.printConnStatus();
//        logger.info("宽={} 高={}", ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        List<Fragment> fragmentList = new ArrayList<>();
        for (int image : images) {
            fragmentList.add(ImageFragment.newInstance(image));
        }
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), R.string.appbar_scrolling_view_behavior, fragmentList));
        viewPager.setOnClickListener((v) -> {
            if (viewPager.getCurrentItem() == 1 && printController != null) {
                printController.print("疫苗排队", "21", "我的号码21", 10);
                viewPager.setCurrentItem(2, true);
            }

        });

    }

    @Subscriber(mode = ThreadMode.MAIN, tag = Const.SCAN_EVENT)
    public void ScanResult(EventMessage eventMessage) {
        logger.info("接受扫码信息-----");
        if (eventMessage != null) {
            String str = eventMessage.getString("data");
            Toast.makeText(this, "扫码信息：" + str, Toast.LENGTH_LONG).show();
            if (viewPager.getCurrentItem() != 1) {
                viewPager.setCurrentItem(1, true);
            }
        }
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

// 扫码设备usb device vid=1504  pid=6400 name=Symbol Bar Code Scanner
// 触摸屏usb device vid=9589  pid=1025 name=CoolTouchR System
//打印机usb device vid=1305  pid=8211 name=IP1000 Printer USB001

}
