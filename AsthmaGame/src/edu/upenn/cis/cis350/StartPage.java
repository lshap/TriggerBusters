package edu.upenn.cis.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class StartPage extends Activity {
	private boolean reached;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		reached=false;
	}
	
	public void onRulesClick(View v) {
		Intent i = new Intent(this, RulesPage.class);
		startActivity(i);
	}
	
	
	
	
	public boolean onTouchEvent(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_UP) {
			reached = false;
			return true;
		} else if (e.getAction() == MotionEvent.ACTION_DOWN) {
			if(reached){
				return false;
			}
			reached=true;
			StartView view = (StartView) (findViewById(R.id.startview1));
			float x=e.getX();
			float y=e.getY();
			if(view.inBoyClick(x, y)){
				Intent i = new Intent(this, PreScreen.class);
				i.putExtra("Gender", 0);
				startActivity(i);
			}
			else if(view.inGirlClick(x, y)){
				Intent i = new Intent(this, PreScreen.class);
				i.putExtra("Gender", 1);
				startActivity(i);
			}
		}
			return false;
	}

	
	
	
}
