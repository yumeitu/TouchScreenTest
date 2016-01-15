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
		//��ȡcontext
		this.context = context;
	}
	
	public String getLocalIPAddr() {
		//������manifest���������Ȩ��
		//��ȡwifi����
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		//�ж�wifi�Ƿ���,δ��������
        if (!wifiManager.isWifiEnabled()) {  
        	QuickLog.shortToast(context, "wifi is not open!");
        	//wifiManager.setWifiEnabled(true);  
        	return null;
        }
        
        //��ȡIP
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();
        localIPAddr = this.intToIp(ipAddress); 
        QuickLog.i(localIPAddr);
		
		return localIPAddr;
	}
	
	//ipת��ΪString
	private String intToIp(int i) {       
		return  (i & 0xFF ) + "." +       
				((i >> 8 ) & 0xFF) + "." +       
				((i >> 16 ) & 0xFF) + "." +       
			( i >> 24 & 0xFF) ;  
	}
	
	private static final int SERVER_STATE_SLEEP = 0;
	private static final int SERVER_STATE_RUN = 1;
	private static ArrayList<Socket> socketList = new ArrayList<Socket>();
	private int serverState = this.SERVER_STATE_SLEEP;	//Ĭ�Ϸ�����Ϊ��ͣ״̬
	public int setAsServer(int clientNumber) {
		this.toolState = this.RUN_AS_SERVER;
		
		new Thread();
		
		return 0;
	}
		
	public int stopServer() {
		//1.������״̬Ϊsleep
		//2.Ȼ��ģ��һ������,����,serverManger�ͻ��⵽,break
		return 0;
	}
	
	//������������--�����ڶ��socket
	private class ServerManagerThread implements Runnable
	{
		public ServerManagerThread() {
			
		}
		
		public void run() {
			
			while(true) {	//�ȴ�����,һ��������Ϊ�����ߴ���һ���߳̽��з���
				
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int setAsClient() {
		
		return 0;
	}
}
