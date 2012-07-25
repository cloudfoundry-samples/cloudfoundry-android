package org.cloudfoundry.android.cfdroid.account;

public class AccountUtils {
	
	private AccountUtils() {
		
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
