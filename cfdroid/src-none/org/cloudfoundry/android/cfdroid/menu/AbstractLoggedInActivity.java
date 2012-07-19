package org.cloudfoundry.android.cfdroid.menu;


import org.cloudfoundry.android.cfdroid.R;

import android.content.Intent;

import com.actionbarsherlock.app.SherlockActivity;
import org.cloudfoundry.android.cfdroid.DashboardActivity_;
import org.cloudfoundry.android.cfdroid.LoginActivity_;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;

/**
 * Base class for activities which appear after login (and hence allow logout).
 * 
 * @author ebottard
 */
@EActivity
@OptionsMenu(R.menu.logout)
public abstract class AbstractLoggedInActivity extends SherlockActivity {

	@OptionsItem(R.id.logout)
	protected void logout() {
		DashboardActivity_.intent(this).doLogout(true)
				.flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
	}

}
