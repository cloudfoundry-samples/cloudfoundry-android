package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.android.cfdroid.support.DeferredContentFragment;
import org.cloudfoundry.android.cfdroid.support.TaskWithDialog;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudApplication.AppState;
import org.cloudfoundry.client.lib.CloudInfo;

import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.database.ContentObserver;
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

/**
 * A fragment for controlling a particular app. Allows start/stop as well as
 * changing number of instances and memory quota.
 * 
 * Assumes that memory management is done in powers of 2.
 * 
 * @author Eric Bottard
 * 
 */
public class ApplicationControlFragment extends
		DeferredContentFragment<ApplicationControlFragment.AsyncResult>
implements DetailPaneEventsCallback{

	/* default */static class AsyncResult {
		private CloudInfo cloudInfo;

		private int[] memoryOptions;
	}

	private static int log2(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		return 31 - Integer.numberOfLeadingZeros(n);
	}
	
	private int position;

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
	 * The lowest memory unit an app can have. Current heuristics for getting
	 * that are a bit broken.
	 */
	private int baseMemoryUnit;

	@Inject
	private CloudFoundry client;

	private CloudInfo cloudInfo;

	/**
	 * Used to react to changes to application(s), eg after start/stop.
	 */
	private ContentObserver contentObserver = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Ln.d("ControlFragment received new data");
					fullyRedrawWidgets();
				}
			});
		}
	};

	/**
	 * This is to enable/diable the save button.
	 */
	private int initialInstances, initialMemory;
	
	@InjectView(R.id.instances_seekbar)
	private SeekBar instancesSeekBar;

	@InjectView(R.id.instances)
	private TextView instancestv;

	/**
	 * This is the (total) maximum memory that this app could take out of this
	 * cloud, once other apps have taken their share.
	 */
	private int maxWithoutOthers;

	@InjectView(R.id.memory_seekbar)
	private SeekBar memorySeekBar;

	@InjectView(R.id.memory)
	private TextView memorytv;

	@InjectView(R.id.overall_memory_progressbar)
	private ProgressBar overallMemoryPb;

	@InjectView(R.id.start)
	private View startBtn;
	
	@InjectView(R.id.status)
	private TextView status;
	
	@InjectView(R.id.stop)
	private View stopBtn;

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
	
	private void fullyRedrawWidgets() {
		CloudApplication cloudApplication = getCloudApplication();
		initialInstances = cloudApplication.getInstances();
		initialMemory = cloudApplication.getMemory();

		AppState state = cloudApplication.getState();
		startBtn.setEnabled(state != AppState.STARTED);
		stopBtn.setEnabled(state == AppState.STARTED);
		status.setText(state.toString());
		status.setTextColor(getActivity().getResources().getColor(ApplicationView.COLORS.get(state)));

		int maxTotalMemory = cloudInfo.getLimits().getMaxTotalMemory();
		int usedByOtherApps = cloudInfo.getUsage().getTotalMemory()
				- initialInstances * initialMemory;
		maxWithoutOthers = maxTotalMemory - usedByOtherApps;

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
	
	private CloudApplication getCloudApplication() {
		return client.getApplications(false).get(position);
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

	/*
	 * To be run on a background thread.
	 */
	private AsyncResult latestRemoteState() {
		AsyncResult result = new AsyncResult();
		result.cloudInfo = client.getCloudInfo();
		result.memoryOptions = client.getApplicationMemoryChoices();
		return result;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			position = savedInstanceState.getInt("position");
		}
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
	public void onDataAvailable(AsyncResult data) {
		baseMemoryUnit = data.memoryOptions[0];
		cloudInfo = data.cloudInfo;
		fullyRedrawWidgets();
		setHasOptionsMenu(true); // first time
		getActivity().invalidateOptionsMenu(); // subsequent calls
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
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.cloud_apply).setEnabled(userChangedSettings());
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("position", position);
	}

	@Override
	public void onStart() {
		super.onStart();
		client.listenForApplicationsUpdates(contentObserver);
	}

	@Override
	public void onStop() {
		client.stopListeningForApplicationUpdates(contentObserver);
		super.onStop();
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

	private void saveChanges() {
		new TaskWithDialog<AsyncResult>(getActivity(), R.string.working) {
			@Override
			public AsyncResult call() throws Exception {
				if (memory() != initialMemory) {
					client.updateApplicationMemory(getCloudApplication()
							.getName(), memory());
					// Mem changes need restart to take effect
					if (getCloudApplication().getState() == AppState.STARTED) {
						client.stopApplication(getCloudApplication().getName());
						client.startApplication(getCloudApplication().getName());
					}
				}
				if (instances() != initialInstances) {
					client.updateApplicationInstances(getCloudApplication()
							.getName(), instances());
				}
				// Need to re-query eg max maxWithoutOthers
				return latestRemoteState();
			}
			protected void onSuccess(AsyncResult t) throws Exception {
				onDataAvailable(t);
			};
			
		}.execute();
	}

	@Override
	public void selectionChanged(int position) {
		this.position = position;
//		appName = client.getApplications(false).get(position).getName();
	}

	private void start() {
		new TaskWithDialog<Void>(getActivity(), R.string.working) {
			@Override
			public Void call() throws Exception {
				client.startApplication(getCloudApplication().getName());
				return null;
			}
		}.execute();
	}

	private void stop() {
		new TaskWithDialog<Void>(getActivity(), R.string.working) {
			@Override
			public Void call() throws Exception {
				client.stopApplication(getCloudApplication().getName());
				return null;
			}
		}.execute();
	}

	/**
	 * Updates widgets (notably limits conveyed by seekbars secondary progress)
	 * to reflect new data.
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

	private int usedByOtherApps() {
		return overallMemoryPb.getProgress();
	}

	private boolean userChangedSettings() {
		return instances() != initialInstances || memory() != initialMemory;
	}

}
