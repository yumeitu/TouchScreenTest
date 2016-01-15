package com.vivo.touchscreen.activitys;

import java.io.File;
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
import android.content.Intent;
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

public class FirmwareChooseActivity extends Activity {
	private Intent resultIntent = null;
	private Bundle dataPack = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_firmware_choose);		
		LinearLayout fwTable = (LinearLayout)this.findViewById(R.id.firmware_choose_scroll);		//��ȡ����
		
		//��ȡ���ݹ�����Intent
		this.resultIntent = this.getIntent();
		
		//�������ݰ�
		dataPack = new Bundle();
		
		//��ȡfirewareĿ¼�µĹ̼�����
		
		String str = "";
		File[] files = new File("/sdcard/").listFiles();	//����sdcard,�������Ȩ��
		for(File file : files) {
			String strTmp = file.getName();
			//QuickLog.i("file:" + str);
			if(strTmp != null && strTmp.contains("img")) {
				Button button = new Button(this);
				button.setOnClickListener(new ButtonClickListener());
				button.setText(strTmp);
				fwTable.addView(button);
			}
		}
		//QuickLog.i("file:" + str);
	}
	
	private class ButtonClickListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			Button button = (Button)view;
			FirmwareChooseActivity.this.dataPack.putString("imgName", button.getText().toString());
			FirmwareChooseActivity.this.resultIntent.putExtras(dataPack);
			FirmwareChooseActivity.this.setResult(0, resultIntent);	//�����ⲿ�۳�Ա
			FirmwareChooseActivity.this.finish();
			QuickLog.i("choose done");
		}
		
	}
	/*
	public void onClick(View view) {
		Button button = (Button)view;
		dataPack.putString("imgName", button.getText().toString());
		resultIntent.putExtras(dataPack);
		this.setResult(0, resultIntent);
		this.finishActivity(0);
		
	} */
	
}
