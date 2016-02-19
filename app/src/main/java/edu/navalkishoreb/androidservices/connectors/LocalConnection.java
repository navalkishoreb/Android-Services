package edu.navalkishoreb.androidservices.connectors;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import edu.navalkishoreb.androidservices.binders.LocalBinder;
import edu.navalkishoreb.androidservices.interfaces.LocalService;

/**
 * Created by navalb on 19-02-2016.
 */
public class LocalConnection implements ServiceConnection {
	private static final String TAG = LocalConnection.class.getSimpleName();
	private LocalService localService;

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.d(TAG, "service connected");
		if (service != null) {
			LocalBinder localBinder = (LocalBinder) service;
			localService = localBinder.getService();
		} else {
			Log.e(TAG, "service received is NULL. Might be binder not implemented");
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.d(TAG, "service disconnected");
		localService = null;
	}

	public String getOutput() {
		String output;
		if (localService != null) {
			output =  String.valueOf(localService.getRandomNumber());
		} else {
			Log.w(TAG, "local service is NULL.");
			output ="local service is NOT available";
		}
		return output;
	}

	public boolean isConnected(){
		return localService != null;
	}

	public void unbindingService(Context context){
		Log.e(TAG,"unbinding service");
		context.unbindService(LocalConnection.this);
		localService = null;
	}
}
