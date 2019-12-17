package com.ethink.lineup.ui;

import android.os.Bundle;
import android.view.View;

import com.ethink.lineup.Const;
import com.ethink.lineup.R;
import com.ethink.lineup.controller.PrintController;
import com.ethink.plugin.message.EventMessage;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class PrintActivity extends AppCompatActivity {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private PrintController printController;



    private  int num=1;
    private int current=0;
    private int[] images = {R.drawable.print1, R.drawable.print2, R.drawable.print3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemUIVisible(false);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
         printController = new PrintController(this);
           printController.printConnStatus();
//        logger.info("宽={} 高={}", ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
      /*  List<Fragment> fragmentList = new ArrayList<>();
        for (int image : images) {
            fragmentList.add(ImageFragment.newInstance(image));
        }
        viewPager.setScroll(false);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), R.string.appbar_scrolling_view_behavior, fragmentList));
        emptyView.setOnClickListener((v)->{
            int cur=viewPager.getCurrentItem();
            if (cur == 1 && printController != null) {
                printController.print("疫苗排队", String.valueOf(num), String.format("我的号码%s",num), current);
                num++;
                current+=5;
                viewPager.setCurrentItem(2, true);
            }
              if(cur==2){
                viewPager.setCurrentItem(0, false);
            }
        });*/
    }



    @Subscriber(mode = ThreadMode.MAIN,tag = Const.SCAN_EVENT)
    public void ScanResult(EventMessage eventMessage) {
        if (eventMessage != null) {
            //String str = eventMessage.getString("data");
            printController.print("疫苗排队", String.valueOf(current), String.format("我的号码%s",num), num);
            num++;
            current+=5;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
