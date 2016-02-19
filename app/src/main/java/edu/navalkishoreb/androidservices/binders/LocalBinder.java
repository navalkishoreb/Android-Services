package edu.navalkishoreb.androidservices.binders;

import android.os.Binder;

import edu.navalkishoreb.androidservices.interfaces.LocalService;

/**
 * Created by navalb on 19-02-2016.
 */
public class LocalBinder extends Binder {
	private final LocalService localService;

	/**
	 * Class used for the client Binder.  Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public LocalBinder(LocalService localService) {this.localService = localService;}

	public LocalService getService() {
		return localService;
	}
}
