package com.vivo.touchscreen.hardlibrary;

import com.vivo.touchscreen.logs.QuickLog;

public class HardControl {
	   public static native int ledCtrl();
	    public static native int ledOpen();
	    public static native int ledClose();
	 
	    static {	//¾²Ì¬¿é
	    	try {
	    		System.loadLibrary("led");	//¼ÓÔØso¿âÎÄ¼þ
			} catch (Exception e) {
				// TODO: handle exception
				QuickLog.i("load lib fail");
			}
	        
	    }
}
