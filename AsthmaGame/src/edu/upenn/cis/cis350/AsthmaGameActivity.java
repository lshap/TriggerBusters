package edu.upenn.cis.cis350;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class AsthmaGameActivity extends Activity {
	private long _startTime;
	private long _tempTime;
	private long _totalTime;
	private Timer _timer;
	private SoundManager mSoundManager;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		_startTime = System.currentTimeMillis();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
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
		_timer.scheduleAtFixedRate(timer_task, 0, 500);
		
		mSoundManager = new SoundManager();
		mSoundManager.initSounds(getBaseContext());
		mSoundManager.addSound(1, R.raw.beatbox);
		mSoundManager.addSound(2, R.raw.suck); 
		mSoundManager.addSound(3, R.raw.buzzer); 		
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
	
	public void onRightClick(View v) {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (grid.gameOver()) {
			mSoundManager.playSound(3);
			onEndGame(grid);
		}
		grid.getCharacter().setDirection(Direction.Right);
	}
	public void onLeftClick(View v) {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (grid.gameOver()) {
			mSoundManager.playSound(3);
			onEndGame(grid);
		}
		grid.getCharacter().setDirection(Direction.Left);
	}
	public void onUpClick(View v) {
		GameGridView grid = (GameGridView) (findViewById(R.id.gridview1));
		if (grid.gameOver()) {
			mSoundManager.playSound(3);
			onEndGame(grid);
		}
		grid.getCharacter().setDirection(Direction.Up);
	}
	public void onDownClick(View v) {
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