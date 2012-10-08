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
 * See src/drawable/framework_logos.xml.
 * 
 * @author Eric Bottard
 * 
 */
public enum FrameworkLogos {

	unknown(0), play(1), rails3(2), grails(3), lift(4), sinatra(5), java_web(6), standalone(
			7), spring(8), rack(9), node(10), php(11), django(12);

	public int level;

	private FrameworkLogos(int level) {
		this.level = level;
	}

}
