package org.cloudfoundry.android.cfdroid.account;


import static android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Authenticator service that returns our particular subclass of
 * AbstractAccountAuthenticator in onBind().
 */
public class AccountAuthenticatorService extends Service {
	
	private static CloudFoundryAccountAuthenticator singleton = null;

	@Override
	public IBinder onBind(Intent intent) {
		return intent.getAction().equals(ACTION_AUTHENTICATOR_INTENT) ? getAuthenticator()
				.getIBinder() : null;
	}

	private  CloudFoundryAccountAuthenticator getAuthenticator() { 
		if (singleton == null) {
			singleton = new CloudFoundryAccountAuthenticator(this);
		}
		return singleton;
	}

}
