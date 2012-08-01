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
