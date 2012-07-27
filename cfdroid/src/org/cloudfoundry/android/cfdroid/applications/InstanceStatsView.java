package org.cloudfoundry.android.cfdroid.applications;

import static android.text.format.Formatter.formatFileSize;
import static android.text.format.Formatter.formatShortFileSize;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.InstanceStats;
import org.cloudfoundry.client.lib.InstanceStats.Usage;

import android.view.View;
import android.widget.TextView;

/**
 * A view for each instance stats line.
 * 
 * @author Eric Bottard
 * 
 */
public class InstanceStatsView extends BaseViewHolder<InstanceStats> {

	private TextView cpu;
	private TextView memory;
	private TextView disk;
	private TextView uptime;
	private int notAvailableText;

	public InstanceStatsView(View container, int notAvailableText) {
		cpu = (TextView) container.findViewById(R.id.cpu);
		memory = (TextView) container.findViewById(R.id.memory);
		disk = (TextView) container.findViewById(R.id.disk);
		uptime = (TextView) container.findViewById(R.id.uptime);
		this.notAvailableText = notAvailableText;
	}

	@Override
	public void bind(InstanceStats item) {
		Usage usage = item.getUsage();
		if (usage != null) {
			cpu.setText(String.format("%.1f%% (%d)", usage.getCpu(),
					item.getCores()));
			memory.setText(pretty(1024 * (long) usage.getMem()) + " ("
					+ prettyShort(item.getMemQuota()) + ")");
			disk.setText(String.format("%s (%s)", pretty(usage.getDisk()),
					prettyShort(item.getDiskQuota())));
			uptime.setText(uptime(item.getUptime()));
		} else {
			cpu.setText(notAvailableText);
			memory.setText(notAvailableText);
			disk.setText(notAvailableText);
			uptime.setText(notAvailableText);
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
