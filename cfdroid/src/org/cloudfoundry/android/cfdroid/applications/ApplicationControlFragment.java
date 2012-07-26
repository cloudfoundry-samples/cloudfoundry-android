package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.android.cfdroid.support.DeferredContentFragment;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DataHolder;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudApplication.AppState;
import org.cloudfoundry.client.lib.CloudInfo;

import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

public class ApplicationControlFragment extends DeferredContentFragment<ApplicationControlFragment.AsyncResult> {

	private static int log2(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		return 31 - Integer.numberOfLeadingZeros(n);
	}

	/*default*/ static class AsyncResult {
		private CloudInfo cloudInfo;
		
		private int[] memoryOptions;
	}
	
	private int baseMemoryUnit;

	@Inject
	private CloudFoundry client;

	private int initialInstances, initialMemory;

	@InjectView(R.id.instances_seekbar)
	private SeekBar instancesSeekBar;

	@InjectView(R.id.instances)
	private TextView instancestv;

	private int maxWithoutOthers;

	private OnSeekBarChangeListener barsListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			constrainBars();
			if (!fromUser) {
				return;
			}
			updateData();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	};

	/**
	 * Prevents the user from sliding the primary progress ahead of the
	 * secondary progress.
	 */
	private void constrainBars() {
		if (instancesSeekBar.getProgress() > instancesSeekBar
				.getSecondaryProgress()) {
			instancesSeekBar.setProgress(instancesSeekBar
					.getSecondaryProgress());
		}
		if (memorySeekBar.getProgress() > memorySeekBar.getSecondaryProgress()) {
			memorySeekBar.setProgress(memorySeekBar.getSecondaryProgress());
		}
	}

	/**
	 * Updates widgets (notably limits conveyed by seekbars secondary progress) to reflect new data.
	 */
	private void updateData() {
		memorySeekBar.setSecondaryProgress(memR2P(maxWithoutOthers
				/ instances()));
		instancestv.setText("" + instances());

		instancesSeekBar.setSecondaryProgress(instancesR2P(maxWithoutOthers
				/ memory()));
		overallMemoryPb.setSecondaryProgress(usedByOtherApps() + memory()
				* instances());
		memorytv.setText("" + memory());
		getActivity().invalidateOptionsMenu();

	}

	@InjectView(R.id.memory_seekbar)
	private SeekBar memorySeekBar;

	@InjectView(R.id.memory)
	private TextView memorytv;

	@InjectView(R.id.overall_memory_progressbar)
	private ProgressBar overallMemoryPb;

	@InjectView(R.id.start)
	private View startBtn;

	@InjectView(R.id.stop)
	private View stopBtn;

	private CloudApplication getCloudApplication() {
		return ((DataHolder<CloudApplication>) getActivity()).getSelectedItem();
	}

	private int instances() {
		return instancesP2R(instancesSeekBar.getProgress());
	}

	/**
	 * Convert from seekbar domain (0 - x) to instances count domain (1 - x+1)
	 */
	private int instancesP2R(int p) {
		return p + 1;
	}

	/**
	 * Convert from instances count (1 - x) to progress domain (0 - x-1).
	 */
	private int instancesR2P(int i) {
		return i - 1;
	}

	private int memory() {
		return memP2R(memorySeekBar.getProgress());
	}

	/**
	 * Convert from seekbar domain (0 - i) to memory domain (64 - 2048).
	 */
	private int memP2R(int p) {
		return baseMemoryUnit * (1 << p);
	}

	/**
	 * Convert from mem progress domain to actual memory domain.
	 */
	private int memR2P(int mem) {
		return log2((mem / baseMemoryUnit));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.app_control, menu);
		menu.findItem(R.id.cloud_apply).setEnabled(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.application_control, container, false);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.cloud_apply).setEnabled(userChangedSettings());
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		startBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				start();
			}

		});
		stopBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stop();
			}
		});
	}

	private void start() {
		final Dialog dialog = showDialog(R.string.working);
		new RoboAsyncTask<AsyncResult>(getActivity()) {
			@Override
			public AsyncResult call() throws Exception {
				client.startApplication(getCloudApplication().getName());
				return latestRemoteState();
			}
			protected void onSuccess(AsyncResult t) throws Exception {
				fullyRedrawWidgets(t);
				getActivity().invalidateOptionsMenu();
			}
			protected void onFinally() throws RuntimeException {
				dialog.dismiss();
			}
		}.execute();
	}
	
	private void stop() {
		final Dialog dialog = showDialog(R.string.working);
		new RoboAsyncTask<AsyncResult>(getActivity()) {
			@Override
			public AsyncResult call() throws Exception {
				client.stopApplication(getCloudApplication().getName());
				return latestRemoteState();
			}
			protected void onSuccess(AsyncResult t) throws Exception {
				fullyRedrawWidgets(t);
				getActivity().invalidateOptionsMenu();
			}
			protected void onFinally() throws RuntimeException {
				dialog.dismiss();
			}
		}.execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (R.id.cloud_apply == item.getItemId()) {
			saveChanges();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	private Dialog showDialog(int message) {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage(getActivity().getText(message));
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        return dialog;
	}

	private void saveChanges() {
		final Dialog dialog = showDialog(R.string.working);
		new RoboAsyncTask<AsyncResult>(getActivity()) {
			@Override
			public AsyncResult call() throws Exception {
				if (instances() != initialInstances) {
					client.updateApplicationInstances(
							getCloudApplication().getName(), instances());
				}
				if (memory() != initialMemory) {
					client.updateApplicationMemory(
							getCloudApplication().getName(), memory());
				}
				return latestRemoteState();
			}
			
			protected void onSuccess(AsyncResult t) throws Exception {
				fullyRedrawWidgets(t);
				getActivity().invalidateOptionsMenu();
			}
			protected void onFinally() throws RuntimeException {
				dialog.dismiss();
			}

		}.execute();

	}

	@Override
	public void onDataAvailable(AsyncResult data) {
		fullyRedrawWidgets(data);
		setHasOptionsMenu(true);
	}
	
	private void fullyRedrawWidgets(AsyncResult data) {
		CloudApplication cloudApplication = getCloudApplication();
		initialInstances = cloudApplication.getInstances();
		initialMemory = cloudApplication.getMemory();

		AppState state = cloudApplication.getState();
		startBtn.setEnabled(state != AppState.STARTED);
		stopBtn.setEnabled(state == AppState.STARTED);

		CloudInfo info = data.cloudInfo;
		int maxTotalMemory = info.getLimits().getMaxTotalMemory();
		int usedByOtherApps = info.getUsage().getTotalMemory()
				- initialInstances * initialMemory;
		maxWithoutOthers = maxTotalMemory - usedByOtherApps;
		baseMemoryUnit = data.memoryOptions[0];

		instancesSeekBar.setMax(instancesR2P(maxTotalMemory / baseMemoryUnit));
		instancesSeekBar.setProgress(instancesR2P(cloudApplication
				.getInstances()));
		instancesSeekBar.setOnSeekBarChangeListener(barsListener);

		memorySeekBar.setMax(memR2P(maxTotalMemory));
		memorySeekBar.setProgress(memR2P(cloudApplication.getMemory()));
		memorySeekBar.setOnSeekBarChangeListener(barsListener);

		overallMemoryPb.setMax(maxTotalMemory);
		overallMemoryPb.setProgress(usedByOtherApps);
		updateData();
	}

	private int usedByOtherApps() {
		return overallMemoryPb.getProgress();
	}

	private boolean userChangedSettings() {
		return instances() != initialInstances || memory() != initialMemory;
	}

	@Override
	public Loader<AsyncResult> onCreateLoader(int arg0, Bundle arg1) {
		return new AsyncLoader<AsyncResult>(getActivity()) {
			@Override
			public AsyncResult loadInBackground() {
				return latestRemoteState();
			}
			
		};
	}

	private AsyncResult latestRemoteState() {
		AsyncResult result = new AsyncResult();
		result.cloudInfo = client.getCloudInfo();
		result.memoryOptions = client.getApplicationMemoryChoices();
		return result;
	}

}
