package com.vivo.touchscreen.views;

import com.vivo.touchscreen.logs.QuickLog;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
//import android.provider.ContactsContract.PinnedPositions;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class DiffRawDataView extends SurfaceView {
	public final static int CMD_CLEAN_BIGGEST_DIFF_DATA = 1;
	private int screenWidthDp;
	private int screenHeightDp;
	private int tableWidth;
	private int tableHeight;
	private int unitWidth;
	private int unitHeight;
	private int startPosX = 50;
	private int startPosY = 50;
	private int tx=10;
	private int rx=10;
	private int tableSize = 7;
	private Paint paint = new Paint();
	private Paint textPaint = new Paint();
	
	private void getOrUpdataTableParameter() {
        tableWidth = screenWidthDp * (14+tableSize) / 10 / this.tx * this.tx;	//21
        tableHeight = screenHeightDp  * (14+tableSize) / 10 / this.rx * this.rx;	//21
        unitWidth = tableWidth / this.tx;
        unitHeight = tableHeight / this.rx;
	}
	
	private String[] data = null;
	public DiffRawDataView(Context context, WindowManager windowManager, int tx, int rx) {
		super(context);
        this.tx = tx;
        this.rx = rx;
		//Log.i("chenpeng","00");   
        Display display = windowManager.getDefaultDisplay();    
        Point outSize = new Point();
       // Log.i("chenpeng","01");
        display.getSize(outSize);
        this.screenWidthDp = (int) (outSize.x/context.getResources().getDisplayMetrics().density + 0.5f); 	//将像素转换为dp
        this.screenHeightDp = (int) (outSize.y/context.getResources().getDisplayMetrics().density + 0.5f);
        getOrUpdataTableParameter();
       // Log.i("chenpeng", "pixl-x="+String.valueOf(outSize.x)+" y="+String.valueOf(outSize.y));
       // Log.i("chenpeng", "dp-x="+String.valueOf(screenWidthDp)+" y="+String.valueOf(screenHeightDp));


        
		paint.setAntiAlias(true);	//去锯齿
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.FILL);	//设置画笔为实心
		paint.setStrokeWidth(1);
		paint.setTextSize(18);
		
		textPaint.setAntiAlias(true);	//去锯齿
		textPaint.setColor(Color.GREEN);
		textPaint.setStyle(Paint.Style.STROKE);	//设置画笔为空心
		textPaint.setStrokeWidth(1);
		textPaint.setTextSize(18);
		
	}
	
	private boolean isLock = false;
	public void setIsLock(boolean isLock) {
		this.isLock = isLock;
	}
	public void setTableSize(int size) {
		this.tableSize = size;
		getOrUpdataTableParameter();	//根据tablesize的修改,更新其他参数
		textPaint.setTextSize(14+size);	//根据不同的大小设定字体大小
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//canvas.drawColor(Color.WHITE);	//更新背景色
		//paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		//canvas.drawPaint(paint);
		//paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
		
		if(isLock == false) {	//非锁定模式,可以移动图片否则不能移动
			paint.setColor(Color.GRAY);
			canvas.drawRect(startPosX, startPosY, startPosX+tableWidth, startPosY+tableHeight,paint);
		} else {		
			diff_raw_show(canvas, startPosX, startPosY, rx, tx);
		}
		
	}
	
	public void setData(String data[]) {
		this.data = data;
	}
	
	int highDiffVal = 0;
	int lowDiffVal = 0;
	public void setCommond(int cmd) {
		switch(cmd) {
			case CMD_CLEAN_BIGGEST_DIFF_DATA:
				highDiffVal = 0;
				lowDiffVal = 0;
				break;
			default :
				break;
		}
	}
	private void diff_raw_show(Canvas canvas, int startPosX, int startPosY, int rx, int tx) {
		int i=0, j=0;
		String str = null;
		int maxPos = 0;
		int maxX = 0;
		int maxY = 0;
		int minNeg = 0;
		int minX = 0;
		int minY = 0;
		int val = 0;
		
		paint.setStyle(Paint.Style.FILL);	//设置画笔为实心
		
		if(data!=null) {
//			QuickLog.i("data is not null");
			for(i=0; i<tx; i++) {	//14
				//QuickLog.i("draw tx:" + String.valueOf(i));	//
				for(j=0; j<rx; j++) {	//25
					//QuickLog.i("draw rx:" + String.valueOf(j));	//
					//Log.i("chenpeng", String.valueOf(i)+" "+String.valueOf(j));
					if(data[rx*i+j].contains("-")) {
					//	QuickLog.i("neg 1");
						str = data[rx*i+j].replaceAll("\\s*", "");	//除了去掉空格,应该其中还混有换行符,不取出容易产生异常
						val = Integer.parseInt(str, 10);
						//QuickLog.i("neg 2");
						//获取最小值
						if(val < minNeg) {
							minNeg = val;	//保存最小值
							minX = i;
							minY = j;
							if(minNeg < lowDiffVal) {
								lowDiffVal = minNeg;
							}								
						}
						//QuickLog.i("neg 3");
						val = 255 + 5 * val;
						paint.setColor(Color.rgb(val, val, val));
						if(val < 120) {
							textPaint.setColor(Color.GREEN);
						} else {
							textPaint.setColor(Color.BLACK);
						}					
					} else {						
						str = data[rx*i+j].replaceAll("\\s*", "");	//除了去掉空格,应该其中还混有换行符,不取出容易产生异常
						val = Integer.parseInt(str, 10);						
						
						//获取最大值
						if(val > maxPos) {
							maxPos = val;	//保存最大值
							maxX = i;
							maxY = j;
							if(maxPos > highDiffVal) {
								highDiffVal = maxPos;
							}	
						}
						
						val = (14 - val / 12) * 12;	//计算颜色阶梯值
						if(val<0) {
							paint.setColor(Color.RED);
						} else {
							paint.setColor(Color.rgb(val, val, 255));
						}
						textPaint.setColor(Color.BLACK);
					}					
					canvas.drawRect(startPosX+unitWidth*i+1, startPosY+unitHeight*j-1, startPosX+unitWidth*(i+1)-2, startPosY+unitHeight*(j+1)-3, paint);													
					canvas.drawText(data[rx*i+j], startPosX+unitWidth*i, startPosY+unitHeight*(j+1)-unitHeight/2+10, textPaint);					
				}				
			}
	//		QuickLog.i("diff_raw_show for end");
			canvas.drawText("max:"+String.valueOf(maxPos)+" min:"+String.valueOf(minNeg), startPosX, startPosY+unitHeight*(j+1)-unitHeight/2+10, textPaint);
			canvas.drawText("highDiffVal:"+String.valueOf(highDiffVal)+" lowDiffVal:"+String.valueOf(lowDiffVal), startPosX, startPosY+unitHeight*(j+2)-unitHeight/2+10, textPaint);
			
			paint.setColor(Color.GREEN);
			paint.setStyle(Paint.Style.STROKE);	//设置画笔为空心
			paint.setStrokeWidth(6);
			canvas.drawRect(startPosX+unitWidth*maxX+1, startPosY+unitHeight*maxY-1, startPosX+unitWidth*(maxX+1)-2, startPosY+unitHeight*(maxY+1)-3, paint);
			paint.setColor(Color.RED);
			canvas.drawRect(startPosX+unitWidth*minX+1, startPosY+unitHeight*minY-1, startPosX+unitWidth*(minX+1)-2, startPosY+unitHeight*(minY+1)-3, paint);
			
			paint.setStrokeWidth(1);
		//	QuickLog.i("diff_raw_show end");
		}		
	}
	
	private int oldPosX = 50;
	private int oldPosY = 50;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(this.isLock == false) {
			int newPosX = (int) event.getX();
			int newPosY = (int) event.getY();
				
			switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN :
					oldPosX = newPosX;
					oldPosY = newPosY;
					QuickLog.i("DOWN x=" + String.valueOf(newPosX)+ " y=" + String.valueOf(newPosY));
					break;
					
				case MotionEvent.ACTION_UP :
			//	case MotionEvent.ACTION_CANCEL :
					
					
					QuickLog.i("UP x=" + String.valueOf(newPosX)+ " y=" + String.valueOf(newPosY));
					break;
				
				default : 
					startPosX = startPosX + (newPosX - oldPosX);
					startPosY = startPosY + (newPosY - oldPosY);
					if(startPosX < -tableWidth/2){	//保证图像区域在可视范围内
						startPosX = -tableWidth/2;
					}
					if(startPosY < -tableHeight/2){
						startPosY = -tableHeight/2;
					}
					if(startPosX > tableWidth){
						startPosX = tableWidth;
					}
					if(startPosY > tableHeight){
						startPosY = tableHeight;
					}
					oldPosX = newPosX;
					oldPosY = newPosY;
					this.invalidate();
					
					break;
			}
			//QuickLog.i("startPosX=" + String.valueOf(startPosX)+ " startPosY=" + String.valueOf(startPosY));	
		}	
		return true;	//返回true表示处理方法已经处理该事件
	}
	
}
