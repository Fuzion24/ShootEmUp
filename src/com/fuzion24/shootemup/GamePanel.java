package com.fuzion24.shootemup;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
	
	GameThread mThread;
	CrossHairSprite mCrossHairs;
	List<BulletHoleSprite> mBulletHoles;
	
	
	public GamePanel(Context context) {
		super(context);
		mCrossHairs = new CrossHairSprite(getResources());
		mBulletHoles = new ArrayList<BulletHoleSprite>();
		getHolder().addCallback(this);
		mThread = new GameThread(this);
	}
	
	public void doDraw(Canvas canvas) {
	    canvas.drawColor(Color.WHITE);
	    mCrossHairs.drawCrossHairs(canvas);
	    synchronized(mBulletHoles){
		    for(BulletHoleSprite bulletHole : mBulletHoles)
		    {
		    	bulletHole.drawBulletHole(canvas);
		    }
	    }
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.d("MultiTouchDebug", String.valueOf(event.getPointerId(1)));
		
		switch(event.getAction()){
		case MotionEvent.ACTION_POINTER_2_DOWN:
			BulletHoleSprite newBulletHole = new BulletHoleSprite(getResources());
			newBulletHole.setBulletHole((int)event.getX(), (int)event.getY());
			synchronized(mBulletHoles){
			  mBulletHoles.add(newBulletHole);
			}
		default: 
			mCrossHairs.moveCrossHairs((int)event.getX(), (int) event.getY());
		}
		
	   return true;
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
	    if (!mThread.isAlive()) {
	        mThread = new GameThread(this);
	        mThread.setRunning(true);
	        mThread.start();
	    }
	}
	 
	public void surfaceDestroyed(SurfaceHolder holder) {
	    if (mThread.isAlive()) {
	        mThread.setRunning(false);
	    }
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
}
