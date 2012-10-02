package org.cloudfoundry.android.cfdroid.account;

import static android.accounts.AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE;
import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import static android.accounts.AccountManager.KEY_INTENT;

import org.cloudfoundry.client.lib.CloudFoundryClient;

import roboguice.util.Ln;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CloudFoundryAccountAuthenticator extends
		AbstractAccountAuthenticator {

	private Context context;
	
	public CloudFoundryAccountAuthenticator(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
        final Intent intent = new Intent(context, LoginActivity.class);
        //intent.putExtra(PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_INTENT, intent);
        return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		String password = AccountManager.get(context).getPassword(account);
		String login = Accounts.extractName(account.name);
		String target = Accounts.extractTarget(account.name);
		try {
			CloudFoundryClient client = new CloudFoundryClient(login, password, target);
			String token = client.login();
			Bundle bundle = new Bundle();
			bundle.putString(KEY_ACCOUNT_NAME, account.name);
			bundle.putString(KEY_ACCOUNT_TYPE, Accounts.ACCOUNT_TYPE);
			bundle.putString(KEY_AUTHTOKEN, token);
			return bundle;
		} catch (Exception e) {
			throw new NetworkErrorException(e);
		}
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
	}

}
