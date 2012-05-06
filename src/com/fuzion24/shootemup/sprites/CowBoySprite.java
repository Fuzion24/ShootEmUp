package com.fuzion24.shootemup.sprites;

import java.util.Random;

import com.fuzion24.shootemup.GamePanel;
import com.fuzion24.shootemup.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

public class CowBoySprite implements Comparable<CowBoySprite>{
	private float mX = -1;
	public  float mY = -1;
	private float mSpeedX;
	private float mSpeedY;
	private Bitmap mOrigCowBoy;
	private Bitmap mCurrentCowBoy;
	
	int mBloodPuddleScale = 1;
	private Bitmap mBloodPuddle;
	private Bitmap mCurrentBloodPuddle;
	public boolean removeCowBoy = false;
	
	private int mSpeed = CowBoySpeed.SLOW;
	
	private CowBoyState mCurrentState = CowBoyState.stopped;
	private long mStateTime = 0;
	private long mLongestStateTime = 0;
	
	private static class CowBoySpeed {
		public static final int SLOW = 2;
		public static final int MEDIUM = 3;
		public static final int FAST = 5;
	}
	
	enum CowBoyState{
		walking,
		stopped,
		shooting,
		dead
	}
	
	public CowBoySprite(Resources res) {
		Random rand = new Random();
		mCurrentCowBoy = mOrigCowBoy = BitmapFactory.decodeResource(res, R.raw.cowboy);
		mCurrentBloodPuddle = mBloodPuddle = BitmapFactory.decodeResource(res, R.raw.bloodpuddle);
		mCurrentCowBoy = scaleBitmap(mOrigCowBoy, (float).6,(float).6,0);
		
		//Randomly select their starting position
		mX = rand.nextInt(800);
		mY = rand.nextInt(800);
		
		//Randomly select their speed
		switch(rand.nextInt(3)){
		case 1: 
			mSpeed = CowBoySpeed.SLOW;
		case 2: 
		    mSpeed = CowBoySpeed.MEDIUM;
		case 3: 
		    mSpeed = CowBoySpeed.FAST;
		default:
			mSpeed = CowBoySpeed.MEDIUM;
		}
		
	    changeDirection();
	    int max = 10000;
	    int min = 2000;
	    mLongestStateTime = rand.nextInt(max - min + 1) + min;
	}

	private void changeDirection()
	{
		Random rand = new Random();
		boolean negativeX = rand.nextInt(1) == 0 ? true : false;
		boolean negativeY = rand.nextInt(1) == 0 ? true : false;
		//Make them prefer to not move diagonally
		boolean diagonally = rand.nextInt(10) < 4 ? true : false;
		
		if(diagonally)
		{
			mSpeedX = rand.nextInt(mSpeed);
			mSpeedY = rand.nextInt(mSpeed);
		}else
		{
			//Randomly choose to zero out x-speed or y-speed
			boolean zeroxspeed = rand.nextInt(1) == 0 ? true : false;
			if(zeroxspeed)
			{
				mSpeedX = 0;
				mSpeedY = rand.nextInt(mSpeed);
			}else
			{
				mSpeedY = 0;
				mSpeedX = rand.nextInt(mSpeed);
			}
		}
		
		if(negativeX)
			mSpeedX = -mSpeedX;
		if(negativeY)
			mSpeedY = -mSpeedY;
		
	}
	public void drawCowboy(Canvas canvas) {
		/*Scale the cowboy based on the depth*/
	   float scaleFactor = mY / GamePanel.mHeight;
	   //Set scalefactor to a number between .4f and .7f based on the percentage away from the user the indian is.
	   scaleFactor = scaleFactor * (.7f-.4f) + .4f;

	   //TODO: scale blood pile based on depth, too .. 
	   //Cowboys further away bleed more?! :P
		if(mCurrentState == CowBoyState.dead)
		{
			//Show Blood Puddle
			if(mBloodPuddleScale < 100)
			{
				mCurrentBloodPuddle = scaleBitmap(mBloodPuddle, (float) mBloodPuddleScale/100, (float) mBloodPuddleScale/100,0);
				mBloodPuddleScale++;
			}else
			{
				removeCowBoy = true;
			}
			canvas.drawBitmap(mCurrentBloodPuddle, mX,mY, null);
			 mCurrentCowBoy = scaleBitmap(mOrigCowBoy,scaleFactor,scaleFactor,90);
		}else
		{
			 mCurrentCowBoy = scaleBitmap(mOrigCowBoy,scaleFactor,scaleFactor,0);
		}
		canvas.drawBitmap(mCurrentCowBoy, mX, mY, null);
	}
	
