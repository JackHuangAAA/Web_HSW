package com.ethink.lineup.print_nh80m;

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

public class RasterBitmapActivity extends Activity {
	
	//Parameter variable
	private String img_data = null;
	private int paintSize = 0;
	private int bold = 0;
	
	//Operate control
	private Button btnOK, btnRet;
	private EditText txtdata,txtPaintSize;
	private Spinner raster_bold_spi;
	
	//TestPrint variable
	private TestPrintInfo testprint;
	private int error_code = 0;
	
	//Returned Value Statement
	public static final int POS_SUCCESS=1000;		//�ɹ�
	public static final int ERR_PROCESSING = 1001;	//ʧ��
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rasterbitmap);
    	
        //**************************************************************************************************
        //FontBold Spinner
        raster_bold_spi = (Spinner)findViewById(R.id.raster_bold_spi);
        String[] bold_items = {"1","2","3","4","5"};
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bold_items);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	raster_bold_spi.setAdapter(adapter);
    	
    	//Get PrintData EditText Object
    	txtdata = (EditText)findViewById(R.id.raster_data);
    	
    	//Get PaintSize EditText Object
    	txtPaintSize = (EditText)findViewById(R.id.raster_paintSize);
    	
		//****************************************************************************************************
		//Get parameter and print
		btnOK = (Button)findViewById(R.id.raster_print_btn);
		btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String spi_str;
				testprint = new TestPrintInfo();
				
				//Get PrintData
				img_data = txtdata.getText().toString();
				
				//Get PaintSize
				spi_str = txtPaintSize.getText().toString();
				if(spi_str.length() == 0){
					Toast.makeText(RasterBitmapActivity.this, "Edit box value cannot be empty.",Toast.LENGTH_LONG).show();	
					return;
				}
				paintSize = Integer.parseInt(spi_str);
				
				//Get FrontBold
				spi_str = raster_bold_spi.getSelectedItem().toString();
				bold = Integer.parseInt(spi_str);
				

					error_code = testprint.TestRasterBitmap(SerialActivity.pos_com, SerialActivity.printMode, img_data, paintSize, bold);
					if(error_code != POS_SUCCESS)
					{
						Toast.makeText(RasterBitmapActivity.this, "Failed to print RasterCharacter.",Toast.LENGTH_LONG).show();
					}


			}});
		//*************************************************************************************************
		//Back
		btnRet = (Button)findViewById(R.id.raster_ret_btn);
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
