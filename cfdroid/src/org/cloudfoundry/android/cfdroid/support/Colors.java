package org.cloudfoundry.android.cfdroid.support;

import android.graphics.Color;

/**
 * Miscellaneous utils for working with colors.
 * 
 * @author Eric Bottard
 * 
 */
public class Colors {

	private Colors() {

	}

	/**
	 * Return a {@link Color} that is (1-p) like {@code from} and p like {@code to}.
	 */
	public static int blend(int from, int to, float weigh) {
		int a = (int) ((1 - weigh) * Color.alpha(from) + weigh * Color.alpha(to));
		int r = (int) ((1 - weigh) * Color.red(from) + weigh * Color.red(to));
		int g = (int) ((1 - weigh) * Color.green(from) + weigh * Color.green(to));
		int b = (int) ((1 - weigh) * Color.blue(from) + weigh * Color.blue(to));

		return Color.argb(a, r, g, b);

	}
}
