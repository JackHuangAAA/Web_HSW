package com.ethink.lineup.print_nh80m;

import java.io.File;

import POSAPI.POSInterfaceAPI;
import POSAPI.POSSerialAPI;
import POSSDK.POSSDK;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ethink.lineup.R;

public class SerialActivity extends Activity {
	
	//Returned Value Statement
	public static final int POS_SUCCESS=1000;		//success	
	public static final int ERR_PROCESSING = 1001;	//processing error
	public static final int ERR_PARAM = 1002;		//parameter error
	

	//Print Mode
	private static final int PRINT_MODE_STANDARD = 0;
	private static final int PRINT_MODE_PAGE = 1;
	public static int printMode = PRINT_MODE_STANDARD;
	
	
	//SDK variable
	public static POSSDK pos_com = null;
	private POSInterfaceAPI interface_com = null;
	private int error_code = 0;
	private TestPrintInfo testprint;
	private boolean sdk_flag = false;
	
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
	
	private RadioButton r1;
	private RadioButton r2;
	private RadioButton r3;
	private RadioButton r4;
	private RadioButton r5;
	private RadioButton r6;
	private RadioButton r7;
	private RadioButton r8;
	byte btemps[] = new byte[8];
	
	private Spinner baudrate_spi;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial_demo);
        interface_com = new POSSerialAPI();
     
        //Get State RadioButton Object
		r1 = (RadioButton) findViewById(R.id.com_r1);
		r2 = (RadioButton) findViewById(R.id.com_r2);
		r3 = (RadioButton) findViewById(R.id.com_r3);
		r4 = (RadioButton) findViewById(R.id.com_r4);
		r5 = (RadioButton) findViewById(R.id.com_r5);
		r6 = (RadioButton) findViewById(R.id.com_r6);
		r7 = (RadioButton) findViewById(R.id.com_r7);
		r8 = (RadioButton) findViewById(R.id.com_r8);
		
		//TestPrint variable
		testprint = new TestPrintInfo();
		
        //**************************************************************************************************
        //Set baud rate
    	baudrate_spi = (Spinner)findViewById(R.id.com_baudrate_spi);
        String[] fonttype_items = {"1200","2400","4800","9600","19200","38400","57600","115200"};
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fonttype_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	baudrate_spi.setAdapter(adapter);
    	baudrate_spi.setSelection(7);
    	
		//************************************************************************************************
	    //Open Port
        openBtn = (Button)findViewById(R.id.com_openDevice);
        openBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String port_name = null;
				String spi_str;
				int baud_rate = 0;
				TextView set_port = (TextView) findViewById(R.id.com_port);
				//get port name
				port_name = (String)set_port.getText().toString();
				//get baud rate 
				spi_str = baudrate_spi.getSelectedItem().toString();
				baud_rate = Integer.parseInt(spi_str);
				error_code = interface_com.OpenDevice(new File(port_name),baud_rate);
				if(error_code != POS_SUCCESS)
				{
					Toast.makeText(SerialActivity.this, "Failed to open port, please try again.", Toast.LENGTH_LONG).show();
				}
				else
				{
					pos_com = new POSSDK(interface_com);
					sdk_flag = true;
					Toast.makeText(SerialActivity.this, "Open Port OK!.", Toast.LENGTH_LONG).show();
				}
				
			}
        });
        
        //**************************************************************************************************
        //Close Port
        closeBtn = (Button)findViewById(R.id.com_CloseDevice);
        closeBtn.setOnClickListener(new OnClickListener(){
        	
        	public void onClick(View v){
        		error_code = interface_com.CloseDevice();
        		if(error_code != POS_SUCCESS)
        		{
        			Toast.makeText(SerialActivity.this, "Failed to close port.", Toast.LENGTH_LONG).show();
        		}
        		else
        		{
        			sdk_flag = false;
        		}
        	}
        });
        
        //**************************************************************************************************
		// Choose PrintMode
		radiogroup = (RadioGroup) findViewById(R.id.com_radiogroup);
		standardmodeRB = (RadioButton) findViewById(R.id.com_radiobutton_standard);
		pagemodeRB = (RadioButton) findViewById(R.id.com_radiobutton_page);
		radiogroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if(sdk_flag == true){
							if (checkedId == standardmodeRB.getId()) {
								printMode = PRINT_MODE_STANDARD;
								error_code = pos_com.systemSelectPrintMode(PRINT_MODE_STANDARD);
							} else if (checkedId == pagemodeRB.getId()) {
								printMode = PRINT_MODE_PAGE;
								error_code = pos_com.systemSelectPrintMode(PRINT_MODE_PAGE);
							}						
						}
						else
						{
							Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
						}

					}
			});
        
		//**************************************************************************************************
        // PrintBitmap
        printBitmapBtn = (Button)findViewById(R.id.com_print_bitmap);
        printBitmapBtn.setOnClickListener(new OnClickListener(){
        	
        	public void onClick(View v){
        		if(sdk_flag == true)
        		{
    			    Intent intent = new Intent();
    				intent.setClass(SerialActivity.this, PrintBitmapActivity.class);
    				startActivity(intent);
        		}
        		else
        		{
        			Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}
        	}
        });
        
        //**************************************************************************************************
        //PrintText
        printTextBtn = (Button)findViewById(R.id.com_print_text);
        printTextBtn.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		if(sdk_flag == true)
        		{
    			    Intent intent = new Intent();
    				intent.setClass(SerialActivity.this, PrintTextActivity.class);
    				startActivity(intent);        			
        		}
        		else
        		{
        			Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}
        	}
        });
        
        //**************************************************************************************************
		// PirntBarCode
		printBarcodeBtn = (Button) findViewById(R.id.com_print_barcode);
		printBarcodeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
				    Intent intent = new Intent();
					intent.setClass(SerialActivity.this, BarCodeActivity.class);
					startActivity(intent);	
				}        		
				else
        		{
        			Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}
			}
		});
        
		//**************************************************************************************************
		// PrintPDF417
		printPDF417Btn = (Button) findViewById(R.id.com_print_pdf417);


		//*************************************************************************************************
		// PrintQR
		printQRBtn = (Button) findViewById(R.id.com_print_qr);
		printQRBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
				    Intent intent = new Intent();
					intent.setClass(SerialActivity.this, QRCodeActivity.class);
					startActivity(intent);			
				}
				else
        		{
        			Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}
			}
		});
		
		//***************************************************************************************************
		// PrintGS1
		printGS1Btn = (Button)findViewById(R.id.com_print_gs1);

		
		//**************************************************************************************************
		// Print RasterCharacter
		printRasterBitmapBtn = (Button)findViewById(R.id.com_print_rasterimg);
		printRasterBitmapBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
				    Intent intent = new Intent();
					intent.setClass(SerialActivity.this, RasterBitmapActivity.class);
					startActivity(intent);			
				}
				else
        		{
        			Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
        		}
			}
		});
		//*************************************************************************************************
		//Print User-defined character
		printUserDefCharBtn = (Button)findViewById(R.id.com_print_userfont);
		printUserDefCharBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
					error_code = testprint.TestUserDefinedCharacter(pos_com, printMode);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(SerialActivity.this, "Failed to Print UserDefinedCharacter.",Toast.LENGTH_LONG).show();	
					}
				}
				else
				{
					Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();	
				}
				
			}
		});
		
		//***************************************************************************************************
		//FeedPaper
		printFeedLineBtn = (Button)findViewById(R.id.com_print_feed);
		printFeedLineBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
					error_code = testprint.TestFeedLine(pos_com, printMode);
	        		if(error_code != POS_SUCCESS)
	        		{
	        			Toast.makeText(SerialActivity.this, "Failed to Feed Paper.",Toast.LENGTH_LONG).show();
	        		}				
				}
				else
				{
					Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();
				}

			}
		});
		
		//*************************************************************************************************
		//CutPaper
		printCutPaperBtn = (Button)findViewById(R.id.com_print_cut);
		printCutPaperBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(sdk_flag == true)
				{
					error_code = testprint.TestCutPaper(pos_com, printMode);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(SerialActivity.this, "Failed to Cut Paper.",Toast.LENGTH_LONG).show();	
					}
				}
				else
				{
					Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();	
				}
				
			}
			
		});
		//**************************************************************************************************
		// Get Printer State
		queryStateBtn = (Button) findViewById(R.id.com_query_state);
		queryStateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] posStateTemp = new byte[1];
				int result;
					
				if(sdk_flag == true){
					result = testprint.POSNETQueryStatus(pos_com,posStateTemp);
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
						Toast.makeText(SerialActivity.this, "Failed to read Data.",Toast.LENGTH_LONG).show();
					}	
				}else{
					Toast.makeText(SerialActivity.this, "The port is not open,please click 'Open' button, try to open port.",Toast.LENGTH_LONG).show();	
				}

			}
		});
    }
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}  
}
