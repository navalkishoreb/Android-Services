package edu.navalkishoreb.androidservices.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import edu.navalkishoreb.androidservices.R;
import edu.navalkishoreb.androidservices.connectors.LocalConnection;
import edu.navalkishoreb.androidservices.enums.IntentAction;

public class LocalBoundServices extends AppCompatActivity implements View.OnClickListener {
	private static final String TAG = LocalBoundServices.class.getSimpleName();

	public static void launch(Context context) {
		Log.d(TAG, "-fired up intent to start activity");
		Intent intent = new Intent();
		intent.setClass(context, LocalBoundServices.class);
		context.startActivity(intent);
	}

	private TextView output;
	private LocalConnection localConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_bound_services);
		output = (TextView) findViewById(R.id.tv_output);
		setListeners();
		localConnection = new LocalConnection();
	}

	private void setListeners() {
		findViewById(R.id.btn_getOutput).setOnClickListener(this);
		findViewById(R.id.btn_next).setOnClickListener(this);
		findViewById(R.id.btn_bindService).setOnClickListener(this);
		findViewById(R.id.btn_unbindService).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_getOutput) {
			setOutput();
		} else if (v.getId() == R.id.btn_next) {
			nextTopic();
		} else if (v.getId() == R.id.btn_bindService) {
			bindService();
		} else if (v.getId() == R.id.btn_unbindService) {
			unbindService();
		} else {
			Log.w(TAG, v.getId() + "not handled");
		}
	}
	
	private void nextTopic() {
		Log.w(TAG, "launching remote bound service");
		RemoteBoundServices.launch(LocalBoundServices.this);
	}

	private void setOutput() {
		if (localConnection != null) {
			Log.d(TAG, "output placed");
			output.setText(localConnection.getOutput());
		} else {
			Log.e(TAG, "localConnection is NULL");
		}
	}

	private void bindService() {
		Log.d(TAG, "binding service");
		if (!localConnection.isConnected()) {
			DemoService.bindService(LocalBoundServices.this, localConnection, IntentAction.BOUND_LOCAL);
		} else {
			Log.d(TAG, "service is already connected");
		}
	}

	private void unbindService() {
		Log.d(TAG, "un-binding service");
		if (localConnection.isConnected()) {
			localConnection.unbindingService(LocalBoundServices.this);
		} else {
			Log.d(TAG, "service is already dis connected");
		}
	}

	/**
	 * one must explicitly bind and unbind service
	 */

	@Override
	protected void onStart() {
		super.onStart();
		bindService();
	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService();
	}
}
