package com.vivo.touchscreen.views;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/*
 * delicate game 
 * */

public class GLView extends GLSurfaceView {
	private ViewRenderer renderer;
	public GLView(Context context) {
		super(context);
		renderer = new ViewRenderer();
		this.setRenderer(renderer);
	}

	public GLView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	/* 
	 * render
	 **/
	public class ViewRenderer implements Renderer {

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
			
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			/* 设置显示区域 为整个屏幕 */
			gl.glViewport(0, 0, width, height);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			//set clear color
			gl.glClearColor(100, 100, 0, 1.0f); 
			//show we draw 
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		}
		
		
	}
}
