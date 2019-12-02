package com.ethink.snd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.StringUtils;
import com.ethink.snd.R;
import com.ethink.snd.controller.PrintController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****
 *
 * 存折打印机
 * **/
public class PrintActivity extends AppCompatActivity {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private PrintController printController;
    @BindView(R.id.edit)
    EditText editText;
    @BindView(R.id.tv_change)
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);
        printController=new PrintController("/dev/ttyS0",19200);
    }

    @OnClick({R.id.init,R.id.clean_err,R.id.print_status,R.id.exit,R.id.province,R.id.city,R.id.stop,R.id.paper_status,R.id.print,R.id.inpaper,R.id.measure,R.id.stripe})
    public void OnClick(View view){
        new Thread(()->{
        switch (view.getId()){
            case R.id.print:
                String str=editText.getText().toString();
                    //fixme \n换行不会到行首 \r\n回车另起一行
                String print="\r\n\r\n\r\n1 201901111 入账 RMB   存入      +999999999   07    ChinaBank";
                printController.print(print);
                break;
            case R.id.stop:
                printController.stop();
                break;
            case R.id.init:
                printController.initPrint();
                break;
            case R.id.clean_err:
                printController.clearError();
                break;
            case R.id.print_status:
                printController.printStatus();
                break;
            case R.id.exit:
                printController.exitPaper();
                break;
            case R.id.stripe:
                printController.readStripe();
                break;
            case R.id.inpaper:
                printController.inPaper();
                break;
            case R.id.measure:
                printController.measurement();
                break;
            case R.id.paper_status:
                printController.paperStatus();
                break;
            case R.id.province:
                String num=editText.getText().toString();
                //fixme \n换行不会到行首 \r\n回车另起一行
                if(StringUtils.isEmpty(num))return;
              String  data = PrintController.provinceFormat(true,"H7N9","20191128","xxx工厂","xxx医院","手臂","李四");
                printController.zheProvince(Integer.parseInt(num),data);
                break;
            case R.id.city:
                String n=editText.getText().toString();
                //fixme \n换行不会到行首 \r\n回车另起一行
                if(StringUtils.isEmpty(n))return;
                String  data1 = PrintController.cityFormat("H7N9疫苗","20191128","xxx工厂","xxx医院","手臂","李四");
                printController.hangCity(Integer.parseInt(n),data1);
                break;
        }
        }).start();
    }

}
