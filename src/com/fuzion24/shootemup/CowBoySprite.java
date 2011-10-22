package com.fuzion24.shootemup;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class CowBoySprite {
	private float mX = -1;
	private float mY = -1;
	private int mSpeedX;
	private int mSpeedY;
	private Bitmap mBitmap;

	public CowBoySprite(Resources res) {
		Random rand = new Random();
		mBitmap = BitmapFactory.decodeResource(res, R.raw.cowboy);
		mX = 0;
		mY = 0;
	    mSpeedX = rand.nextInt(7) - 3;
	    mSpeedY = rand.nextInt(7) - 3;
	}

	public void drawCowboy(Canvas canvas) {
		canvas.drawBitmap(mBitmap, mX, mY, null);
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
	    } else if (mX + mBitmap.getWidth() >= GamePanel.mWidth) {
	        mSpeedX = -mSpeedX;
	        mX = GamePanel.mWidth - mBitmap.getWidth();
	    }
	    if (mY <= 0) {
	        mY = 0;
	        mSpeedY = -mSpeedY;
	    }
	    if (mY + mBitmap.getHeight() >= GamePanel.mHeight) {
	        mSpeedY = -mSpeedY;
	        mY = GamePanel.mHeight - mBitmap.getHeight();
	    }
	}
}
