package com.vivo.touchscreen.logs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class QuickLog {
	public static final String TSTAG = "tstest";
	public static void i(String str) {
		Log.i("tstest", str);
	}
	public static void shortToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	public static void longToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
}
