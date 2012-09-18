package org.cloudfoundry.android.cfdroid.support;

import roboguice.util.RoboAsyncTask;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Extension of {@link RoboAsyncTask} that shows an indeterminate progress
 * dialog while the task is running.
 * 
 * @author Eric Bottard
 * 
 * @param <T>
 *            result type of the computation
 */
public abstract class TaskWithDialog<T> extends RoboAsyncTask<T> {

	private ProgressDialog dialog;

	public TaskWithDialog(Context context, int message) {
		super(context);
		dialog = new ProgressDialog(context);
		dialog.setMessage(context.getText(message));
		dialog.setIndeterminate(true);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	@Override
	protected void onPreExecute() throws Exception {
		super.onPreExecute();
		dialog.show();
	}

	@Override
	protected void onFinally() throws RuntimeException {
		super.onFinally();
		dialog.dismiss();
	}
	
	@Override
	protected void onException(Exception e) throws RuntimeException {
		dialog.setTitle("An error has occured");
		dialog.setMessage(e.getMessage());
	}

}
