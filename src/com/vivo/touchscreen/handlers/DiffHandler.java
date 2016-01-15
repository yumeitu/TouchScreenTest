package com.vivo.touchscreen.handlers;

import com.vivo.touchscreen.logs.QuickLog;
import com.vivo.touchscreen.service.NodeFileRWService;
import com.vivo.touchscreen.views.DiffRawDataView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DiffHandler extends Handler{
	private String[] data;
	private DiffRawDataView dataView;
	private NodeFileRWService nrw;
	private int tx,rx;
	public DiffHandler(DiffRawDataView dataView, String[] data, NodeFileRWService nrw, int tx, int rx) {
		super();

		this.dataView = dataView;
		this.data = data;
		this.nrw = nrw;
		this.tx = tx;
		this.rx = rx;
	}

	@Override
	public void handleMessage(Message msg) {
		String str = null;
		if(msg.what == 0) {
			return ;
		}
		if(msg.what == 1) {
			str = new String("sensor_delta");
		}
		if(msg.what == 2) {
			str = new String("sensor_rawdata");
		}
		
		QuickLog.i("DiffHandler in");
			int node = 0;
			String diff_raw = null;
			try {
				QuickLog.i("DiffHandler 1");
				diff_raw = nrw.read(str);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//QuickLog.i("DiffHandler read diff/raw err");
				e.printStackTrace();
			}
			//Log.i("chenpeng", diff_raw);	//20开始,没5个字符为一个数据
			//QuickLog.i("DiffHandler 2");
			int index = 0;
			StringBuffer tmpString;
			StringBuffer test = new StringBuffer();
			for(index=19; index<rx*tx*5+19+(tx-1); index=index+5) {		
				tmpString = new StringBuffer();
				if(node%rx==0 && node!=0) {	//越过换行
					index++;
				}
				//QuickLog.i("DiffHandler 3");
				tmpString.append(diff_raw.substring(index, index+5));
				data[node] = tmpString.toString();
				test.append(data[node]+"@");
				node++;
			}
			
			//QuickLog.i("DiffHandler 4");
			dataView.setData(data);
			dataView.invalidate();
		
		super.handleMessage(msg);
	}
		
}
