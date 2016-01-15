package com.vivo.touchscreen.threads;

import com.vivo.touchscreen.logs.QuickLog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DiffThread implements Runnable {
	private Handler handler;
	private int msgWhat = 0;	//0: stop 1:diff_2d 2:raw_2d
	private int time = 500;		//刷新时间
	Object lock = new Object();	//作为一个锁来shi
	private boolean runStop = true;
	
	public DiffThread(Handler handler) {
		super();

		this.handler = handler;
	}
	
	/**
	 * 设置要发送的消息类型和刷新时间
	 * @param msgWhat 消息类型
	 * @param time 刷新时间
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
			Message message = new Message();	//必须每次构造一个新的message
			message.what = this.msgWhat; 
            handler.sendMessage(message);// 发送消息 
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
	 * 设置run中线程死循环标志
	 */
	public void stop() {
		this.runStop = false;	//设置死循环标志,让进程结束
	}

}
