package com.fuzion24.shootemup;

import java.util.ArrayList;
import java.util.List;

import com.fuzion24.shootemup.sprites.BulletHoleSprite;
import com.fuzion24.shootemup.sprites.CowBoySprite;
import com.fuzion24.shootemup.sprites.CrossHairSprite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
	
	public static int mWidth;
	public static int mHeight;
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
		
		synchronized(mCowBoys)
	    {
			for(int i = 0; i < NUM_OF_COWBOYS; i++)
			{
				CowBoySprite newCowBoy = new CowBoySprite(getResources());
				mCowBoys.add(newCowBoy);
			}
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
		    canvas.drawText("FPS: " + Math.round(1000f / elapsed) + " Elements: " + mCowBoys.size(), 10, 10, new Paint());
	    }
	    synchronized(mBulletHoles){
		    for(BulletHoleSprite bulletHole : mBulletHoles)
		    {
		    	bulletHole.drawBulletHole(canvas);
		    }
	    }
	    mCrossHairs.drawCrossHairs(canvas);
	    

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
		switch(event.getAction()){
		case MotionEvent.ACTION_POINTER_2_DOWN:
			BulletHoleSprite newBulletHole = new BulletHoleSprite(getResources());
			int xPos = (int)event.getX();
			int yPos = (int)event.getY();
			newBulletHole.setBulletHole(xPos,yPos);
			synchronized(mBulletHoles){
			  mBulletHoles.add(newBulletHole);
			}
			synchronized(mCowBoys){
				
			  for(CowBoySprite cowBoy: mCowBoys)
			  {
				  boolean cowBoyWasKilled = cowBoy.wasShot(xPos, yPos);
				  if(cowBoyWasKilled)
				  {					 
					  mBulletHoles.remove(newBulletHole);
					  break;
				  }
			  }
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

	public void removeDeadCowboys() {
		synchronized(mCowBoys){
			  for(int i = 0; i < mCowBoys.size(); i++)
			  {
				  CowBoySprite cowBoy = mCowBoys.get(i);
				  if(cowBoy.removeCowBoy)
				  {					 
					  mCowBoys.remove(cowBoy);
				  }
			  }
			}
	}
}
