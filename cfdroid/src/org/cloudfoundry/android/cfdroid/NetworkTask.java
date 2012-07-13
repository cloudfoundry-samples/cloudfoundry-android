package org.cloudfoundry.android.cfdroid;

import java.util.concurrent.Callable;

import android.content.Context;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

@EBean
public class NetworkTask<R> {

	private R result;
	
	private volatile boolean dlInProgress;
	
	private Callable<R> callable;
	
	@RootContext
	Context context;
	
	@Background
	public void work() {
		dlInProgress = true;
		
		try {
			result = callable.call();
			
			((Refreshable<R>)context).publishResult(result);
		} catch (Exception e) {
			((Refreshable<R>)context).onException(e);
		}
		
		dlInProgress = false;
	}
	
}
