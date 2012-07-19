package org.cloudfoundry.android.cfdroid.targets;


public class CloudTarget {
	
	private CloudTarget() {
	}

	public CloudTarget(String label, String uRL) {
		super();
		this.label = label;
		URL = uRL;
	}

	/*default*/ String label;
	
	/*default*/ String URL;
	
	public String getLabel() {
		return label;
	}

	public String getURL() {
		return URL;
	}
	
	public static CloudTarget parse(String raw) {
		int pipe = raw.indexOf('|');
		CloudTarget result = new CloudTarget();
		result.URL = raw.substring(0, pipe);
		result.label = raw.substring(pipe + 1, raw.length());
		return result;
	}
	
	public String toPref() {
		return URL + '|' + label;
	}
	
	@Override
	public String toString() {
		return label;
	}
	
}
