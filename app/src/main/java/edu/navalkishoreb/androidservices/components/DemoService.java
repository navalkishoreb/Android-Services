package edu.navalkishoreb.androidservices.components;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Random;

import edu.navalkishoreb.androidservices.binders.LocalBinder;
import edu.navalkishoreb.androidservices.connectors.ToEncode;
import edu.navalkishoreb.androidservices.enums.IntentAction;
import edu.navalkishoreb.androidservices.interfaces.LocalService;
import edu.navalkishoreb.androidservices.interfaces.RemoteService;

public class DemoService extends Service implements LocalService , RemoteService{
	private final static String TAG = DemoService.class.getSimpleName();
	/**
	 * icon
	 */
	private View overlayView;
	private WindowManager windowManager;

	private boolean isRunning = false;
	private int startValue = Service.START_NOT_STICKY;

	public DemoService() {
	}

	public static void startService(Context context, IntentAction intentAction) {
		Log.d(TAG, "-fired up intent to start service");
		Intent intent = new Intent();
		intent.setAction(intentAction.toString());
		intent.setClass(context, DemoService.class);
		context.startService(intent);
	}

	public static void stopService(Context context) {
		Log.d(TAG, "-fired up intent to stop service");
		Intent intent = new Intent();
		intent.setClass(context, DemoService.class);
		context.stopService(intent);
	}

	public static void bindService(Context context, ServiceConnection serviceConnection,IntentAction intentAction) {
		Log.d(TAG, "-fired up intent to bind service");
		Intent intent = new Intent();
		intent.setAction(intentAction.toString());
		intent.setClass(context, DemoService.class);
		context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "------------service created------------");
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "service started with start id " + startId);
		if (!isRunning) {
			String action = handleIntent(intent);
			reportFlags(flags);
			showIcon();
			startValue = getStartValue(action);
			isRunning = true;
		} else {
			Log.d(TAG, "service is already running");
		}
		return startValue;
	}

	/**
	 * available from API 14
	 */
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
		Log.d(TAG, "task was removed");
	}

	private String handleIntent(Intent intent) {
		String action = null;
		if (intent != null) {
			action = intent.getAction();
		} else {
			Log.d(TAG, "intent received is NULL");
		}
		return action;
	}

	private void reportFlags(int flags) {
		switch (flags) {
			case START_FLAG_REDELIVERY:
				Log.d(TAG, "reporting flag START_FLAG_REDELIVERY");
				break;
			case START_FLAG_RETRY:
				Log.d(TAG, "reporting flag START_FLAG_RETRY");
				break;
			default:
				Log.d(TAG, "reporting flag " + flags);
		}
	}

	private int getStartValue(String action) {
		int startValue = START_NOT_STICKY;
		if (action != null) {
			if (action.equals(IntentAction.START_NOT_STICKY.toString())) {
				Log.i(TAG, "START_NOT_STICKY");
				startValue = START_NOT_STICKY;
			} else if (action.equals(IntentAction.START_STICKY.toString())) {
				Log.i(TAG, "START_STICKY");
				startValue = START_STICKY;
			} else if (action.equals(IntentAction.START_REDELIVERY.toString())) {
				Log.i(TAG, "START_REDELIVER_INTENT");
				startValue = START_REDELIVER_INTENT;
			} else if (action.equals(IntentAction.START_STICKY_COMPATIBILITY.toString())) {
				Log.i(TAG, "START_STICKY_COMPATIBILITY");
				startValue = START_STICKY_COMPATIBILITY;
			}
		} else {
			Log.e(TAG, "action was NULL default START_NOT_STICKY");
		}
		return startValue;
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (intent == null) {
			Log.e(TAG, "binding intent is null");
			return null;
		}
		return handleBindIntent(intent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.e(TAG, "unbinding service");

		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hideIcon();
		Log.d(TAG, "------------service destroyed------------");
	}



	/**
	 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
	 */
	private void showIcon() {
		if (overlayView == null) {
			overlayView = new LinearLayout(this);
			windowManager.addView(overlayView, getWindowProperties());
		}
		overlayView.setBackgroundColor(Color.BLUE);
	}

	private WindowManager.LayoutParams getWindowProperties() {
		int flag = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		int size = getStatusBarHeight();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(size, size, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, flag, PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.TOP | Gravity.END;
		return params;
	}

	public int getStatusBarHeight() {
		int result = 20;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	private void hideIcon() {
		if (overlayView != null) {
			windowManager.removeView(overlayView);
			overlayView = null;
		} else {
			Log.w(TAG, "icon is not present");
		}
	}

	private void stopService() {
		stopSelf();
	}

	private IBinder handleBindIntent(Intent intent) {
		String action = intent.getAction();
		IBinder iBinder = null;
		if(action!=null) {
			if (action.equals(IntentAction.BOUND_LOCAL.toString())) {
				iBinder = new LocalBinder(this);
			} else if (action.equals(IntentAction.BOUND_REMOTE.toString())) {
				iBinder = new Messenger(new ToEncode(DemoService.this)).getBinder();
			}
		}else{
			Log.e(TAG,"intent action is NULL");
		}

		return iBinder;
	}

	@Override
	public int getRandomNumber() {
		return new Random(Calendar.getInstance().getTimeInMillis()).nextInt();
	}

	@Override
	public String encode(String data) {
		/*Log.d(TAG,"making receiver wait for few seconds");
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		return String.valueOf(data.hashCode());
	}
}
