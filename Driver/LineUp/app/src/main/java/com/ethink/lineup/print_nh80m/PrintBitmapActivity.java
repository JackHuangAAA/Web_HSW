package com.ethink.lineup.print_nh80m;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PrintBitmapActivity extends Activity {
	//Parameter variable
	private int hd_type = 0;
	private String bitmap_name = null;
	
	//Operate control
	private Button btnOK, btnRet;
	private EditText txtBitmapName;
	private RadioGroup radiogroup; 	
	private RadioButton RAMmodeRB; 	// RAM
	private RadioButton FALSEmodeRB; // FALSH
	private RadioButton GRAYmodeRB; // GRAY
	
	//TestPrint variable
	private TestPrintInfo testprint;
	private int error_code = 0;
	
	//Returned Value Statement
	public static final int POS_SUCCESS=1000;		//success
	public static final int ERR_PROCESSING = 1001;	//fail
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bitmapprint);
        
		//Choose Type(RAM or FALSH)
		radiogroup = (RadioGroup) findViewById(R.id.bitmap_group);
		RAMmodeRB = (RadioButton) findViewById(R.id.bitmap_ram_radio);
		FALSEmodeRB = (RadioButton) findViewById(R.id.bitmap_falsh_radio);
		GRAYmodeRB = (RadioButton) findViewById(R.id.bitmap_gray_radio);
		radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == RAMmodeRB.getId()) {
					hd_type = 0;
				} else if (checkedId == FALSEmodeRB.getId()) {
					hd_type = 1;
				} else if (checkedId == GRAYmodeRB.getId()) {
					hd_type = 2;
				}
			}
	});
		
		//Get BiamapName EditText Object
		txtBitmapName = (EditText)findViewById(R.id.bitmap_imgName);
		
		//****************************************************************************************************
		//Get parameter and print
		btnOK = (Button)findViewById(R.id.bitmap_print_but);
		btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
			
				testprint = new TestPrintInfo();
				
				//Get BitmapName
				bitmap_name = txtBitmapName.getText().toString();
				
				//USB
				if(MainActivity.port_type == MainActivity.USBPORT)
				{
					error_code = testprint.TestPrintBitmap(USBActivity.pos_usb, USBActivity.printMode, bitmap_name, hd_type);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(PrintBitmapActivity.this, "Failed to print bitmap.",Toast.LENGTH_LONG).show();
					}
				}
				//COM
				else if(MainActivity.port_type == MainActivity.SERIALPORT)
				{
					error_code = testprint.TestPrintBitmap(SerialActivity.pos_com, SerialActivity.printMode, bitmap_name, hd_type);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(PrintBitmapActivity.this, "Failed to print bitmap.",Toast.LENGTH_LONG).show();
					}	
				}
				//WIFI
				else if(MainActivity.port_type == MainActivity.WIFIPORT)
				{
					error_code = testprint.TestPrintBitmap(WIFIActivity.pos_wifi, WIFIActivity.printMode, bitmap_name, hd_type);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(PrintBitmapActivity.this, "Failed to print bitmap.",Toast.LENGTH_LONG).show();
					}	
					
				}
				//Bluetooth
				else if(MainActivity.port_type == MainActivity.BLUETOOTHPORT)
				{
					error_code = testprint.TestPrintBitmap(BlueActivity.pos_blue, BlueActivity.printMode, bitmap_name, hd_type);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(PrintBitmapActivity.this, "Failed to print bitmap.",Toast.LENGTH_LONG).show();
					}		
				}
				//NET
				else if(MainActivity.port_type == MainActivity.NETPORT)
				{
					error_code = testprint.TestPrintBitmap(NETActivity.pos_net, NETActivity.printMode, bitmap_name, hd_type);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(PrintBitmapActivity.this, "Failed to print bitmap.",Toast.LENGTH_LONG).show();
					}	
					
				}
			}});
		//*************************************************************************************************
		//Back
		btnRet = (Button)findViewById(R.id.bitmap_ret_but);
		btnRet.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				back();
			}
		});
    }
	private void back() {
		finish();
	}

}
