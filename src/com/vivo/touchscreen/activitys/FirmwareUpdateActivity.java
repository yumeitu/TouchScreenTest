package com.vivo.touchscreen.activitys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.http.util.EncodingUtils;

import com.vivo.touchscreen.R;
import com.vivo.touchscreen.logs.QuickLog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirmwareUpdateActivity extends Activity {
	private Button chooseButton = null;
	private String imgName = null;
	private EditText fw_state = null;
	private Button updateButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 this.setContentView(R.layout.activity_firmware_update);
		 
		 this.chooseButton = (Button) findViewById(R.id.fw_choose_button);
		 //chooseButton.setText("choose firmware");
		 this.fw_state = (EditText) findViewById(R.id.fw_state);
		 
		 updateButton = (Button) findViewById(R.id.update_button);
		 updateButton.setClickable(false);	//disable update first
	}

	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.fw_choose_button :
				Intent fwChooseIntent = new Intent();
				fwChooseIntent.setClass(this, FirmwareChooseActivity.class);
				this.startActivityForResult(fwChooseIntent, 0);
				break;
			case R.id.update_button :
			try {
				QuickLog.i("success1");
				this.fw_state.setText("State:updating " + this.imgName);
				//Runtime.getRuntime().exec("");
				Process process = Runtime.getRuntime().exec("cp /sdcard/48_01.img /sdcard/48.img");	//
				//Runtime.getRuntime().exec("echo " + this.imgName + " > /sys/touchscreen/firmware_update");
				byte[] buf = new byte[1024];
				process.getInputStream().read(buf);
				QuickLog.i(EncodingUtils.getString(buf, "UTF-8"));
				
				this.fw_state.setText("State:finish update " + this.imgName);
			} catch (IOException e) {
				QuickLog.i("fail");
				e.printStackTrace();
			}
				break;
			
			case R.id.fw_auto_update :
				
				break;
			default : 
				break;
		}		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0 && resultCode==0) {
			//QuickLog.i("get data:" + data.getExtras().getString("imgName"));
			this.imgName = new String(data.getExtras().getString("imgName"));
			
			chooseButton.setText("Choose Firmware" + "  " + this.imgName);
			this.fw_state.setText("State:choose " + this.imgName);
			
			//enable update button
			updateButton.setClickable(true);
		}
	}

	@Override
	protected void onDestroy() {		
		super.onDestroy();
		
	}

}
