package com.ethink.lineup.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ethink.lineup.R;

public class StartActivity extends AppCompatActivity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		Button openPrint = (Button)findViewById(R.id.OpenPrintTask_btn);
		openPrint.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.OpenPrintTask_btn){
			Intent intent = new Intent(StartActivity.this,MainActivity.class);
			startActivity(intent);
		}
		
	}
}
