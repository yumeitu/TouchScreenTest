package com.vivo.touchscreen.threads;

import com.vivo.touchscreen.logs.QuickLog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DiffThread implements Runnable {
	private Handler handler;
	private int msgWhat = 0;	//0: stop 1:diff_2d 2:raw_2d
	private int time = 500;		//ˢ��ʱ��
	Object lock = new Object();	//��Ϊһ������shi
	private boolean runStop = true;
	
	public DiffThread(Handler handler) {
		super();

		this.handler = handler;
	}
	
	/**
	 * ����Ҫ���͵���Ϣ���ͺ�ˢ��ʱ��
	 * @param msgWhat ��Ϣ����
	 * @param time ˢ��ʱ��
	 */
	public void setMsgWhat(int msgWhat, int time) {	
		this.msgWhat = msgWhat;
		if(time<250) {
			time = 250;
		} else {
			this.time = time;
		}
		
		synchronized (lock) {
			lock.notify();
		}
	}
	
	@Override
	public void run() {
		while (runStop) { 
			Message message = new Message();	//����ÿ�ι���һ���µ�message
			message.what = this.msgWhat; 
            handler.sendMessage(message);// ������Ϣ 
			synchronized (lock) {
				try {
					if(message.what == 0) {
						lock.wait();
						Log.i("chenpeng", "wait");
					}else {
						lock.wait(this.time);
						Log.i("chenpeng", "wait time");
					}
				} catch (Exception e) {
					Log.i("chenpeng", "thread error");
					// TODO: handle exception
				}
				
			}

		} 
		QuickLog.i("DiffThread in DiffRawShowActivity stop!");
	}
	
	/**
	 * ����run���߳���ѭ����־
	 */
	public void stop() {
		this.runStop = false;	//������ѭ����־,�ý��̽���
	}

}
