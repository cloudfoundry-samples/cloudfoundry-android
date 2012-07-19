package org.cloudfoundry.android.cfdroid.menu;


import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.R.id;

import android.content.Intent;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

/**
 * Base class for activities which appear after login (and hence allow logout).
 * 
 * @author ebottard
 */
public abstract class AbstractLoggedInActivity extends RoboSherlockActivity {

	protected void logout() {
//		DashboardActivity_.intent(this).doLogout(true)
//				.flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);
        if (handled) {
            return true;
        }
        switch (item.getItemId()) {
            case id.logout:
                logout();
                return true;
            default:
                return false;
        }
    }

}
