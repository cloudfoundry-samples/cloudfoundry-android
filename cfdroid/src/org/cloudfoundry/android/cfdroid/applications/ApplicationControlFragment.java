package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.Clients;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DataHolder;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudApplication.AppState;
import org.cloudfoundry.client.lib.CloudInfo;

import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import android.os.Bundle;
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
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

public class ApplicationControlFragment extends RoboSherlockFragment  {

	private static int log2(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		return 31 - Integer.numberOfLeadingZeros(n);
	}

	private int baseMemoryUnit;

	@Inject
	private Clients clients;

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
	
	private void constrainBars() {
		if (instancesSeekBar.getProgress() > instancesSeekBar.getSecondaryProgress()) {
			instancesSeekBar.setProgress(instancesSeekBar.getSecondaryProgress());
		}
		if (memorySeekBar.getProgress() > memorySeekBar.getSecondaryProgress()) {
			memorySeekBar.setProgress(memorySeekBar.getSecondaryProgress());
		}
	}
	
	private void updateData() {
		memorySeekBar.setSecondaryProgress(memR2P(maxWithoutOthers
				/ instances()));
		instancestv.setText("" + instances());
		
		instancesSeekBar.setSecondaryProgress(instancesR2P(maxWithoutOthers
				/ memory()));
		overallMemoryPb.setSecondaryProgress(usedByOtherApps()
				+ memory() * instances());
		memorytv.setText("" + memory());
		getActivity().invalidateOptionsMenu();
		
	}

	@InjectView(R.id.memory_seekbar)
	private SeekBar memorySeekBar;

	@InjectView(R.id.memory)
	private TextView memorytv;

	@InjectView(R.id.overall_memory_progressbar)
	private ProgressBar overallMemoryPb;

	@InjectView(R.id.restart)
	private View restartBtn;

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
	public boolean onOptionsItemSelected(MenuItem item) {
		if (R.id.cloud_apply == item.getItemId()) {
			saveChanges();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	

	private void saveChanges() {
		new RoboAsyncTask<Void>(getActivity()) {
			@Override
			public Void call() throws Exception {
				if( instances() != initialInstances) {
					clients.getClient().updateApplicationInstances(getCloudApplication().getName(), instances());
				}
				if (memory() != initialMemory) {
					clients.getClient().updateApplicationMemory(getCloudApplication().getName(), memory());
				}
				return null;
			}
			protected void onFinally() throws RuntimeException {
				fullyRedrawWidgets();
				getActivity().invalidateOptionsMenu();
			}
		}.execute();
		
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fullyRedrawWidgets(); 
		setHasOptionsMenu(true);

	}

	private void fullyRedrawWidgets() {
		CloudApplication cloudApplication = getCloudApplication();
		initialInstances = cloudApplication.getInstances();
		initialMemory = cloudApplication.getMemory();

		AppState state = cloudApplication.getState();
		startBtn.setEnabled(state == AppState.STOPPED);
		stopBtn.setEnabled(state == AppState.STARTED);

		CloudInfo info = clients.getCloudInfo();
		int maxTotalMemory = info.getLimits().getMaxTotalMemory();
		int usedByOtherApps = info.getUsage().getTotalMemory()
				- initialInstances * initialMemory;
		maxWithoutOthers = maxTotalMemory - usedByOtherApps;
		baseMemoryUnit = clients.getClient().getApplicationMemoryChoices()[0];

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
		return instances() != initialInstances
				|| memory() != initialMemory;
	}

}
