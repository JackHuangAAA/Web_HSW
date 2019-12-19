package com.ethink.snd.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.ScreenUtils;
import com.ethink.snd.R;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 *
 * Android手写签名板
 * **/
public class SignatureActivity extends AppCompatActivity {
    protected Logger logger = LoggerFactory.getLogger(getClass());

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
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        ButterKnife.bind(this);
        // noticeInfo.setText(Html.fromHtml(info+"\n"));
        //  noticeInfo.setText(info);
        ImmersionBar.with(this).statusBarColor(R.color.white).hideBar(BarHide.FLAG_HIDE_BAR).init();
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
                switch (checkedId) {
                    case R.id.check1:
                        for (String s:list){
                            if(textIKnow.getText().toString().contains(s)){
                                textIKnow.setText(textIKnow.getText().toString().replace(s,"1"));
                                break;
                            }
                        }
                        break;
                    case R.id.check2:
                        for (String s:list){
                            if(textIKnow.getText().toString().contains(s)){
                                textIKnow.setText(textIKnow.getText().toString().replace(s,"2"));
                                break;
                            }
                        }
                        break;
                    case R.id.check3:
                        for (String s:list){
                            if(textIKnow.getText().toString().contains(s)){
                                textIKnow.setText(textIKnow.getText().toString().replace(s,"3"));
                                break;
                            }
                        }
                        break;

                }
            //    textIKnow.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
           //     textIKnow.getPaint().setAntiAlias(true);//抗锯齿
            }
        });


    }

    @OnClick({R.id.clean, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clean:
                signaturePad.clear();
                imageTran.setImageBitmap(null);
                break;
            case R.id.confirm:
                if (!signaturePad.isEmpty()) {
                    //   -具有白色背景的签名位图。
                 //   Bitmap bitmap = signaturePad.getSignatureBitmap();
                    //具有透明背景的签名位图。
                 //   Bitmap tran_bitmap = signaturePad.getTransparentSignatureBitmap();
                    //签名可缩放矢量图形文档。
                 //   String svg = signaturePad.getSignatureSvg();
                  //  imageTran.setImageBitmap(tran_bitmap);

                    View view1 = LayoutInflater.from(this).inflate(R.layout.confirm_ok, null, false);
                    Dialog dialog = new Dialog(this);
                    ImmersionBar.with(this, dialog).init();
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    window.setLayout(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
                    dialog.setContentView(view1);
                    dialog.setOnDismissListener((dialogInterface) -> {
                        ImmersionBar.destroy(this, dialog);
                    });
                    Bitmap bitmap1 = screenBitmap(scrollView);
                    if (bitmap1 != null) {
                        ((ImageView) view1.findViewById(R.id.image)).setImageBitmap(bitmap1);
                    }
                    dialog.show();
                   //清除笔迹
                //    signaturePad.clear();

                }
                break;
        }
    }

    private Bitmap screenBitmap(NestedScrollView scrollView) {
        String path = getFilesDir().getPath() + "screen.png";
        logger.info("路径：" + path);
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
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h - 70, Bitmap.Config.ARGB_8888);
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
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
        return bitmap;
    }

}
