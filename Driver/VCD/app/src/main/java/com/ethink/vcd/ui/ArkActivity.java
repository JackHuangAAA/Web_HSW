package com.ethink.vcd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.StringUtils;
import com.ethink.vcd.R;
import com.ethink.vcd.controller.ArkController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ArkActivity extends AppCompatActivity {

    private ArkController arkController;
    @BindView(R.id.ed_num)
    EditText editText;
    @BindView(R.id.group)
    RadioGroup radioGroup;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ark);
        ButterKnife.bind(this);
       // this.arkController = new ArkController("/dev/ttyUSB0", 115200);
    }

    @OnClick({R.id.open, R.id.ark_status, R.id.tem_value, R.id.swh_status, R.id.sys_value})
    public void onClick(View view) {
        new Thread(() -> {
            String nu = editText.getText().toString();
            switch (view.getId()) {
                //开门
                case R.id.open:
                    if (radioGroup.getCheckedRadioButtonId() == R.id.radio_mini) {
                        if (StringUtils.isEmpty(nu)) {
                            Toast.makeText(this, "请输入编号", Toast.LENGTH_LONG).show();
                            break;
                        }
                        String[] a = nu.split(",");
                        Set<Integer> set = new HashSet<>();
                        for (String s : a) {
                            if (!StringUtils.isEmpty(s)) {
                                try {
                                    set.add(Integer.parseInt(s));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                        }
                        if (!set.isEmpty()) {
                            String result = arkController.openDrawer(set);
                            logger.info("开门结果：" + result);
                        }
                    } else {
                        boolean result = arkController.bigOpen();
                        logger.info("冷藏柜开门结果：" + result);
                    }

                    break;
                //查询主控板状态
                case R.id.ark_status:
                    int re = arkController.arkStatus();
                    logger.info("主控板状态：" + re);
                    break;
                //查询开关状态
                case R.id.swh_status:
                    if (radioGroup.getCheckedRadioButtonId() == R.id.radio_mini) {
                        //疫苗柜
                        arkController.switchStatus();
                    } else {
                        //冷藏柜
                        arkController.bigSwitchStatus();
                    }
                    break;
                //查询温度
                case R.id.tem_value:
                    // arkController.temperature(b);
                    if (StringUtils.isEmpty(nu)) {
                        Toast.makeText(this, "请输入编号", Toast.LENGTH_LONG).show();
                        break;
                    }
                    String[] a = nu.split(",");
                    Set<Integer> set = new HashSet<>();
                    for (String s : a) {
                        if (!StringUtils.isEmpty(s)) {
                            try {
                                set.add(Integer.parseInt(s));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                    if (!set.isEmpty()) {
                        logger.info("当前温度：" + arkController.temperature(set));
                    }
                    break;
                //查询程序版本信息
                case R.id.sys_value:
                    arkController.sysValue();
                    break;

            }
        }).start();
    }


}
