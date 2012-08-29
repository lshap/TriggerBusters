package edu.upenn.cis.cis350;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class StartView extends View {

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;

	public StartView(Context c) {
		super(c);
	}

	public StartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final WindowManager w = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		final Display d = w.getDefaultDisplay();
		SCREEN_WIDTH = d.getWidth();
		SCREEN_HEIGHT = d.getHeight();
		invalidate();
	}

	public void onDraw(Canvas canvas) {
		int height = SCREEN_WIDTH * (529 / 489);
		Rect boundRect = new Rect(0, 0 + 30, SCREEN_WIDTH, height + 30);
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.frontpage), null, boundRect, null);
	}

	public boolean inBoyClick(float x, float y) {
		int left = SCREEN_WIDTH * 61 / 489;
		int right = SCREEN_WIDTH * 217 / 489;
		int top = SCREEN_HEIGHT * 90 / 529 + 30;
		int bottom = SCREEN_HEIGHT * 230 / 529 + 30;
		return x > left && x < right && y > top && y < bottom;
	}

	public boolean inGirlClick(float x, float y) {
		int left = SCREEN_WIDTH * 261 / 489;
		int right = SCREEN_WIDTH * 451 / 489;
		int top = SCREEN_HEIGHT * 90 / 529 + 30;
		int bottom = SCREEN_HEIGHT * 230 / 529 + 30;
		return x > left && x < right && y > top && y < bottom;
	}

}
