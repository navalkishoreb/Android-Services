package edu.navalkishoreb.androidservices.enums;

/**
 * Created by navalb on 19-02-2016.
 */
public enum IntentAction {
	START_STICKY_COMPATIBILITY("edu.navalkishoreb.start_sticky_compatibility"),
	START_STICKY("edu.navalkishoreb.start_sticky"),
	START_NOT_STICKY("edu.navalkishoreb.start_not_sticky"),
	START_REDELIVERY("edu.navalkishoreb.start_redelivery"),
	BOUND_LOCAL("edu.navalkishoreb.bound_local"),
	BOUND_REMOTE("edu.navalkishoreb.bound_remote");

	private String action;

	IntentAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return action;
	}
}
