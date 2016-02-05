package com.vivo.touchscreen.views;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

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

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_MOVE :
				queueEvent(new Runnable(){	//queueEvent??????,?????????????
					public void run() {
						renderer.bgColorSet(event.getX()/getWidth(),event.getY()/getHeight(), (event.getY()+event.getX())/(getHeight()+getWidth()));
					}
				});
				break;
			default:
				break;
		}
		
		/* must return true else not detect ACTION_MOVE*/
		return true;//super.onTouchEvent(event);
	}

	public GLView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	

	/* 
	 * render
	 **/
	public class ViewRenderer implements Renderer {

		private float colorRed;
		private float colorGreen;
		private float colorBlue;
		public int bgColorSet(float colorRed, float colorGreen, float colorBlue) {
			this.colorRed = colorRed;
			this.colorGreen = colorGreen;
			this.colorBlue = colorBlue;
			return 0;				
		}
		
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			//?????
			gl.glDisable(GL10.GL_DITHER);
			//???????????
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			//????????
			gl.glShadeModel(GL10.GL_SMOOTH);
			//??????
			gl.glEnable(GL10.GL_DEPTH_TEST);
			//????????
			gl.glDepthFunc(GL10.GL_LEQUAL);
			
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			//??3D????????
			gl.glViewport(0, 0, width, height);
			//??????????????
			gl.glMatrixMode(GL10.GL_PROJECTION);
			//???????
			gl.glLoadIdentity();
			//?????????
			float ratio = (float)width/height;
			//??????????
			gl.glFrustumf(-ratio*100, ratio*100, -100, 100, 100, 1000);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			//???????????
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			//set clear color
			gl.glClearColor(colorRed, colorGreen, colorBlue, 1.0f); 
			//show we draw 
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		}
		
		
	}
	
	
}
