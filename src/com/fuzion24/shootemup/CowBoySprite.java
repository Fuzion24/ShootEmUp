package com.fuzion24.shootemup;

import java.util.Random;

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
	private Bitmap mBitmap;
	private Bitmap mCurrentBitmap;

	public CowBoySprite(Resources res) {
		Random rand = new Random();
		mCurrentBitmap = mBitmap = BitmapFactory.decodeResource(res, R.raw.cowboy);
		mX = 0;
		mY = 0;
	    mSpeedX = rand.nextInt(7) - 2 - rand.nextFloat();
	    mSpeedY = rand.nextInt(7) - 2 - rand.nextFloat();
	}

	public void drawCowboy(Canvas canvas) {
		/*Scale the cowboy based on the depth*/
		float scaleHeight = GamePanel.mHeight / (GamePanel.mHeight - mY);
		float scaleWidth = scaleHeight;
		//scaleWidth = scaleHeight = new Random().nextFloat();
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		
		
		if(scaleHeight > 0)
			mCurrentBitmap = Bitmap.createBitmap(mBitmap, 0, 0,width,height,matrix,true);
	
		canvas.drawBitmap(mCurrentBitmap, mX, mY, null);
	}
	
	public void animate(long elapsedTime) {
	    mX += mSpeedX * (elapsedTime / 20f);
	    mY += mSpeedY * (elapsedTime / 20f);
	    checkBorders();
	}
	private void checkBorders() {
	    if (mX <= 0) {
	        mSpeedX = -mSpeedX;
	        mX = 0;
	    } else if (mX + mCurrentBitmap.getWidth() >= GamePanel.mWidth) {
	        mSpeedX = -mSpeedX;
	        mX = GamePanel.mWidth - mCurrentBitmap.getWidth();
	    }
	    if (mY <= 0) {
	        mY = 0;
	        mSpeedY = -mSpeedY;
	    }
	    if (mY + mCurrentBitmap.getHeight() >= GamePanel.mHeight) {
	        mSpeedY = -mSpeedY;
	        mY = GamePanel.mHeight - mCurrentBitmap.getHeight();
	    }
	}
}
