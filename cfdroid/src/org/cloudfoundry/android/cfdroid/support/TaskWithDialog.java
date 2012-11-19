/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
