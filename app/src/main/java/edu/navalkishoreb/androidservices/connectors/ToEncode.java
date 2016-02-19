package edu.navalkishoreb.androidservices.connectors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import edu.navalkishoreb.androidservices.interfaces.RemoteService;

/**
 * Created by navalb on 20-02-2016.
 * this handler class belongs to remote service where encoding will be performed
 */
public class ToEncode extends Handler {
	private static final String TAG = ToDisplay.class.getSimpleName();
	public static final int ENCODE = 910;
	private final RemoteService remoteService;

	public ToEncode(RemoteService remoteService) {
		this.remoteService = remoteService;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Log.v(TAG, "handle message  to encode");
		switch (msg.what) {
			case ENCODE:
				String data = msg.getData().getString("encode");
				String encoded = remoteService.encode(data);
				Log.d(TAG, "encoded " + encoded);
				sendToDisplay(msg.replyTo,encoded);
				break;
			default:
		}
	}


	private Message createMessage(String encoded) {
		Message message = Message.obtain();
		message.what = ToDisplay.DISPLAY;
		Bundle bundle = new Bundle();
		bundle.putString("display", encoded);
		message.setData(bundle);
		return message;
	}

	private void sendToDisplay(Messenger display, String encoded) {
		if(display!=null) {
			try {
				Log.e(TAG,"sending to display");
				display.send(createMessage(encoded));
			} catch (RemoteException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
		}else{
			Log.e(TAG,"display messenger is NULL");
		}
	}
}
