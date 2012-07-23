package org.cloudfoundry.android.cfdroid.services;

public enum ServiceLogos {
	
	unknown(0), mongodb(1), mysql(2), postgresql(3), redis(4), rabbitmq(5);
	
	public int level;

	private ServiceLogos(int level) {
		this.level = level;
	}

}
