package org.cloudfoundry.android.cfdroid.menu;

import org.cloudfoundry.android.cfdroid.menu.AbstractLoggedInActivity_;
import org.cloudfoundry.android.cfdroid.R;

import android.content.Intent;

import org.cloudfoundry.android.cfdroid.DashboardActivity_;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;

/**
 * Base class for activities which appear deeper than the dashboard screen (and
 * hence allow going back 'home').
 * 
 * @author ebottard
 * 
 */
@EActivity
@OptionsMenu(R.menu.general)
public abstract class AbstractNotRootActivity extends AbstractLoggedInActivity_ {

	@AfterViews
	protected void allowHome() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	@OptionsItem(android.R.id.home)
	protected void home() {
		DashboardActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
				.start();
	}

}
