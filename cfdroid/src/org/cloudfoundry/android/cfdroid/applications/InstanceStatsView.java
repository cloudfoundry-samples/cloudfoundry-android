package org.cloudfoundry.android.cfdroid.applications;

import static android.text.format.Formatter.formatFileSize;
import static android.text.format.Formatter.formatShortFileSize;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.android.cfdroid.support.Colors;
import org.cloudfoundry.android.cfdroid.support.view.GradientProgressBar;
import org.cloudfoundry.client.lib.InstanceStats;
import org.cloudfoundry.client.lib.InstanceStats.Usage;

import android.graphics.Color;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

/**
 * A view for each instance stats line.
 * 
 * @author Eric Bottard
 * 
 */
public class InstanceStatsView extends BaseViewHolder<InstanceStats> {

	private TextView cpu;
	private TextView cpu_tv;
	private TextView ram_tv;
	private TextView disk_tv;
	private TextView uptime_tv;
	private int notAvailableText;

	private GradientProgressBar pbRam;
	private GradientProgressBar pbDisk;

	public InstanceStatsView(View container, int notAvailableText) {
		cpu = (TextView) container.findViewById(R.id.cpu);
		pbRam = (GradientProgressBar) container.findViewById(R.id.ram);
		pbDisk = (GradientProgressBar) container.findViewById(R.id.disk);

		cpu_tv = (TextView) container.findViewById(R.id.cpu_tv);
		ram_tv = (TextView) container.findViewById(R.id.ram_tv);
		disk_tv = (TextView) container.findViewById(R.id.disk_tv);
		uptime_tv = (TextView) container.findViewById(R.id.uptime_tv);
		this.notAvailableText = notAvailableText;

	}

	@Override
	public void bind(InstanceStats item) {
		Usage usage = item.getUsage();
		if (usage != null) {
			cpu.setText(String.format("%.1f%%", usage.getCpu()));

			cpu.setTextColor(//
			Colors.blend(cpu.getResources().getColor(R.color.status_ok), //
					cpu.getResources().getColor(R.color.status_bad), //
					(float) usage.getCpu()));

			pbRam.setProgress((int) (1024 * usage.getMem() / item.getMemQuota() * 100));
			pbDisk.setProgress((int) (1024 * usage.getDisk()
					/ item.getDiskQuota() * 100));

			cpu_tv.setText(String.format("%.1f%% (%d)", usage.getCpu(),
					item.getCores()));
			ram_tv.setText(pretty(1024 * (long) usage.getMem()) + " ("
					+ prettyShort(item.getMemQuota()) + ")");
			disk_tv.setText(String.format("%s (%s)", pretty(usage.getDisk()),
					prettyShort(item.getDiskQuota())));
			uptime_tv.setText(uptime(item.getUptime()));
		} else {
			cpu.setText(notAvailableText);
			cpu.setTextColor(cpu.getResources().getColor(R.color.cf_background));
			pbRam.setProgress(0);
			pbDisk.setProgress(0);
			cpu_tv.setText(notAvailableText);
			ram_tv.setText(notAvailableText);
			disk_tv.setText(notAvailableText);
		}
	}

	private String pretty(long size) {
		return formatFileSize(cpu.getContext(), size);
	}

	private String prettyShort(long size) {
		return formatShortFileSize(cpu.getContext(), size);
	}

	private String uptime(double seconds) {
		int s = (int) seconds;
		final int units[] = { 60, 60, 24 };
		Object[] result = new Integer[units.length + 1];
		for (int i = 0; i < units.length; i++) {
			result[i] = s % units[i];
			s = s / units[i];
		}
		result[units.length] = s;

		return String.format("%4$dd:%3$02dh:%2$02dm:%1$02ds", result);
	}

}
