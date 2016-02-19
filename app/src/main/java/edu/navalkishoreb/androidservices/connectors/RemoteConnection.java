package edu.navalkishoreb.androidservices.connectors;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import edu.navalkishoreb.androidservices.interfaces.RemoteService;

/**
 * Created by navalb on 20-02-2016.
 */
public class RemoteConnection implements ServiceConnection {
	private final static String TAG = RemoteConnection.class.getSimpleName();

	private  Messenger remoteService;
	private final Messenger clientService;

	public RemoteConnection(Handler clientService) {
		this.clientService = new Messenger(clientService);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.d(TAG,"service connected");
		remoteService = new Messenger(service);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.d(TAG,"service dis-connected");
		remoteService = null;
	}

	public boolean isConnected(){
		return remoteService!=null;
	}


	public void encode(String data) throws RemoteException {
		if(remoteService!=null){
			remoteService.send(createMessage(data));
		}else{
			Log.e(TAG,"remote service is null");
		}
	}


	private Message createMessage(String data){
		Message message = Message.obtain();
		message.what = ToEncode.ENCODE;
		message.replyTo = clientService;
		Bundle bundle = new Bundle();
		bundle.putString("encode", data);
		message.setData(bundle);
		return message;
	}

	public void unbindService(Context context){
		Log.d(TAG,"unbinding service");
		context.unbindService(RemoteConnection.this);
		remoteService = null;
	}

}
