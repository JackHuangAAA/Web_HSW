package com.ethink.lineup.print_nh80m;


import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ethink.lineup.R;

public class USBActivity extends Activity {
	
	//Returned Value Statement
	public static final int POS_SUCCESS=1000;		//success	
	public static final int ERR_PROCESSING = 1001;	//processing error
	public static final int ERR_PARAM = 1002;		//parameter error
	
	
	//Print Mode
	private static final int PRINT_MODE_STANDARD = 0;
	private static final int PRINT_MODE_PAGE = 1;
	public static int printMode = PRINT_MODE_STANDARD;
	
	//SDK variable
	public static POSSDK pos_usb = null;
	private POSInterfaceAPI interface_usb = null;
	private TestPrintInfo testprint;
	private int error_code = 0;
	private static boolean sdk_flag = false;
	
	//Button variable
	private Button openBtn; 	// Open Port
	private Button closeBtn; 	// Close Port

	private RadioGroup radiogroup; 
	private RadioButton standardmodeRB; // Stand Mode
	private RadioButton pagemodeRB; // Page Mode

	private Button printBitmapBtn; // Test Print Bitmap
	private Button printTextBtn; // Test Print Text
	private Button printBarcodeBtn; // Test Print BarCode
	private Button printPDF417Btn; // Test Print PDF417
	private Button printQRBtn; // Test Print QR
	private Button printGS1Btn; //Test Print GS1
	private Button queryStateBtn; // Get Printer State
	private Button printFeedLineBtn; //Test Print FeedPaper
	private Button printCutPaperBtn; //Test Print CutPaper
	private Button printRasterBitmapBtn; //Test Print RasterCharacter
	private Button printUserDefCharBtn;	//Test Print User-defined character
	private Button cashDrawerOpenBtn;	//open cash drawer
	
	private RadioButton r1;
	private RadioButton r2;
	private RadioButton r3;
	private RadioButton r4;
	private RadioButton r5;
	private RadioButton r6;
	private RadioButton r7;
	private RadioButton r8;

