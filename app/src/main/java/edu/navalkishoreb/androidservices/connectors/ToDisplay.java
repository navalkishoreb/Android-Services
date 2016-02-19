package edu.navalkishoreb.androidservices.connectors;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.navalkishoreb.androidservices.components.RemoteBoundServices;

/**
 * Created by navalb on 20-02-2016.
 */
public class ToDisplay extends Handler {
	private static final String TAG = ToDisplay.class.getSimpleName();
	public static final int DISPLAY = 2738;
	private final RemoteBoundServices remoteBoundServices;

	public ToDisplay(RemoteBoundServices remoteBoundServices) {
		this.remoteBoundServices = remoteBoundServices;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Log.v(TAG,"handle message  to display");
		switch (msg.what){
			case DISPLAY:
				String encoded = msg.getData().getString("display");
				remoteBoundServices.setEncodedData(encoded);
				break;
				default:
		}
	}
}
