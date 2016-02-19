package edu.navalkishoreb.androidservices.components;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import edu.navalkishoreb.androidservices.R;
import edu.navalkishoreb.androidservices.enums.IntentAction;

public class StartServices extends AppCompatActivity implements View.OnClickListener {
	private static final String TAG = StartServices.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_services);
		Log.d(TAG, "screen created");
		setActionBar();
		setListeners();
	}

	private void setActionBar() {
		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle(R.string.start_services);
		} else {
			Log.e(TAG, "action bar is not present");
		}
	}

	private void setListeners() {
		Log.d(TAG, "setting up listeners");
		findViewById(R.id.btn_sticky).setOnClickListener(this);
		findViewById(R.id.btn_not_sticky).setOnClickListener(this);
		findViewById(R.id.btn_redelivery).setOnClickListener(this);
		findViewById(R.id.btn_stop).setOnClickListener(this);
		findViewById(R.id.btn_exception).setOnClickListener(this);
		findViewById(R.id.btn_next).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_sticky) {
			startService(IntentAction.START_STICKY);
		} else if (v.getId() == R.id.btn_not_sticky) {
			startService(IntentAction.START_NOT_STICKY);
		} else if (v.getId() == R.id.btn_redelivery) {
			startService(IntentAction.START_REDELIVERY);
		} else if (v.getId() == R.id.btn_stop) {
			stopService();
		} else if (v.getId() == R.id.btn_exception) {
			exception();
		} else if (v.getId() == R.id.btn_next) {
			nextTopic(true);
		} else {
			Log.w(TAG, v.getId() + "not handled");
		}
	}

	private void startService(IntentAction intentAction) {
		DemoService.startService(getApplicationContext(), intentAction);
	}

	private void stopService() {
		DemoService.stopService(getApplicationContext());
	}

	private void exception() {
		throw new IllegalStateException("throw any exception");
	}

	private void nextTopic(boolean flag) {
		if (flag) {
			stopService();
		} else {
			Log.d(TAG, "not stopping service");
		}
		LocalBoundServices.launch(StartServices.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_start_services, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_dont_destroy) {
			nextTopic(false);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
}