	byte btemps[] = new byte[8];
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usb_demo);
        interface_usb = new POSUSBAPI(this);
        
        //Get State RadioButton Object
		r1 = (RadioButton) findViewById(R.id.usb_r1);
		r2 = (RadioButton) findViewById(R.id.usb_r2);
		r3 = (RadioButton) findViewById(R.id.usb_r3);
		r4 = (RadioButton) findViewById(R.id.usb_r4);
		r5 = (RadioButton) findViewById(R.id.usb_r5);
		r6 = (RadioButton) findViewById(R.id.usb_r6);
		r7 = (RadioButton) findViewById(R.id.usb_r7);
		r8 = (RadioButton) findViewById(R.id.usb_r8);
		
		//TestPrint variable
		testprint = new TestPrintInfo();
        
		//**************************************************************************************************
        //Open Port
        openBtn = (Button)findViewById(R.id.usb_openDevice);
        openBtn.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {				
				//specified pid and vid test
				error_code = interface_usb.OpenDevice();
				if(error_code != POS_SUCCESS)
				{
					Toast.makeText(USBActivity.this, "Failed to open port, please try again.", Toast.LENGTH_LONG).show();
				}
				else
				{
					pos_usb = new POSSDK(interface_usb);
					sdk_flag = true;
					Toast.makeText(USBActivity.this, "Open Port OK!.", Toast.LENGTH_LONG).show();
					
				}
				
			}
        });
        
        //*************************************************************************************************
        //Close Port
        closeBtn = (Button)findViewById(R.id.usb_CloseDevice);
        closeBtn.setOnClickListener(new OnClickListener(){
        	
        	public void onClick(View v){
        		error_code = interface_usb.CloseDevice();
        		if(error_code != POS_SUCCESS)
        		{
        			Toast.makeText(USBActivity.this, "Failed to close port.", Toast.LENGTH_LONG).show();
        		}
        		else
        		{
        			sdk_flag = false;
        		}
        	}
        });
        
        //*************************************************************************************************
		// Choose PrintMode
		radiogroup = (RadioGroup) findViewById(R.id.usb_radiogroup);
		standardmodeRB = (RadioButton) findViewById(R.id.usb_radiobutton_standard);
		pagemodeRB = (RadioButton) findViewById(R.id.usb_radiobutton_page);
		radiogroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if(sdk_flag == true){
							if (checkedId == standardmodeRB.getId()) {
								printMode = PRINT_MODE_STANDARD;
								error_code = pos_usb.systemSelectPrintMode(PRINT_MODE_STANDARD);
							} else if (checkedId == pagemodeRB.getId()) {
								printMode = PRINT_MODE_PAGE;
								error_code = pos_usb.systemSelectPrintMode(PRINT_MODE_PAGE);
							}
						}
						else
						{
							Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
						}
						
					}

			});
        
		//**************************************************************************************************
        //PrintBitmap
        printBitmapBtn = (Button)findViewById(R.id.usb_print_bitmap);
        printBitmapBtn.setOnClickListener(new OnClickListener(){
        	
        	public void onClick(View v){
        		if(sdk_flag == true)
        		{
    			    Intent intent = new Intent();
    				intent.setClass(USBActivity.this, PrintBitmapActivity.class);
    				startActivity(intent);		
        		}
        		else
        		{
        			Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}

        	}
        });
        
        //*************************************************************************************************
        //PrintText
        printTextBtn = (Button)findViewById(R.id.usb_print_text);
        printTextBtn.setOnClickListener(new OnClickListener(){
        	
        	public void onClick(View v){
        		if(sdk_flag == true)
        		{
    			    Intent intent = new Intent();
    				intent.setClass(USBActivity.this, PrintTextActivity.class);
    				startActivity(intent);        			
        		}
        		else
        		{
        			Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}

        	}
        });
        
        //**************************************************************************************************
		// PirntBarCode
		printBarcodeBtn = (Button) findViewById(R.id.usb_print_barcode);
		printBarcodeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
				    Intent intent = new Intent();
					intent.setClass(USBActivity.this, BarCodeActivity.class);
					startActivity(intent);	
				}        		
				else
        		{
        			Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}
			}
		});
        
		//**************************************************************************************************


		//**************************************************************************************************
		// PrintQR
		printQRBtn = (Button) findViewById(R.id.usb_print_qr);
		printQRBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
				    Intent intent = new Intent();
					intent.setClass(USBActivity.this, QRCodeActivity.class);
					startActivity(intent);			
				}
				else
        		{
        			Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}

			}
		});
		
		//**************************************************************************************************

		
		//**************************************************************************************************
		// Print RasterCharacter
		printRasterBitmapBtn = (Button)findViewById(R.id.usb_print_rasterimg);
		printRasterBitmapBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
				    Intent intent = new Intent();
					intent.setClass(USBActivity.this, RasterBitmapActivity.class);
					startActivity(intent);			
				}
				else
        		{
        			Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}
			}
		});
		//*************************************************************************************************
		// Print User-defined character 
		printUserDefCharBtn = (Button)findViewById(R.id.usb_print_userfont);
		printUserDefCharBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
					error_code = testprint.TestUserDefinedCharacter(pos_usb, printMode);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(USBActivity.this, "Failed to Print UserDefinedCharacter.",Toast.LENGTH_LONG).show();	
					}
				}
				else
				{
					Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();	
				}
				
			}
			
		});
		//**************************************************************************************************
		//FeedPaper
		printFeedLineBtn = (Button)findViewById(R.id.usb_print_feed);
		printFeedLineBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
					error_code = testprint.TestFeedLine(pos_usb, printMode);
	        		if(error_code != POS_SUCCESS)
	        		{
	        			Toast.makeText(USBActivity.this, "Failed to Feed Paper.",Toast.LENGTH_LONG).show();
	        		}					
				}
				else
				{
					Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();	
				}

			}
		});
		
		//*************************************************************************************************
		//CutPaper
		printCutPaperBtn = (Button)findViewById(R.id.usb_print_cut);
		printCutPaperBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
					error_code = testprint.TestCutPaper(pos_usb, printMode);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(USBActivity.this, "Failed to Cut Paper.",Toast.LENGTH_LONG).show();	
					}
				}
				else
				{
					Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();	
				}
				
			}
			
		});
		//*************************************************************************************************

		//**************************************************************************************************
		// Get Printer State
		queryStateBtn = (Button) findViewById(R.id.usb_query_state);
		queryStateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				byte[] posStateTemp = new byte[1];
				int result;
				if(sdk_flag == true){
					result = testprint.POSNETQueryStatus(pos_usb,posStateTemp);
					if (result == POS_SUCCESS) {
						byte btemp[] = new byte[8];
						byte bitindex = 1;
						for (int i = 0; i < btemp.length; i++) {
							btemp[i] = (byte) (posStateTemp[0] & bitindex);
							bitindex = (byte) (bitindex << 1);

						}
						if (btemp[0] == 0) {	//CashDrawer Open
							r1.setChecked(false);	
						} else {
							r1.setChecked(true);
						}
						if (btemp[1] == 0) {	//Offline
							r2.setChecked(false);
						} else {
							r2.setChecked(true);
						}
						if (btemp[2] == 0) {	//Cover Open
							r3.setChecked(false);
						} else {
							r3.setChecked(true);
						}
						if (btemp[3] == 0) {	//Feeding
							r4.setChecked(false);
						} else {
							r4.setChecked(true);
						}
						if (btemp[4] == 0) {	//Printer Error
							r5.setChecked(false);
						} else {
							r5.setChecked(true);
						}
						if (btemp[5] == 0) {	//Cutter Error
							r6.setChecked(false);
						} else {
							r6.setChecked(true);
						}
						if (btemp[6] == 0) {	//Paper	Near End
							r7.setChecked(false);
						} else {
							r7.setChecked(true);
						}
						if (btemp[7] == 0) {	//Paper End
							r8.setChecked(false);
						} else {
							r8.setChecked(true);
						}
						btemp = null;
					} else {
						Toast.makeText(USBActivity.this, "Failed to read Data.",
								Toast.LENGTH_LONG).show();
					}				
				}else{
					Toast.makeText(USBActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
				}

			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}     
}
