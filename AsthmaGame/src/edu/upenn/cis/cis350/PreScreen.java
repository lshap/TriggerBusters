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

public class PreScreen extends Activity {
	private Timer _timer;
	private int genderInt;
	private PreScreen pscreen = this;
	private SoundManager mSoundManager;
	PreGameView grid;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prescreen);
		grid = (PreGameView) (findViewById(R.id.pregameview1));
		grid.setPause();
		showDialog(1);
		removeDialog(1);
		showDialog(1);
		_timer = new Timer();
		TimerTask timer_task = new TimerTask() {
			//PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
			private Handler updateUI = new Handler() {
				@Override
				public void dispatchMessage(Message msg) {
					
					((PreGameView) grid).timerCalled();
					super.dispatchMessage(msg);
					checkForCollision();
				}
			};

			@Override
			public void run() {
				updateUI.sendEmptyMessage(0);
			}

		};
		
		Intent i = getIntent();
		genderInt = i.getIntExtra("Gender", 0);
		Gender gender = Gender.Boy;
		if (genderInt == 0) {
			gender = Gender.Boy;
		} else {
			gender = Gender.Girl;
		}
		grid.setGender(gender);
		_timer.scheduleAtFixedRate(timer_task, 0, 500);
		mSoundManager = new SoundManager();
		mSoundManager.initSounds(getBaseContext());
		mSoundManager.addSound(3, R.raw.powerup); 		
	}

	public boolean onKeyDown(int keycode, KeyEvent event) {
		//PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
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
			return true;
		}
		return false;
	}
	
	public void onRightClick(View v) {
		PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
		grid.getCharacter().setDirection(Direction.Right);
	}
	public void onLeftClick(View v) {
		PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
		grid.getCharacter().setDirection(Direction.Left);
	}
	public void onUpClick(View v) {
		PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
		grid.getCharacter().setDirection(Direction.Up);
	}
	public void onDownClick(View v) {
		PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
		grid.getCharacter().setDirection(Direction.Down);
	}

	private void checkForCollision() {
		PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
		grid.checkCollision();
		boolean collides = grid.getCollision();
		if (collides) {
			grid.setPause();
			showDialog(0);
			removeDialog(0);
			showDialog(0);
		}
	}

	public Dialog onCreateDialog(int i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// this is the message to display
		if (i == 0) {
			mSoundManager.playSound(3);
			builder.setMessage(R.string.inhaler_message2);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						// this is the method to call when the button is clicked
						public void onClick(DialogInterface dialog, int id) {
							// this will hide the dialog
							PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
							dialog.cancel();
							_timer.cancel();
							Intent i = new Intent(pscreen, AsthmaGameActivity.class);
							i.putExtra("Gender", genderInt);
							startActivity(i);

						}
					});
			return builder.create();

		} else if (i == 1) {
			builder.setMessage(R.string.inhaler_message1);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						// this is the method to call when the button is clicked
						public void onClick(DialogInterface dialog, int id) {
							// this will hide the dialog
							PreGameView grid = (PreGameView) (findViewById(R.id.pregameview1));
							dialog.cancel();
							grid.setPause();
				
						}
					});
			return builder.create();
		} else {
			return null;

		}

	
		
	}
	
	
	

}
