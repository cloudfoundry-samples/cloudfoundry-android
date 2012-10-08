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
