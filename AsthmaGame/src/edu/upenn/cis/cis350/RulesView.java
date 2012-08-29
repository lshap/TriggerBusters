package edu.upenn.cis.cis350;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class RulesView extends View {

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private int _picture;

	public RulesView(Context c) {
		super(c);
	}

	public RulesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final WindowManager w = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		final Display d = w.getDefaultDisplay();
		SCREEN_WIDTH = d.getWidth();
		SCREEN_HEIGHT = d.getHeight();
		_picture = R.drawable.instructionsa;
		invalidate();
	}

	public void onDraw(Canvas canvas) {
		Rect boundRect = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-100);
		canvas.drawBitmap(
				BitmapFactory.decodeResource(getResources(), _picture), null,
				boundRect, null);
	}

	public boolean leftArrow(float x, float y) {
		int left = SCREEN_WIDTH * 65 / 480;
		int right = SCREEN_WIDTH * 140 / 480;
		int top = SCREEN_HEIGHT * 561 / 640;
		int bottom = SCREEN_HEIGHT * 617 / 640;
		return x > left && x < right && y > top && y < bottom;
	}

	public boolean rightArrow(float x, float y) {
		int left = SCREEN_WIDTH * 335 / 480;
		int right = SCREEN_WIDTH * 410 / 480;
		int top = SCREEN_HEIGHT * 561 / 640;
		int bottom = SCREEN_HEIGHT * 617 / 640;
		return x > left && x < right && y > top && y < bottom;
	}

	public boolean picture1() {
		return _picture == R.drawable.instructionsa;
	}
	
	public void switchPicture(){
		if(_picture == R.drawable.instructionsa){
			_picture = R.drawable.instructionsb;
		}
		else{
			_picture = R.drawable.instructionsa;
		}
		invalidate();
	}

}
