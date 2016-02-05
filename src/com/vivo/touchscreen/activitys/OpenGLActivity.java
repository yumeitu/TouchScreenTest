package com.vivo.touchscreen.activitys;

import com.vivo.touchscreen.views.GLView;

import android.app.Activity;
import android.os.Bundle;

public class OpenGLActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GLView glView = new GLView(this);
		this.setContentView(glView);
	}

}