	private Bitmap scaleBitmap(Bitmap bitmap, float scaleHeight, float scaleWidth,int rotateDegrees)
	{
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		matrix.postRotate(rotateDegrees);
		return Bitmap.createBitmap(bitmap, 0, 0,width,height,matrix,true);
	}
	public void animate(long elapsedTime) {
		mStateTime += elapsedTime;
		
		if (mCurrentState == CowBoyState.walking)
		{
			if(mStateTime > mLongestStateTime)
			{
				mCurrentState = CowBoyState.stopped;
				mStateTime = 0;
				return;
			}
	
			mX += mSpeedX * (elapsedTime / 20f);
			mY += mSpeedY * (elapsedTime / 20f);
			checkBorders();
		}else if (mCurrentState == CowBoyState.stopped)
		{
			//TODO: Change direction of the Cowboy
			if(mStateTime > mLongestStateTime*.75)
			{
				changeDirection();
				mCurrentState = CowBoyState.walking;
				mStateTime = 0;
			}
			
		}
	}
	
	public boolean wasShot(int x, int y)
	{
		int lowWidth = (int) mX;
		int highWidth = (int) (mX + mCurrentCowBoy.getWidth());
		int lowHeight = (int) mY;
		int highHeight = (int) mY + mCurrentCowBoy.getHeight();
		if (x >= lowWidth && x <= highWidth && y >= lowHeight && y <= highHeight)
		{
			if(pixelPerfectCollision(mCurrentCowBoy, x,y))
			{
			//Lay the cowboy down
			mCurrentCowBoy = scaleBitmap(mOrigCowBoy, (float).6,(float).6,90);
			mSpeedX = mSpeedY = 0; //He's no longer moving
			mCurrentBloodPuddle = scaleBitmap(mBloodPuddle,(float).01,(float).01,0);
			mCurrentState = CowBoyState.dead;
			return true;
			}
		}
		return false;
	}
	
	private boolean pixelPerfectCollision(Bitmap bitmap, int x, int y)
	{
		x = (int) (x - mX);
		y = (int) (y - mY);
		if (x >= bitmap.getWidth() || y >= bitmap.getHeight())
			return false;
		else
			return bitmap.getPixel(x, y) != Color.TRANSPARENT;
	}

	private void checkBorders() {
	    if (mX <= 0) {
	        mSpeedX = -mSpeedX;
	        mX = 0;
	    } else if (mX + mCurrentCowBoy.getWidth() >= GamePanel.mWidth) {
	        mSpeedX = -mSpeedX;
	        mX = GamePanel.mWidth - mCurrentCowBoy.getWidth();
	    }
	    if (mY <= 0) {
	        mY = 0;
	        mSpeedY = -mSpeedY;
	    }
	    if (mY + mCurrentCowBoy.getHeight() >= GamePanel.mHeight) {
	        mSpeedY = -mSpeedY;
	        mY = GamePanel.mHeight - mCurrentCowBoy.getHeight();
	    }
	    if(mY + mCurrentCowBoy.getHeight() < 100){
	    	mSpeedY = -mSpeedY;
	    	mY = 100 - mCurrentCowBoy.getHeight();
	    }
	}

	public int compareTo(CowBoySprite cbs) {
		return (int) (this.mY - cbs.mY);
	}
}
