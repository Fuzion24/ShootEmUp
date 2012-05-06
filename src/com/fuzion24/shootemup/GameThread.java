package com.fuzion24.shootemup;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
	   private GamePanel mGamePanel;
	    private SurfaceHolder mHolder;
	    private boolean mRun = false;
	    private long mStartTime;
	    private long mElapsed;
	    public GameThread(GamePanel panel) {
	    	mGamePanel = panel;
	        mHolder = mGamePanel.getHolder();
	    }
	 
	    public void setRunning(boolean run) {
	        mRun = run;
	    }
	 
	    @Override
	    public void run() {
	        Canvas canvas = null;
	        while (mRun) {
	            canvas = mHolder.lockCanvas();
	            if (canvas != null) {
	            	//TODO: only draw at certain intervals
	            	mGamePanel.animateCowBoys(mElapsed);
	            	mGamePanel.removeDeadCowboys();
	            	mGamePanel.doDraw(mElapsed, canvas);
	            	mElapsed = System.currentTimeMillis() - mStartTime;
	                mHolder.unlockCanvasAndPost(canvas);
	            }
	            mStartTime = System.currentTimeMillis();
	        }
	    }
}
