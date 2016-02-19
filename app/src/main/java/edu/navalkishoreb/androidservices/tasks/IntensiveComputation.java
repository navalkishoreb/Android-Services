package edu.navalkishoreb.androidservices.tasks;

import android.util.Log;

/**
 * Created by navalb on 19-02-2016.
 */
public class IntensiveComputation implements Runnable {
	private static final String TAG = IntensiveComputation.class.getSimpleName();
	long sleep = 0;

	IntensiveComputation(long sleep) {
		this.sleep = sleep;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}
}
