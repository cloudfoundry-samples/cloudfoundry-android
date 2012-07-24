package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.Clients;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DataHolder;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudApplication.AppState;
import org.cloudfoundry.client.lib.CloudInfo;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import static com.actionbarsherlock.app.SherlockFragmentActivity.OnCreateOptionsMenuListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

public class ApplicationControlFragment extends RoboSherlockFragment implements
		OnCreateOptionsMenuListener {

	private static int log2(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		return 31 - Integer.numberOfLeadingZeros(n);
	}

	private int baseMemoryUnit;

	@Inject
	private Clients clients;

	private int initialInstances, initialMemory;

	private OnSeekBarChangeListener instancesSBCL = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			int instancesNewValue = instancesP2R(progress);
			if (!fromUser) {
				return;
			}
			memorySeekBar.setSecondaryProgress(memR2P(maxWithoutOthers
					/ instancesNewValue));
			overallMemoryPb.setSecondaryProgress(usedByOtherApps()
					+ instancesNewValue * memory());
			instancestv.setText("" + instancesNewValue);
			getActivity().invalidateOptionsMenu();
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	};

	@InjectView(R.id.instances_seekbar)
	private SeekBar instancesSeekBar;

	@InjectView(R.id.instances)
	private TextView instancestv;

	private int maxWithoutOthers;

	private OnSeekBarChangeListener memorySBCL = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			int memoryNewValue = memP2R(progress);
			if (!fromUser) {
				return;
			}
			instancesSeekBar.setSecondaryProgress(instancesR2P(maxWithoutOthers
					/ memoryNewValue));
			overallMemoryPb.setSecondaryProgress(usedByOtherApps()
					+ memoryNewValue * instances());
			memorytv.setText("" + memoryNewValue);
			getActivity().invalidateOptionsMenu();

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	};

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

		setHasOptionsMenu(true);

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
		instancesSeekBar.setSecondaryProgress(instancesR2P(maxWithoutOthers
				/ initialMemory));
		instancesSeekBar.setOnSeekBarChangeListener(instancesSBCL);

		memorySeekBar.setMax(memR2P(maxTotalMemory));
		memorySeekBar.setProgress(memR2P(cloudApplication.getMemory()));
		memorySeekBar.setSecondaryProgress(memR2P(maxWithoutOthers
				/ initialInstances));
		memorySeekBar.setOnSeekBarChangeListener(memorySBCL);

		overallMemoryPb.setMax(maxTotalMemory);
		overallMemoryPb.setProgress(usedByOtherApps);
		overallMemoryPb.setSecondaryProgress(usedByOtherApps + initialInstances
				* initialMemory); 

	}

	private int usedByOtherApps() {
		return overallMemoryPb.getProgress();
	}

	private boolean userChangedSettings() {
		return instances() != initialInstances
				|| memory() != initialMemory;
	}

}
