package com.vivo.touchscreen.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.http.util.EncodingUtils;

import android.util.Log;

public class NodeFileRWService {
	String nodePath = null;
	
	public NodeFileRWService(String nodePath) {
		super();
		this.nodePath = nodePath;
	}
	
	public String read(String nodeName) throws Exception {
		byte[] buffer;
		if(null == nodePath) {
			return "no file path input";
		}
		FileInputStream fins = new FileInputStream(nodePath + nodeName);
		int len = fins.available();
		Log.i("chenpeng_read", String.valueOf(len));
		if(len > 1024*20) {
			fins.close();
			return "file too big";
		}
		buffer = new byte[len];
		fins.read(buffer, 0, len);
		fins.close();
		String str = EncodingUtils.getString(buffer, "UTF-8");
		return  str;
	}
	
	public String write(String nodeName, String content) throws Exception {
		byte[] buffer = content.getBytes();
		
		if(null == nodePath) {
			return "no file path input";
		}
		FileOutputStream fouts = new FileOutputStream(nodePath + nodeName);
		
		fouts.write(buffer, 0, buffer.length);
		
		fouts.close();
		
		return "write succeed";
	}
}
