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
package org.cloudfoundry.android.cfdroid.targets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.SharedPreferences;

import com.google.inject.Inject;

public class TargetsPreferences {
	
	private static final String TARGETS_PREF_KEY = "targets";
	
	private static final Set<String> DEFAULT_TARGETS;
	static {
		DEFAULT_TARGETS = new HashSet<String>();
		DEFAULT_TARGETS.add("https://api.cloudfoundry.com|CloudFoundry");
		//DEFAULT_TARGETS.add("http://api.eu01.aws.af.cm|AppFog AWS EU");
	}
	private final SharedPreferences sharedPreferences;
	
	@Inject
	public TargetsPreferences(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}
	
	

	public List<CloudTarget> fromPrefs() {
		List<CloudTarget> targets = new ArrayList<CloudTarget>();
		Set<String> raw =  sharedPreferences.getStringSet(TARGETS_PREF_KEY, DEFAULT_TARGETS);
		for (String r : raw) {
			targets.add(CloudTarget.parse(r));
		}
		return targets;
	}
	
	public void toPrefs(List<CloudTarget> targets) {
		
		Set<String> raw = new HashSet<String>();
		for (CloudTarget target : targets) {
			raw.add(target.toPref());
		}
		sharedPreferences.edit().putStringSet(TARGETS_PREF_KEY, raw).commit();
	}
}
