package edu.upenn.cis.cis350;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AsthmaGameActivity extends Activity {
	private long _startTime;
	private long _tempTime;
	private long _totalTime;
	private long _stepTime;
	private Timer _timer;
	private SoundManager mSoundManager;
	private boolean mouseDown;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		_startTime = System.currentTimeMillis();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		
		// set the amount of time between timer task calls to 200 milliseconds
		_stepTime = 200;
		_timer = new Timer();
		TimerTask timer_task = new TimerTask() {
			GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
			private Handler updateUI = new Handler() {
				@Override
				public void dispatchMessage(Message msg) {
					if(((GameGridView) grid).getClutterMoving()){
						mSoundManager.playSound(2);
						((GameGridView) grid).setClutterMoving();
					}
					((GameGridView) grid).timerCalled();
					_totalTime = (_tempTime + System.currentTimeMillis() - _startTime) / 900;
					grid.addTime(_totalTime);
					super.dispatchMessage(msg);
					checkForCollision();
				}
			};

			@Override
			public void run() {
				updateUI.sendEmptyMessage(0);
			}

		};
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		Intent i = getIntent();
    	int genderInt=i.getIntExtra("Gender", 0);
    	Gender gender=null;
    	if(genderInt==0){
    		gender=Gender.Boy;
    	}
    	else{
    		gender=Gender.Girl;
    	}
		grid.setGender(gender);
		_timer.scheduleAtFixedRate(timer_task, 0, _stepTime);
		
		mSoundManager = new SoundManager();
		mSoundManager.initSounds(getBaseContext());
		mSoundManager.addSound(1, R.raw.beatbox);
		mSoundManager.addSound(2, R.raw.suck); 
		mSoundManager.addSound(3, R.raw.buzzer); 		
	}
	
	public boolean onTouchEvent(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_UP) {
			mouseDown = false;
			return true;
		} else if (e.getAction() == MotionEvent.ACTION_DOWN) {
			if (mouseDown) {
				return false;
			}
			
			mouseDown = true;
			
			GameGridView gameview = (GameGridView) (findViewById(R.id.gridview1));
			float x = e.getX();
			Log.v("x", "xcurr " + x);
			float y = e.getY();
			Log.v("y", "ycurr " + y);
		
			processClick(x,y, gameview.SCREEN_WIDTH, gameview.SCREEN_HEIGHT);
			Log.v("width", "width: " + gameview.SCREEN_WIDTH);
			Log.v("height", "height: " + gameview.SCREEN_HEIGHT);
		}

		return false;
	}
	
	private void processClick(float x, float y, float width, float height)
	{
		Log.v("xpos, ypos", "x: "+ x + "y: " + y);
		// make sure click is within joystick bounds
		if (x > width/2 + 90 || x < width/2 - 90 || y < height - 180)
		{
			Log.v("not in rectangle", "x: "+ x + "y: " + y);
			return;
		}
		
		// left
		else if (x < width/2 - 40)
		{
			this.onLeftClick();
		}
		
		// right
		else if (x > width/2 + 40)
		{
			this.onRightClick();
		}
		// up
		else if (y < height - 140)
		{
			this.onUpClick();
		}
		// down
		else if (y > height - 50)
		{
			this.onDownClick();
		}		
	}

	public boolean onKeyDown(int keycode, KeyEvent event) {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (grid.gameOver()) {
			mSoundManager.playSound(3);
			onEndGame(grid);
		}
		super.onKeyDown(keycode, event);
		if (grid.getPause() && keycode != KeyEvent.KEYCODE_SPACE) {
			return false;
		}
		if (keycode == KeyEvent.KEYCODE_DPAD_UP) {
			grid.getCharacter().setDirection(Direction.Up);
			return true;
		} else if (keycode == KeyEvent.KEYCODE_DPAD_DOWN) {
			grid.getCharacter().setDirection(Direction.Down);
			return true;
		} else if (keycode == KeyEvent.KEYCODE_DPAD_LEFT) {
			grid.getCharacter().setDirection(Direction.Left);
			return true;
		} else if (keycode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			grid.getCharacter().setDirection(Direction.Right);
			return true;
		} else if (keycode == KeyEvent.KEYCODE_SPACE) {
			grid.setPause();
			if(!grid.getPause()){
				_startTime=System.currentTimeMillis();
			}
			else{
				_tempTime+=System.currentTimeMillis() - _startTime;
			}
			return true;
		}
		return false;
	}
	
	public void onRightClick() {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (grid.gameOver()) {
			mSoundManager.playSound(3);
			onEndGame(grid);
		}
		grid.getCharacter().setDirection(Direction.Right);
	}
	public void onLeftClick() {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (grid.gameOver()) {
			mSoundManager.playSound(3);
			onEndGame(grid);
		}
		grid.getCharacter().setDirection(Direction.Left);
	}
	public void onUpClick() {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (grid.gameOver()) {
			mSoundManager.playSound(3);
			onEndGame(grid);
		}
		grid.getCharacter().setDirection(Direction.Up);
	}
	public void onDownClick() {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (grid.gameOver()) {
			mSoundManager.playSound(3);
			onEndGame(grid);
		}
		grid.getCharacter().setDirection(Direction.Down);
	}
	public void onPauseClick(View v) {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (grid.gameOver()) {
			mSoundManager.playSound(3);
			onEndGame(grid);
		}
		grid.setPause();
		if(!grid.getPause()){
			_startTime=System.currentTimeMillis();
		}
		else{
			_tempTime+=System.currentTimeMillis() - _startTime;
		}
	}

	private void checkForCollision() {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		boolean collides = grid.getCollision();
		if (collides) {
			grid.setPause();
			removeDialog(0);
			showDialog(0);
		}
		grid.setCollision(false);
	}

	public Dialog onCreateDialog(int id) {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (id == 0) {
			_tempTime+=System.currentTimeMillis() - _startTime;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// this is the message to display
			if(grid.getMeter()==1){
				builder.setMessage(R.string.collision_message2);	
			}
			else{
				
				switch(grid.getEncounteredObject()){
				case COCKROACH:
					builder.setMessage(R.string.collision_message_cockroach);
					break;
				case MOLD:
					builder.setMessage(R.string.collision_message_mold);
					break;
				case CAT:
					builder.setMessage(R.string.collision_message_pet);
					break;
				case SMOKE:
					builder.setMessage(R.string.collision_message_smoke);
					break;
				default:
					builder.setMessage(R.string.collision_message);
					break;
				}
			}
			// this is the button to display
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						// this is the method to call when the button is clicked
						public void onClick(DialogInterface dialog, int id) {
							_startTime=System.currentTimeMillis();
							// this will hide the dialog
							dialog.cancel();
							GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
							grid.setPause();
						}
					});
			mSoundManager.playSound(1);
			return builder.create(); 

		}
		return null;
	}

	public void onEndGame(View view) {
		cancelTimer();
		GameGridView grid = (GameGridView) view;
		if (grid.getPause()) {
			_startTime = System.currentTimeMillis();
		}
		_totalTime=grid.getScore();
		Intent i = new Intent(this, ScoreShow.class);
		i.putExtra("Milliseconds", _totalTime);
		startActivity(i);
	}
	//methods to help in testing
	public void cancelTimer(){
		_timer.cancel();
	}
	
	public void runManually(){
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		grid.timerCalled();
	}
	

	/*
	 * public void updatePause() { TextView v = (TextView)
	 * findViewById(R.id.Pause); String x = "Pause"; if (_paused) { x =
	 * "Resume"; } else { x = "Pause"; } v.setText(x); }
	 * 
	 * public void onPauseClick(View view) { _paused = !_paused; if (_paused) {
	 * _tempTime += System.currentTimeMillis() - _startTime; } else { _startTime
	 * = System.currentTimeMillis(); } updatePause(); }
	 */
}