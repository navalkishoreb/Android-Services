package edu.navalkishoreb.androidservices.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.navalkishoreb.androidservices.R;
import edu.navalkishoreb.androidservices.connectors.RemoteConnection;
import edu.navalkishoreb.androidservices.connectors.ToDisplay;
import edu.navalkishoreb.androidservices.enums.IntentAction;

public class RemoteBoundServices extends AppCompatActivity implements View.OnClickListener {
	private final static String TAG = RemoteBoundServices.class.getSimpleName();

	private EditText inputText;
	private TextView outputText;
	private RemoteConnection remoteConnection;

	public static void launch(Context context) {
		Log.d(TAG, "-fired up intent to start activity");
		Intent intent = new Intent();
		intent.setClass(context, RemoteBoundServices.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_bound_services);
		setListeners();
		inputText = (EditText) findViewById(R.id.edt_remote_bound_encode);
		outputText = (TextView) findViewById(R.id.tv_remote_bound_decoded);
		remoteConnection = new RemoteConnection(new ToDisplay(RemoteBoundServices.this));
	}

	private void setListeners() {
		findViewById(R.id.btn_remote_bound_decode).setOnClickListener(this);
		findViewById(R.id.btn_remote_bound_bind).setOnClickListener(this);
		findViewById(R.id.btn_remote_bound_unbind).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_remote_bound_decode) {
			encode();
		} else if (v.getId() == R.id.btn_remote_bound_bind) {
			bindService();
		} else if (v.getId() == R.id.btn_remote_bound_unbind) {
			unbindService();
		} else {
			Log.e(TAG, v.getId() + " click not handled");
		}
	}

	private void encode() {
		Log.d(TAG, "encoding data");
		String input = inputText.getText().toString();
		outputText.setText("waiting for response...");
		if (!input.isEmpty()) {
			if (remoteConnection.isConnected()) {
				try {
					Log.d(TAG, "remote service called");
					remoteConnection.encode(input);
				} catch (RemoteException e) {
					e.printStackTrace();
					Log.e(TAG, e.getMessage());
				}
			} else {
				Log.e(TAG, "remote connection is NULL");
			}
		} else {
			Log.e(TAG, "useless attempt to encode empty text");
		}
	}

	private void bindService() {
		Log.d(TAG, "binding service");
		if (remoteConnection == null || !remoteConnection.isConnected()) {
			DemoService.bindService(RemoteBoundServices.this, remoteConnection, IntentAction.BOUND_REMOTE);
		} else {
			Log.d(TAG, "service is already connected");
		}
	}

	private void unbindService() {
		Log.d(TAG, "unbinding service");
		if (remoteConnection != null) {
			remoteConnection.unbindService(RemoteBoundServices.this);
		} else {
			Log.d(TAG, "remote connection is NULL");
		}
	}

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

	public void setEncodedData(String data) {
		if (data != null) {
			Log.d(TAG, "setting data");
			outputText.setText(data);
		} else {
			Log.e(TAG, "encoded data is NULL");
		}
	}
}
