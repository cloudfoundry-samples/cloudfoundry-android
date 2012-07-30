package org.cloudfoundry.android.cfdroid.applications;

/**
 * See res/drawable/runtime_logos.xml.
 * 
 * @author Eric Bottard
 * 
 */
public enum RuntimeLogos {

	unknown(0), erlang(1), python(2), ruby(3), php(4), node(5), java(6);

	public int level;

	private RuntimeLogos(int level) {
		this.level = level;
	}

	/**
	 * Simple heuristic for getting the best fit. Different cloud vendors use
	 * different strings here, get the longest prefi that matches.
	 */
	public static RuntimeLogos match(String raw) {
		RuntimeLogos best = null;
		int bestLength = 0;
		for (RuntimeLogos r : RuntimeLogos.values()) {
			if(r.name().startsWith(raw) && r.name().length() > bestLength) {
				best = r;
				bestLength = r.name().length();
			}
		}
		return best;
	}

}
