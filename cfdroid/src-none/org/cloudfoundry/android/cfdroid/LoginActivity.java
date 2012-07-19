package org.cloudfoundry.android.cfdroid;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.android.cfdroid.Preferences_;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.targets.CloudTarget;
import org.cloudfoundry.android.cfdroid.targets.TargetAdapter;
import org.cloudfoundry.android.cfdroid.targets.TargetsActivity_;
import org.cloudfoundry.client.lib.CloudFoundryClient;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.login)
public class LoginActivity extends SherlockActivity {

	@ViewById
	EditText login;

	@ViewById
	EditText password;

	@ViewById
	Spinner target;
	
	@Bean
	Clients clients;

	@Pref
	Preferences_ preferences;
	
	@Bean
	TargetAdapter targetAdapter;

	private List<CloudTarget> targets = new ArrayList<CloudTarget>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] raw = preferences.targets().get().split("\\|\\|");
		for (String r : raw) {
			targets.add(CloudTarget.parse(r));
		}

	}

	@AfterViews
	void handleTargets() {
		targetAdapter.setTargets(targets);
		target.setAdapter(targetAdapter);
	}

	@Click(R.id.manage)
	public void manageTargets() {
		TargetsActivity_.intent(this).start();
	}
	
	@Click
	@Background
	public void connect() {
		// String sLogin = login.getText().toString();
		String sLogin = "presidentielles.dataviz@gmail.com";

		try {
			// String sPassword = password.getText().toString();
			String sPassword = "2hX9AEKA";
			String sTarget = "https://api.cloudfoundry.com";
			sTarget = ((CloudTarget)target.getSelectedItem()).getURL();
			
			CloudFoundryClient client = new CloudFoundryClient(sLogin,
					sPassword, sTarget);
			client.login();
			clients.store(sLogin, sTarget, client);

			finish();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
