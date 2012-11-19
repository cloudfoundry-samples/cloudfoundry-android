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
package org.cloudfoundry.android.cfdroid.cloud;

/**
 * See res/drawable/runtime_logos.xml.
 * 
 * @author Eric Bottard
 * 
 */
public enum RuntimeLogos {

	unknown(0), erlang(1), python(2), ruby(3), php(4), node(5), java(6);

	public final int level;

	private RuntimeLogos(int level) {
		this.level = level;
	}

	/**
	 * Simple heuristic for getting the best fit. Different cloud vendors use
	 * different strings here, get the longest prefix that matches.
	 */
	public static RuntimeLogos bestMatch(String raw) {
		RuntimeLogos best = unknown;
		int bestLength = 0;
		for (RuntimeLogos r : RuntimeLogos.values()) {
			if (raw.startsWith(r.name()) && r.name().length() > bestLength) {
				best = r;
				bestLength = r.name().length();
			}
		}
		return best;
	}

}
