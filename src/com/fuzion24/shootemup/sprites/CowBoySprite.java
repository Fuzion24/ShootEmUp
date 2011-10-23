package com.fuzion24.shootemup.sprites;

import java.util.Random;

import com.fuzion24.shootemup.GamePanel;
import com.fuzion24.shootemup.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class CowBoySprite {
	private float mX = -1;
	private float mY = -1;
	private float mSpeedX;
	private float mSpeedY;
	private Bitmap mOrigCowBoy;
	private Bitmap mCurrentCowBoy;
	
	int mBloodPuddleScale = 1;
	private Bitmap mBloodPuddle;
	private Bitmap mCurrentBloodPuddle;
	private boolean mKilled = false;
	public boolean removeCowBoy = false;
	
	enum SPEED{
		SLOW,
		MEDIUM,
		FAST
	}
	
	public CowBoySprite(Resources res) {
		Random rand = new Random();
		mCurrentCowBoy = mOrigCowBoy = BitmapFactory.decodeResource(res, R.raw.cowboy);
		mCurrentBloodPuddle = mBloodPuddle = BitmapFactory.decodeResource(res, R.raw.bloodpuddle);
		mCurrentCowBoy = scaleBitmap(mOrigCowBoy, (float).6,(float).6,0);
		mX = 0;
		mY = 0;
	    mSpeedX = rand.nextInt(7) - 2 - rand.nextFloat();
	    mSpeedY = rand.nextInt(7) - 2 - rand.nextFloat();
	}

	public void drawCowboy(Canvas canvas) {
		//TODO: The cowboy should never get bigger than 100%
		/*Scale the cowboy based on the depth*/
	/*	
	   float scaleFactor = GamePanel.mHeight / (GamePanel.mHeight - mY);
		if(scaleFactor > 0)
			mCurrentBitmap = scaleCowboy(scaleFactor,scaleFactor);
	*/
			
		if(mKilled)
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
	    mX += mSpeedX * (elapsedTime / 20f);
	    mY += mSpeedY * (elapsedTime / 20f);
	    checkBorders();
	}
	
	public boolean wasShot(int x, int y)
	{
		int lowWidth = (int) mX;
		int highWidth = (int) (mX + mCurrentCowBoy.getWidth());
		int lowHeight = (int) mY;
		int highHeight = (int) mY + mCurrentCowBoy.getHeight();
		if (x >= lowWidth && x <= highWidth && y >= lowHeight && y <= highHeight)
		{
			//Lay the cowboy down
			mCurrentCowBoy = scaleBitmap(mOrigCowBoy, (float).6,(float).6,90);
			mSpeedX = mSpeedY = 0; //He's no longer moving
			mCurrentBloodPuddle = scaleBitmap(mBloodPuddle,(float).01,(float).01,0);
			mKilled = true;
			return true;
		}
		return false;
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
	}
}
