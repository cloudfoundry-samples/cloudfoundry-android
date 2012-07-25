package org.cloudfoundry.android.cfdroid.account;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

import java.util.List;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseTextWatcher;
import org.cloudfoundry.android.cfdroid.targets.CloudTarget;
import org.cloudfoundry.android.cfdroid.targets.TargetAdapter;
import org.cloudfoundry.android.cfdroid.targets.TargetsActivity;
import org.cloudfoundry.android.cfdroid.targets.TargetsPreferences;
import org.cloudfoundry.client.lib.CloudFoundryClient;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockAccountAuthenticatorActivity;
import com.google.inject.Inject;

@ContentView(R.layout.login)
public class LoginActivity extends RoboSherlockAccountAuthenticatorActivity {

	@InjectView(R.id.login)
	EditText login;

	@InjectView(R.id.password)
	EditText password;

	@InjectView(R.id.target)
	Spinner targetSpinner;

	@InjectView(R.id.connect)
	Button connect;

	@InjectView(R.id.manage)
	View manageTargets;

	@Inject
	TargetsPreferences targetsPreferences;

	@Inject
	AccountManager accountManager;

	private class UpdateUiTextWatcher extends BaseTextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			updateEnablement();
		}
	}

	private void updateEnablement() {
		connect.setEnabled(loginEnabled());
	}

	private boolean loginEnabled() {
		return login.getText().length() > 0 && password.getText().length() > 0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		// ActionBar ab = getActionBar();
		// ab.setDisplayShowTitleEnabled(false);
		// ab.setDisplayShowHomeEnabled(false);

		UpdateUiTextWatcher watcher = new UpdateUiTextWatcher();
		login.addTextChangedListener(watcher);
		password.addTextChangedListener(watcher);

		updateEnablement();

		password.setOnKeyListener(new View.OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event != null && ACTION_DOWN == event.getAction()
						&& keyCode == KEYCODE_ENTER && loginEnabled()) {
					handleLogin();
					return true;
				} else
					return false;
			}
		});

		password.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == IME_ACTION_DONE && loginEnabled()) {
					handleLogin();
					return true;
				}
				return false;
			}
		});

		connect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleLogin();
			}
		});

		manageTargets.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				manageTargets();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		List<CloudTarget> targets = targetsPreferences.fromPrefs();
		TargetAdapter adapter = new TargetAdapter(R.layout.target_item,
				getLayoutInflater(), targets);
		targetSpinner.setAdapter(adapter);
	}

	private void manageTargets() {
		startActivity(new Intent(this, TargetsActivity.class));
	}

	private void handleLogin() {
		final String sLogin = login.getText().toString().trim();
		final String sPassword = password.getText().toString().trim();
		final String sTarget = ((CloudTarget) targetSpinner.getSelectedItem())
				.getURL();

		RoboAsyncTask<String> task = new RoboAsyncTask<String>(this) {
			@Override
			public String call() throws Exception {
				CloudFoundryClient client = new CloudFoundryClient(sLogin,
						sPassword, sTarget);
				String result = client.login();

				return result;
			}

			@Override
			protected void onSuccess(String token) throws Exception {
				Account account = new Account(Accounts.toStoredForm(sLogin,
						sTarget), Accounts.ACCOUNT_TYPE);
				accountManager.addAccountExplicitly(account, sPassword, null);
				finish();
			}

			@Override
			protected void onException(Exception e) throws RuntimeException {
				Ln.e(e);
				Toast.makeText(LoginActivity.this, e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		};
		task.execute();
	}

}
