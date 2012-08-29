package edu.upenn.cis.cis350;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreShow extends Activity {
	
	private static long _highScore;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.score);
    	update();
	}
	
	public void onStartScreenClick(View v) {
		Intent i = new Intent(this, StartPage.class);
		startActivity(i);
	}

	public void update(){
		Intent i = getIntent();
    	long time=i.getLongExtra("Milliseconds", 0);
    	TextView v=(TextView) findViewById(R.id.counter);
    	if(_highScore<time){
    		_highScore=time;}
    	String x=Long.toString(_highScore);
    	v.setText(x);
    		
	}
}
