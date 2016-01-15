package com.vivo.touchscreen.activitys;

import java.nio.Buffer;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;

import com.vivo.touchscreen.R;
import com.vivo.touchscreen.handlers.DiffHandler;
import com.vivo.touchscreen.logs.QuickLog;
import com.vivo.touchscreen.service.NodeFileRWService;
import com.vivo.touchscreen.threads.DiffThread;
import com.vivo.touchscreen.views.DiffRawDataView;

import android.app.ActionBar.LayoutParams;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class DiffRawShowActivity extends Activity {
	private final int NONE_STOP = 0;	//定义宏
	private final int DIFF_2D = 1;
	private final int RAW_2D = 2;
	private final int DIFF_3D = 3;
	private final int RAW_3D = 4;

	private int which_show = NONE_STOP;	//0:stop	1:diff_2d	2:raw_2d
	private int flushTime = 500;
	
	private DiffRawDataView dataView;
	private NodeFileRWService nrw = new NodeFileRWService("/sys/touchscreen/");
	private int tx,rx;
	private DiffHandler handler;
	private String[] data = new String[700];
	private DiffThread diffThread = null;
	private Thread thread = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("chenpeng","entered DiffRawShowActivity");		
		this.setContentView(R.layout.activity_diff_rawdata_show);
		this.getActionBar().hide();	//关闭ActionBar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);	//去除状态栏  
		
		Log.i("chenpeng","45");
		String str = null;
		try {
			str = nrw.read("sensor_rx_tx");
		} catch (Exception e) {
			e.printStackTrace();
		}	//高位RX  低位tx	得到的str为十进制,直接右移8位即是tx
		//Log.i("chenpeng","46 str="+str);
		str = str.substring(0, 4).trim();
		int txrx = Integer.parseInt(str, 10);
		//Log.i("chenpeng","47 ");
		tx = txrx&0xff;
		rx = txrx>>8;
		Log.i("chenpeng","97");
		
		dataView = new DiffRawDataView(this, getWindowManager(), tx, rx);
		LinearLayout ll = (LinearLayout)findViewById(R.id.diff_raw_linearlayout);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		dataView.setLayoutParams(params);
		dataView.setBackgroundColor(Color.WHITE);
		ll.addView(dataView);
		Log.i("chenpeng","98");
		
		//添加一个handler
		handler = new DiffHandler(dataView, data, nrw, tx, rx);
		Log.i("chenpeng","99");
		//创建线程
		diffThread = new DiffThread(handler);
		Thread thread = new Thread(diffThread);
		thread.start();	//启动线程

		buttonColorSet(null);	//首先初始化,获取按键默认的颜色
		
		Log.i("chenpeng","100");
		
	}
	
	private ColorStateList csl = null;
	private void buttonColorSet(View view) {
		Button d2_diff_button = (Button) findViewById(R.id.d2_diff_button);
		
		if(view == null) {
			csl= d2_diff_button.getTextColors();
			return ;
		}
	
		d2_diff_button.setTextColor(csl);
		Button d2_raw_button = (Button) findViewById(R.id.d2_raw_button);
		d2_raw_button.setTextColor(csl);
		Button d3_diff_button = (Button) findViewById(R.id.d3_diff_button);
		d3_diff_button.setTextColor(csl);
		Button d3_raw_button = (Button) findViewById(R.id.d3_raw_button);
		d3_raw_button.setTextColor(csl);
		
		switch(view.getId()) {
			case R.id.d2_diff_button :
				((Button)view).setTextColor(Color.BLUE);
				break;
			case R.id.d2_raw_button :
				((Button)view).setTextColor(Color.BLUE);									
				break;
			case R.id.d3_diff_button :
				((Button)view).setTextColor(Color.BLUE);
				break;
			
			case R.id.d3_raw_button :
				((Button)view).setTextColor(Color.BLUE);
				break;				
		}
		switch(this.which_show) {
			case 1:
				d2_diff_button.setTextColor(Color.BLUE);
				break;
			case 2 : 
				d2_raw_button.setTextColor(Color.BLUE);
				break;
			default : break;
				
		}
	}
	private void buttonEnable(boolean ed) {
		Button d2_diff = (Button) findViewById(R.id.d2_diff_button);
		Button d2_raw = (Button) findViewById(R.id.d2_raw_button);		
		Button d3_diff = (Button) findViewById(R.id.d3_diff_button);		
		Button d3_raw = (Button) findViewById(R.id.d3_raw_button);		
		Button menu = (Button) findViewById(R.id.menu_button);
		Button flush_time_button = (Button) findViewById(R.id.flush_time_button);
		Button size_button = (Button) findViewById(R.id.size_button);
		
		d2_diff.setEnabled(ed);
		d2_raw.setEnabled(ed);	//设置安江是否能够按压
		d3_diff.setEnabled(ed);
		d3_raw.setEnabled(ed);		
		menu.setEnabled(ed);	
		flush_time_button.setEnabled(ed);
		size_button.setEnabled(ed);
	}
	private int dataViewSize = 4;
	public void onClick(View view) throws Exception {
		//buttonColorSet(view);
		switch(view.getId()) {
			case R.id.size_button :
				dataViewSize++;
				if(dataViewSize > 11) {
					dataViewSize = 1;
				}
				dataView.setTableSize(dataViewSize);
				((Button)view).setText("SZ:" + String.valueOf(dataViewSize));
				break;
			case R.id.flush_time_button :
				Button ftb = (Button)view;
				if(flushTime == 500) {
					ftb.setText("1s");
					flushTime = 1000;
					break;
				}
				if(flushTime == 1000) {
					ftb.setText("2s");
					flushTime = 2000;
					break;
				}
				if(flushTime == 2000) {
					ftb.setText("250ms");
					flushTime = 250;
					break;
				}
				if(flushTime == 250) {
					ftb.setText("500ms");
					flushTime = 500;
					break;
				}
				break;
			case R.id.d2_diff_button :
				this.which_show = this.DIFF_2D;
				//this.flushTime = 500; 
				break;
			case R.id.d2_raw_button :
				this.which_show = this.RAW_2D;
				//this.flushTime = 1000; 									
				break;
			
			case R.id.d3_diff_button :
				this.which_show = this.NONE_STOP;
				//this.flushTime = 500; 	
				break;
			
			case R.id.d3_raw_button :
				this.which_show = this.NONE_STOP;
				//this.flushTime = 500; 	
				break;
			
			case R.id.lock_button :
				ToggleButton lock_button = (ToggleButton) view;
				boolean isChecked = lock_button.isChecked();
				if(isChecked) {
					if(this.which_show == this.NONE_STOP) {
						lock_button.setChecked(false);
						lock_button.setTextColor(Color.RED);
						QuickLog.shortToast(this, "No one chiose!");
						buttonEnable(true);
						break;
					}
					lock_button.setTextColor(Color.GREEN);
					buttonEnable(false);
					diffThread.setMsgWhat(this.which_show, this.flushTime);
					dataView.setIsLock(true);	//锁定画面,将不能移动移动
				}else {
					lock_button.setTextColor(Color.RED);
					buttonEnable(true);
					diffThread.setMsgWhat(this.NONE_STOP, 0);
					dataView.setIsLock(false);	//释放画面,将可以移动移动
					dataView.setCommond(DiffRawDataView.CMD_CLEAN_BIGGEST_DIFF_DATA);
				}
				
				break;
			
			case R.id.menu_button : 
				ActionBar ab = this.getActionBar();
				if(ab.isShowing()) {
					this.getActionBar().hide();
				} else {
					this.getActionBar().show();
				}				
				break;
				
			case R.id.return_button :
				diffThread.stop();	//先设置标志,然后唤醒,唤醒后循环不成立,因此退出run
				diffThread.setMsgWhat(this.DIFF_2D, 500);	//先暂停
				
				this.finish();
				break;
				
			default : break;		
		}
		buttonColorSet(view);
	}



/*

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(thread!=null) {
			thread.notify();
		}
	}





	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		thread.notify();
	}





	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		thread.notify();
	}





	@Override
	protected void onPause() {
		try {
			thread.wait();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		super.onPause();
	}





	@Override
	protected void onStop() {
		try {
			diffThread.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStop();
	}





	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	*/
	

}
