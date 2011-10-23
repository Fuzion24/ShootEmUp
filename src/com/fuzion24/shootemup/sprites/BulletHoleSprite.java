package com.fuzion24.shootemup.sprites;

import com.fuzion24.shootemup.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class BulletHoleSprite {
	private int mX = -1;
	private int mY = -1;
	private Bitmap mBitmap;

	public BulletHoleSprite(Resources res) {
		mBitmap = BitmapFactory.decodeResource(res, R.raw.mark);
	}

	public void drawBulletHole(Canvas canvas) {
		if (mX == -1 || mY == -1)
			return;
		canvas.drawBitmap(mBitmap, mX, mY, null);
	}

	public void setBulletHole(int x, int y) {
		mX = x - mBitmap.getWidth() / 2;
		mY = y - mBitmap.getHeight() / 2;
	}
}
