package com.fuzion24.shootemup.sprites;

import com.fuzion24.shootemup.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class CrossHairSprite {
	private int mX = -1;
	private int mY = -1;
	public boolean locked = false;
	private Bitmap mBitmap;

	public CrossHairSprite(Resources res) {
		mBitmap = BitmapFactory.decodeResource(res, R.raw.aim);
	}

	public void drawCrossHairs(Canvas canvas) {
		mX = mX == -1 ? (canvas.getWidth() / 2) - (mBitmap.getWidth() / 2) : mX;
		mY = mY == -1 ? (canvas.getHeight() / 2) - (mBitmap.getHeight() / 2) : mY;
		canvas.drawBitmap(mBitmap, mX, mY, null);
	}

	public void moveCrossHairs(int x, int y) {
		mX = x - mBitmap.getWidth() / 2;
		mY = y - mBitmap.getHeight() / 2;
	}
}
