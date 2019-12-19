package com.ethink.lineup.print_nh80m;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ethink.lineup.R;

import java.io.UnsupportedEncodingException;

public class BarCodeActivity extends Activity {
	
	//Parameter variable
	private String bar_data=null;
	private int DataLength = 0;
	private int bar_type=0;
	private int bar_mode_width=0;
	private int bar_height=0;
	private int bar_hri_type=0;
	private int bar_hri_posit=0;
	
	//Operate control
	private Button btnOK,btnRet;
	private EditText txtdata, txtBarHeight;
	private Spinner bar_type_spi,mode_width_spi,hri_type_spi,hri_posit_spi;
	private int error_code = 0;
	
	//TestPrint variable
	private TestPrintInfo testprint;
	
	//Returned Value Statement 
	public static final int POS_SUCCESS=1000;		//success
	public static final int ERR_PROCESSING = 1001;	//fail
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode);
        
        //**************************************************************************************************
        //BarCodeType Spinner
        bar_type_spi = (Spinner)findViewById(R.id.bar_type_spi);
        String[] bar_items = {"UPC-A","UPC-E","JAN13","JAN8","CODE39","ITF","CODABAR","CODE93","CODE128"};
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bar_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	bar_type_spi.setOnItemSelectedListener(new SpinnerSelectedListener());
    	bar_type_spi.setAdapter(adapter);
		
		//**************************************************************************************************
		//ModuleWidth Spinner
		mode_width_spi = (Spinner)findViewById(R.id.bar_mode_width_spi);
        String[] mode_width_items = {"2","3","4","5","6"};
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mode_width_items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mode_width_spi.setAdapter(adapter);
		mode_width_spi.setSelection(1);
		
		//**************************************************************************************************
		//HRIFontType Spinner
		hri_type_spi = (Spinner)findViewById(R.id.bar_HRI_type_spi);
        String[] hri_type_items = {"FontTypeStandardASCII","FontTypeCompressedASCII"};
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hri_type_items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		hri_type_spi.setAdapter(adapter);
		
		//***************************************************************************************************
		//HRIFontPosition
		hri_posit_spi = (Spinner)findViewById(R.id.bar_HRI_posit_spi);
        String[] hri_posit_items = {"0","1","2","3"};
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hri_posit_items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		hri_posit_spi.setAdapter(adapter); 
		hri_posit_spi.setSelection(1);
		
		//Get PrintData EditText Object
		txtdata = (EditText)findViewById(R.id.barcode_data);
		
		//Get BarcodeHeight EditText Object
		txtBarHeight = (EditText)findViewById(R.id.bar_img_height);
		
		//**************************************************************************************************
		//Get parameter and print
		btnOK = (Button)findViewById(R.id.barcode_print_but);
		btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int item = 0;
				String spi_str;
		
				testprint = new TestPrintInfo();
				
				//Get PrintData
				bar_data = txtdata.getText().toString();
				
				//Get BarCodeType
				item = bar_type_spi.getSelectedItemPosition();
				bar_type = item+65;
				
				//Get ModuleWidth
				spi_str = mode_width_spi.getSelectedItem().toString();
				bar_mode_width = Integer.parseInt(spi_str);
				
				//Get BarcodeHeight
				spi_str = txtBarHeight.getText().toString();
				if(spi_str.length() == 0){
					Toast.makeText(BarCodeActivity.this, "Edit box value cannot be empty.",Toast.LENGTH_LONG).show();	
					return;
				}
				bar_height = Integer.parseInt(spi_str);
				
				//Get HRIFontType
				item = hri_type_spi.getSelectedItemPosition();
				bar_hri_type = item;
				
				//Get HRIFontPosition
				spi_str = hri_posit_spi.getSelectedItem().toString();
				bar_hri_posit = Integer.parseInt(spi_str);
				
				//Get data length
				try {
					DataLength = bar_data.getBytes("GB18030").length;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				


					error_code = testprint.TestPrintBar(SerialActivity.pos_com, SerialActivity.printMode, bar_data, DataLength, 
							bar_type, bar_mode_width, bar_height, bar_hri_type, bar_hri_posit);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(BarCodeActivity.this, "Failed to print Barcode.",Toast.LENGTH_LONG).show();
					}	



			}});
		
		//*************************************************************************************************
		//Back
		btnRet = (Button)findViewById(R.id.barcode_ret_but);
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
	
	//**********************************************************************************************************
	//Set Print data
    class SpinnerSelectedListener implements OnItemSelectedListener{   
  	  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,   
                long arg3) {   
    		char []text_buf = {'1','2','3','4','5','6','7','8','9','0','1','2','3'};
    		int item = bar_type_spi.getSelectedItemPosition();
    		//Set Print data
    		switch(item){
    	    case 0:
    	    	txtdata.setText(text_buf, 0, 11);
    	    	break;
    	    case 1:
    	    	txtdata.setText(text_buf, 0, 11);
    	    	break;
    	    case 2:
    	    	txtdata.setText(text_buf, 0, 12);
    	    	break;
    	    case 3:
    	    	txtdata.setText(text_buf, 0, 7);
    	    	break;
    	    default:
    	    	txtdata.setText(text_buf, 0, 12);
    	    	break;
    	} 
        }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}    
    } 
    
 

}
