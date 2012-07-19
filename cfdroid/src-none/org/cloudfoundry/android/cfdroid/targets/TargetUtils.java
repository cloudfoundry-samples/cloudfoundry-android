package org.cloudfoundry.android.cfdroid.targets;

import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.android.cfdroid.Preferences_;

public class TargetUtils {

	public static List<CloudTarget> fromPrefs(Preferences_ prefs) {
		List<CloudTarget> targets = new ArrayList<CloudTarget>();
		String[] raw = prefs.targets().get().split("\\|\\|");
		for (String r : raw) {
			targets.add(CloudTarget.parse(r));
		}
		return targets;
	}
	
	public static void toPrefs(List<CloudTarget> targets, Preferences_ prefs) {
		StringBuilder sb = new StringBuilder();
		for (CloudTarget target : targets) {
			sb.append(target.toPref()).append("||");
		}
		prefs.edit().targets().put(sb.toString()).apply();
	}
}
