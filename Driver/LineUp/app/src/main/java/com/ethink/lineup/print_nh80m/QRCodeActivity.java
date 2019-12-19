package com.ethink.lineup.print_nh80m;

import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ethink.lineup.R;

public class QRCodeActivity extends Activity {
	
	//Parameter variable
	private String qr_data = null;
	private int DataLength = 0;
	private int nOrgx = 0;
	private int iWeigth = 0;
	private int iSymbolType = 0;
	private int iLanguageMode = 0;
	
	//Operate control
	private Button btnOK, btnRet;
	private EditText txtdata, txtPosit;
	private Spinner qr_mode_width_spi,qr_boltype_spi,qr_language_spi;
	
	//TestPrint variable
	private TestPrintInfo testprint;
	private int error_code = 0;
	
	//Returned Value Statement 
	public static final int POS_SUCCESS=1000;		//success
	public static final int ERR_PROCESSING = 1001;	//fail
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);
        
        //**************************************************************************************************
        //BasicElementWidth Spinner
        qr_mode_width_spi = (Spinner)findViewById(R.id.qr_mode_width_spi);
        String[] mode_width_items = {"1","2","3","4","5","6","7","8","9","10"};
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mode_width_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	qr_mode_width_spi.setAdapter(adapter);
    	qr_mode_width_spi.setSelection(4);
    	
        //**************************************************************************************************
        //SymbolType Spinner
    	qr_boltype_spi = (Spinner)findViewById(R.id.qr_boltype_spi);
        String[] boltype_items = {"OriginalType","EnhancedType"};
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, boltype_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	qr_boltype_spi.setAdapter(adapter);
    	qr_boltype_spi.setSelection(1);
    	
        //**************************************************************************************************
        //LanguageMode Spinner
    	qr_language_spi = (Spinner)findViewById(R.id.qr_language_spi);
        String[] language_items = {"LanguageChinese","LanguageJapanese"};
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, language_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	qr_language_spi.setAdapter(adapter);
    	
    	//Get PrintData EditText Object
    	txtdata = (EditText)findViewById(R.id.qr_data);
    	
    	//Get HorStartingPosition EditText Object
    	txtPosit = (EditText)findViewById(R.id.qr_posit);
    	
		//****************************************************************************************************
		//Get parameter and print
		btnOK = (Button)findViewById(R.id.qr_print_but);
		btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String spi_str;
				int item;
				testprint = new TestPrintInfo();
				
				//Get PrintData
				qr_data = txtdata.getText().toString();
				
				//Get HorStartingPosition
				spi_str = txtPosit.getText().toString();
				if(spi_str.length() == 0){
					Toast.makeText(QRCodeActivity.this, "Edit box value cannot be empty.",Toast.LENGTH_LONG).show();	
					return;
				}
				nOrgx = Integer.parseInt(spi_str);
				
				//Get BasicElementWidth
				spi_str = qr_mode_width_spi.getSelectedItem().toString();
				iWeigth = Integer.parseInt(spi_str);
				
				//Get SymbolType
				item = qr_boltype_spi.getSelectedItemPosition();
				iSymbolType = item+1;
				
				//Get LanguageMode
				item = qr_language_spi.getSelectedItemPosition();
				iLanguageMode = item;
				
				//Get data length
				try {
					DataLength = qr_data.getBytes("GB18030").length;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}


					error_code = testprint.TestPrintQR(SerialActivity.pos_com, SerialActivity.printMode, qr_data, DataLength, 
							nOrgx, iWeigth, iSymbolType, iLanguageMode);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(QRCodeActivity.this, "Failed to print QR.",Toast.LENGTH_LONG).show();
					}	



			}});
    	
		//*************************************************************************************************
		//Back
		btnRet = (Button)findViewById(R.id.qr_ret_but);
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
