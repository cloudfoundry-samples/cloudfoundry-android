package org.cloudfoundry.android.cfdroid.account;

public class Accounts {
	
	/**
	 * Also seen in res/xml/authenticator.xml.
	 */
	public static final String ACCOUNT_TYPE = "org.cloudfoundry";
	
	private Accounts() {
		
	}
	
	public static String extractName(String stored) {
		return stored.split("\n")[0];
	}
	
	public static String extractTarget(String stored) {
		return stored.split("\n")[1];
	}
	
	public static String toStoredForm(String name, String target) {
		return name + "\n" + target;
	}

}
