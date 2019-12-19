package com.ethink.lineup.print_nh80m;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ethink.lineup.R;

import java.io.UnsupportedEncodingException;

public class PrintTextActivity extends Activity {
	
	//Parameter variable
	private String text_data = null;
	private int DataLength = 0;
	private int FontStyle = 0;
	private int FontType = 0;
	private int Alignment = 0;
	private int HorStartingPosition = 0;
	private int VerStartingPosition = 0;
	private int LineHeight = 0;
	private int HorizontalTimes = 0;
	private int VerticalTimes = 0;
	
	//Operate control
	private Button btnOK, btnRet;
	private EditText txtdata,txtHorStartingPosition,txtVerStartingPosition,txtLineHeight;
	private Spinner text_fonttype_spi,text_alignmenttype_spi,text_hortimes_spi,text_vertimes_spi;
	private ToggleButton TogStyleReverse,TogStyleBold,TogStyleUnderline;
	
	//TestPrint variable
	private TestPrintInfo testprint;
	private int error_code = 0;
	
	//Returned Value Statement 
	public static final int POS_SUCCESS=1000;		//success
	public static final int ERR_PROCESSING = 1001;	//fail
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textprint);
        
        //**************************************************************************************************
        //FontType Spinner
        text_fonttype_spi = (Spinner)findViewById(R.id.text_fonttype_spi);
        String[] fonttype_items = {"Standard ASCII","Compressed ASCII","User-defined character","Chinese font"};
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fonttype_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	text_fonttype_spi.setAdapter(adapter);
    	
        //**************************************************************************************************
        //Alignment Spinner
    	text_alignmenttype_spi = (Spinner)findViewById(R.id.text_alignmenttype_spi);
        String[] alignmenttype_items = {"0","1","2"};
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alignmenttype_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	text_alignmenttype_spi.setAdapter(adapter);
    	
    	
        //**************************************************************************************************
        //HorizontalTimes Spinner
    	text_hortimes_spi = (Spinner)findViewById(R.id.text_hortimes_spi);
        String[] hortimes_items = {"1","2","3","4","5","6"};
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hortimes_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	text_hortimes_spi.setAdapter(adapter);
    	
        //**************************************************************************************************
        //VerticalTimes Spinner
    	text_vertimes_spi = (Spinner)findViewById(R.id.text_vertimes_spi);
        String[] vertimes_items = {"1","2","3","4","5","6"};
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vertimes_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	text_vertimes_spi.setAdapter(adapter);
    	
    	//Get StyleReverse ToggleButton Object
    	TogStyleReverse = (ToggleButton)findViewById(R.id.text_StyleReverse);
    	
    	//Get StyleBold ToggleButton Object
    	TogStyleBold = (ToggleButton)findViewById(R.id.text_StyleBold);
    	
    	//Get StyleUnderline ToggleButton Object
    	TogStyleUnderline = (ToggleButton)findViewById(R.id.text_StyleUnderline);
    	
    	//Get PrintData EditText Object
    	txtdata = (EditText)findViewById(R.id.text_data);
    	
    	//Get HorStartingPosition EditText Object
    	txtHorStartingPosition = (EditText)findViewById(R.id.text_HorStartingPosition);
    	
    	//Get VerStartingPosition EditText Object
    	txtVerStartingPosition = (EditText)findViewById(R.id.text_VerStartingPosition);
    	
    	//Get LineHeight EditText Object 
    	txtLineHeight = (EditText)findViewById(R.id.text_LineHeight);
    	
		//****************************************************************************************************
		//Get parameter and print
		btnOK = (Button)findViewById(R.id.text_print_but);
		btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String spi_str;
				testprint = new TestPrintInfo();
				FontStyle = 0;
				
				//Get PrintData
				text_data = txtdata.getText().toString();
				
				//Get FontStyle
				if(TogStyleReverse.isChecked() == true)//choose StyleReverse
				{
					FontStyle |= 0x400;
				}
				if(TogStyleBold.isChecked() == true)//choose StyleBold
				{
					FontStyle |= 0x08;
				}
				if(TogStyleUnderline.isChecked() == true)//choose StyleUnderline
				{
					FontStyle |= 0x80;
				}
				
				//Get FontType
				FontType = text_fonttype_spi.getSelectedItemPosition();
				
				//Get Alignmenttype
				Alignment = text_alignmenttype_spi.getSelectedItemPosition();
				
				//Get HorStartingPosition
				spi_str = txtHorStartingPosition.getText().toString();
				if(spi_str.length() == 0){
					Toast.makeText(PrintTextActivity.this, "Edit box value cannot be empty.",Toast.LENGTH_LONG).show();	
					return;
				}
				HorStartingPosition = Integer.parseInt(spi_str);
				
				//Get VerStartingPosition
				spi_str = txtVerStartingPosition.getText().toString();
				if(spi_str.length() == 0){
					Toast.makeText(PrintTextActivity.this, "Edit box value cannot be empty.",Toast.LENGTH_LONG).show();	
					return;
				}
				VerStartingPosition = Integer.parseInt(spi_str);
				
				//Get LineHeight 
				spi_str = txtLineHeight.getText().toString();
				if(spi_str.length() == 0){
					Toast.makeText(PrintTextActivity.this, "Edit box value cannot be empty.",Toast.LENGTH_LONG).show();	
					return;
				}
				LineHeight = Integer.parseInt(spi_str);
				
				//Get HorizontalTimes
				spi_str = text_hortimes_spi.getSelectedItem().toString();
				HorizontalTimes = Integer.parseInt(spi_str);
				
				//Get VerticalTimes
				spi_str = text_vertimes_spi.getSelectedItem().toString();
				VerticalTimes = Integer.parseInt(spi_str);
				
				//Get data length
				try {
					DataLength = text_data.getBytes("GB18030").length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

					error_code = testprint.TestPrintText(USBActivity.pos_usb, USBActivity.printMode, text_data, DataLength, FontType, FontStyle, 
							Alignment,HorStartingPosition, VerStartingPosition, LineHeight, HorizontalTimes, VerticalTimes);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(PrintTextActivity.this, "Failed to print Text.",Toast.LENGTH_LONG).show();
					}

			   
			}});
		//*************************************************************************************************
		//Back
		btnRet = (Button)findViewById(R.id.text_ret_but);
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
