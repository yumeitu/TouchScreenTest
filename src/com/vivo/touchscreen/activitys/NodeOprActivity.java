package com.vivo.touchscreen.activitys;

import com.vivo.touchscreen.R;
import com.vivo.touchscreen.service.NodeFileRWService;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class NodeOprActivity extends Activity {
	NodeFileRWService nrw;
	Button logSwitchOnButton;
	Button logSwitchOffButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_node_opr);
		
		nrw = new NodeFileRWService("/sys/touchscreen/");
		logSwitchOffButton = (Button) findViewById(R.id.log_switch_off_button);
		logSwitchOnButton = (Button) findViewById(R.id.log_switch_on_button);
		try {
			stateInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void closeActivity(View v) {
		this.finish();	//该方法直接关闭该activity
	} 
	
	private void stateInit() throws Exception {
		String str = nrw.read("log_switch");
		if(str.contains(" = 0")) {
			logSwitchOffButton.setTextColor(Color.parseColor("#00FF00"));			
		} else {
			logSwitchOnButton.setTextColor(Color.parseColor("#00FF00"));
		}
		
		//Log.i("chenpeng", "init gesture switch button state!");
		str = nrw.read("gesture_switch");	//初始化手势开关
		int index = str.indexOf("=");
		str = str.substring(index+4, index+6);
		int gesture_state_value = Integer.parseInt(str, 16);	//将手势的16进制字符转换为int
		Log.i("chenpeng", String.valueOf(gesture_state_value));
		ToggleButton gestureButton = (ToggleButton) this.findViewById(R.id.gesture_lr);
		boolean checked;
		//Log.i("chenpeng", "4");
		if(((gesture_state_value & 0x1)== 0x1)) {
		//	Log.i("chenpeng", "1");
			checked = true;
		} else {
			//Log.i("chenpeng", "2");
			checked = false;
		}
		//Log.i("chenpeng", "3");
		gestureButton.setChecked(checked);		
		//------du
		gestureButton =  (ToggleButton) this.findViewById(R.id.gesture_du);
		if(((gesture_state_value & 0x02)==0x02)) {
			checked = true;
		} else {
			checked = false;
		}		
		gestureButton.setChecked(checked);	
		//------
		gestureButton =  (ToggleButton) this.findViewById(R.id.gesture_o);
		if(((gesture_state_value & 0x04)==0x04)) {
			checked = true;
		} else {
			checked = false;
		}		
		gestureButton.setChecked(checked);
		//------
		gestureButton =  (ToggleButton) this.findViewById(R.id.gesture_w);
		if(((gesture_state_value & 0x08)==0x08)) {
			checked = true;
		} else {
			checked = false;
		}		
		gestureButton.setChecked(checked);
		//------
		gestureButton =  (ToggleButton) this.findViewById(R.id.gesture_m);
		if(((gesture_state_value & 0x10)==0x10)) {
			checked = true;
		} else {
			checked = false;
		}		
		gestureButton.setChecked(checked);
		//------
		gestureButton =  (ToggleButton) this.findViewById(R.id.gesture_e);
		if(((gesture_state_value & 0x20)==0x20)) {
			checked = true;
		} else {
			checked = false;
		}		
		gestureButton.setChecked(checked);
		//------
		gestureButton =  (ToggleButton) this.findViewById(R.id.gesture_c);
		if(((gesture_state_value & 0x40)==0x40)) {
			checked = true;
		} else {
			checked = false;
		}		
		gestureButton.setChecked(checked);	
	}

    public void onClick(View view) throws Exception {
    	//Log.i("chenpeng", "button");
    	//Log.i("chenpeng", String.valueOf(view.getId()) + String.valueOf(R.id.fw_module_id_button ));
    	String str;
    	TextView tv;
    	switch(view.getId()) {
    		
    		case R.id.fw_module_id_button : 	//模组ID
    			//Toast.makeText(this, "modele id", Toast.LENGTH_SHORT).show();
    			tv = (TextView)this.findViewById(R.id.fw_module_id_textview);
    			str = nrw.read("firmware_module_id");
    			tv.setText(str);
    			break;
    		case R.id.log_switch_on_button :
    			nrw.write("log_switch", "1");
    			str = nrw.read("log_switch");
    			if(str.contains(" = 1")) {
    				logSwitchOnButton.setTextColor(Color.parseColor("#00FF00"));		
    				logSwitchOffButton.setTextColor(Color.parseColor("#000000"));
    			} else {
    				logSwitchOnButton.setTextColor(Color.parseColor("#000000"));
    				logSwitchOffButton.setTextColor(Color.parseColor("#00FF00"));	
    			}
    			break;
    		case R.id.log_switch_off_button :
    			nrw.write("log_switch", "0");
    			str = nrw.read("log_switch");
    			if(str.contains(" = 0")) {
    				logSwitchOffButton.setTextColor(Color.parseColor("#00FF00"));	
    				logSwitchOnButton.setTextColor(Color.parseColor("#000000"));
    			} else {
    				logSwitchOffButton.setTextColor(Color.parseColor("#000000"));
    				logSwitchOnButton.setTextColor(Color.parseColor("#00FF00"));	
    			}
    			break;
    			
    		case R.id.fw_version_button :
    			tv = (TextView)this.findViewById(R.id.fw_version_textview);
    			str = nrw.read("firmware_version");
    			tv.setText(str);
    			break;
    		case R.id.sensor_rx_tx_button : 
    			//Log.i("chenpeng","00");
    			tv = (TextView) this.findViewById(R.id.sersor_rx_tx_textview);
    			str = nrw.read("sensor_rx_tx");	//高位RX  低位tx	得到的str为十进制,直接右移8位即是tx
    			//Log.i("chenpeng", "11");
    			//Log.i("chenpeng", str);
    			str = str.substring(0, 4).trim();
    			//Log.i("chenpeng","33 str=" + str);
    			int txrx = Integer.parseInt(str, 10);
    			//Log.i("chenpeng","11");
    			str = "Rx:" + String.valueOf(txrx>>8) + " Tx:" + String.valueOf(txrx&0xff);
    			//Log.i("chenpeng","22"+str);
    		
    			tv.setText(str);
    			break;
    		case R.id.gesture_switch_button :
    			tv = (TextView) this.findViewById(R.id.gesture_switch_textview);
    			str = nrw.read("gesture_switch");    			
    			int index = str.indexOf("=");
    			str = str.substring(index+4, index+6);
    			//int gesture_state_value = Integer.parseInt(str, 16);	//将手势的16进制字符转换为int
    			tv.setText(str);
    			break;
    		case R.id.gesture_point_button : 
    			tv = (TextView) this.findViewById(R.id.gesture_point_textview);
    			str = nrw.read("gesture_point");
    			tv.setText(str);
    			break;
    			
    		case R.id.gesture_du : 
    		case R.id.gesture_o :
    		case R.id.gesture_dc :
    		case R.id.gesture_a :
    		case R.id.gesture_c : 
    		case R.id.gesture_e : 
    		case R.id.gesture_f : 
    		case R.id.gesture_lr :
    		case R.id.gesture_m :
    		case R.id.gesture_rl :
    		case R.id.gesture_ud :
    		case R.id.gesture_w :
    			ToggleButton tgb = (ToggleButton) this.findViewById(view.getId());
    			str = nrw.read("gesture_switch");	//初始化手势开关
    			index = str.indexOf("=");
    			str = str.substring(index+4, index+6);
    			int gesture_state_value = Integer.parseInt(str, 16);	//将手势的16进制字符转换为int
    			int bitMask = 0;
    			switch(view.getId()) {	//0x01:LR 0x02:up 0x04:O 0x08:W 0x10:M 0x20:e 0x40:C
    				case R.id.gesture_du : bitMask = 0x02; break;
    				case R.id.gesture_lr : bitMask = 0x01; break;	
    				case R.id.gesture_o : bitMask = 0x04; break;
    				case R.id.gesture_w : bitMask = 0x08; break;
    				case R.id.gesture_m : bitMask = 0x10; break;
    				case R.id.gesture_e : bitMask = 0x20; break;
    				case R.id.gesture_c : bitMask = 0x40; break;
    				default : break;
    			}
    			if(tgb.isChecked()) {
    				//Log.i("chenpeng", "1");
    				gesture_state_value = gesture_state_value | bitMask;
    			}else{
    				gesture_state_value = gesture_state_value & (~bitMask);    				
    			}
    			nrw.write("gesture_switch", String.valueOf(gesture_state_value));
    			break;
    		default : break;
    	}
    }
}
