package com.fuzion24.shootemup;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
	
	public static float mWidth;
	public static float mHeight;
	private final int NUM_OF_COWBOYS = 5;
	
	GameThread mThread;
	CrossHairSprite mCrossHairs;
	List<BulletHoleSprite> mBulletHoles;
	List<CowBoySprite> mCowBoys;

	
	public GamePanel(Context context) {
		super(context);
		mCrossHairs = new CrossHairSprite(getResources());
		mBulletHoles = new ArrayList<BulletHoleSprite>();
		mCowBoys = new ArrayList<CowBoySprite>();
		
		for(int i = 0; i < NUM_OF_COWBOYS; i++)
		{
			CowBoySprite newCowBoy = new CowBoySprite(getResources());
			mCowBoys.add(newCowBoy);
		}
		getHolder().addCallback(this);
		mThread = new GameThread(this);
	}
	
	public void doDraw(long elapsed, Canvas canvas) {
	    canvas.drawColor(Color.WHITE);
	   

	    synchronized(mCowBoys)
	    {
		    for(CowBoySprite cbs : mCowBoys)
		    {
		    	cbs.drawCowboy(canvas);
		    }
	    }
	    synchronized(mBulletHoles){
		    for(BulletHoleSprite bulletHole : mBulletHoles)
		    {
		    	bulletHole.drawBulletHole(canvas);
		    }
	    }
	    mCrossHairs.drawCrossHairs(canvas);
	    canvas.drawText("FPS: " + Math.round(1000f / elapsed) + " Elements: " + mCowBoys.size(), 10, 10, new Paint());

	}
	
	public void animateCowBoys(long elapsedTime) {
	    synchronized (mCowBoys) {
	        for (CowBoySprite cbs : mCowBoys) {
	        	cbs.animate(elapsedTime);
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
	    mWidth = width;
	    mHeight = height;
	}
}
