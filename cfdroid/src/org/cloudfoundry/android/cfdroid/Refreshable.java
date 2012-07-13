package org.cloudfoundry.android.cfdroid;

public interface Refreshable<R> {

	void publishResult(R result);
	
	void onException(Exception e);
}
