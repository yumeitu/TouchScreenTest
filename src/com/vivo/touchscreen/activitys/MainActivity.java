package com.vivo.touchscreen.activitys;

import com.vivo.touchscreen.activitys.NodeOprActivity;
import com.vivo.touchscreen.logs.QuickLog;
import com.vivo.touchscreen.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private ActionBar acb;
	WakeLock wakeLock = null;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        //acb = this.getActionBar();
        
        this.keepWakeButton = (ToggleButton)this.findViewById(R.id.keep_wake_button);
        //keep lcd light aways
        PowerManager manager = ((PowerManager)this.getSystemService(POWER_SERVICE)); 
        wakeLock = manager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, QuickLog.TSTAG);
        setKeepWake(false);	//start default false
    }
    
    //while in push "aways light" button ,set to light aways,else set no keep
    private Boolean isKeepWake = false;
    ToggleButton keepWakeButton = null;
    public void setKeepWake(Boolean keepOrNone) {
    	if(keepOrNone==true && isKeepWake==false) {
    		wakeLock.acquire();	//keep light aways
    		isKeepWake = true;    		
    	}
    	if(keepOrNone==false && isKeepWake==true) {
    		wakeLock.release();	//none keep light
    		isKeepWake = false;    		
    	}
    	keepWakeButton.setChecked(isKeepWake);
    }
    
    public void onClick(View view) {
    	Log.i("chenpeng", "button");
    	switch(view.getId()) {
    		
    		case R.id.node_operation_button : 
    			//Toast.makeText(this, "main_node_operation_button", Toast.LENGTH_SHORT).show();
    			//acb.show();
    			
    			Intent intent = new Intent();
    			intent.setClass(this, NodeOprActivity.class);	
    			this.startActivity(intent);
    			break;
    		case R.id.firmware_update_button :
    			Toast.makeText(this, "main_firmware_update_button", Toast.LENGTH_SHORT).show();
    			
    			//first set lcd keep wake
    			setKeepWake(true);
    			
    			Intent firmwareIntent = new Intent();
    			firmwareIntent.setClass(this, FirmwareUpdateActivity.class);	
    			this.startActivity(firmwareIntent);
    			//acb.hide();
    			break;
    		case R.id.effect_adjust_button :
    			Toast.makeText(this, "main_effect_adjust_button", Toast.LENGTH_SHORT).show();
    			break;
    		case R.id.diff_rawdata_show_button :
    			Log.i("chenpeng", "diff button");
    			//Toast.makeText(this, "main_diff_rawdata_show_button", Toast.LENGTH_SHORT).show();
    			Intent diffRawIntent = new Intent();
    			diffRawIntent.setClass(this, DiffRawShowActivity.class);
    			Log.i("chenpeng", "start activity");
    			this.startActivity(diffRawIntent);
    			break;
    		case R.id.product_config_button :
    			QuickLog.i("product config button");
    			Intent productConfigIntent = new Intent();
    			productConfigIntent.setClass(this, ProductConfigActivity.class);
    			this.startActivity(productConfigIntent);
    			break;
    		case R.id.keep_wake_button :
    			if(isKeepWake == true) {
    				setKeepWake(false);
    			} else {
    				setKeepWake(true);
    			}
    			break;
    		case R.id.exit_app_button :
    			this.finish();	
    			break;    			
    		default : break;
    	}
    }
}
