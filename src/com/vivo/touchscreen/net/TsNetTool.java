package com.vivo.touchscreen.net;

import java.net.Socket;
import java.util.ArrayList;

import com.vivo.touchscreen.logs.QuickLog;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class TsNetTool {
	public final static int RUN_AS_SERVER = 0;
	public final static int RUN_AS_CLIENT = 1;
	
	private static int toolState = -1;
	
	private Context context = null;
	private String localIPAddr = null;
	
	
	public TsNetTool(Context context) {
		//获取context
		this.context = context;
	}
	
	public String getLocalIPAddr() {
		//必须在manifest中添加网络权限
		//获取wifi服务
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		//判断wifi是否开启,未开启则开启
        if (!wifiManager.isWifiEnabled()) {  
        	QuickLog.shortToast(context, "wifi is not open!");
        	//wifiManager.setWifiEnabled(true);  
        	return null;
        }
        
        //获取IP
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();
        localIPAddr = this.intToIp(ipAddress); 
        QuickLog.i(localIPAddr);
		
		return localIPAddr;
	}
	
	//ip转换为String
	private String intToIp(int i) {       
		return  (i & 0xFF ) + "." +       
				((i >> 8 ) & 0xFF) + "." +       
				((i >> 16 ) & 0xFF) + "." +       
			( i >> 24 & 0xFF) ;  
	}
	
	private static final int SERVER_STATE_SLEEP = 0;
	private static final int SERVER_STATE_RUN = 1;
	private static ArrayList<Socket> socketList = new ArrayList<Socket>();
	private int serverState = this.SERVER_STATE_SLEEP;	//默认服务器为暂停状态
	public int setAsServer(int clientNumber) {
		this.toolState = this.RUN_AS_SERVER;
		
		new Thread();
		
		return 0;
	}
		
	public int stopServer() {
		//1.先设置状态为sleep
		//2.然后模拟一个连接,这样,serverManger就会检测到,break
		return 0;
	}
	
	//服务器管理器--管理众多的socket
	private class ServerManagerThread implements Runnable
	{
		public ServerManagerThread() {
			
		}
		
		public void run() {
			
			while(true) {	//等待访问,一旦访问则为访问者创建一个线程进行服务
				
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int setAsClient() {
		
		return 0;
	}
}
