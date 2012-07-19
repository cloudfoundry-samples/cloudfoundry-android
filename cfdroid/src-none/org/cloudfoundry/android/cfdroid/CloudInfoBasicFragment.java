package org.cloudfoundry.android.cfdroid;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.client.lib.CloudInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.cloud_info_basic)
public class CloudInfoBasicFragment extends Fragment implements HasTitle {
	
	@ViewById(R.id.cloud_info_name)
	TextView ciName;

	@ViewById(R.id.cloud_info_build)
	TextView ciBuild;

	@ViewById(R.id.cloud_info_version)
	TextView ciVersion;

	@ViewById(R.id.cloud_info_description)
	TextView ciDescription;
	
	@ViewById(R.id.cloud_info_quota_apps)
	TextView quotaApps;
	
	@ViewById(R.id.cloud_info_quota_services)
	TextView quotaServices;
	
	@ViewById(R.id.cloud_info_quota_uris)
	TextView quotaURIs;
	
	@ViewById(R.id.cloud_info_quota_mem)
	TextView quotaMem;
	
	@ViewById(R.id.cloud_info_user)
	TextView ciUser;
	
	@StringRes(R.string.ci_basic_title)
	String title;
	
	@Override
	public CharSequence getTitle() {
		return title;
	}

	void updateData(CloudInfo info) {
		ciName.setText(info.getName());
		ciVersion.setText(info.getVersion());
		ciBuild.setText("" + info.getBuild());
		ciDescription.setText(info.getDescription());
		
		ciUser.setText(info.getUser());
		
		quotaApps.setText(info.getUsage().getApps() + "/" + info.getLimits().getMaxApps());
		quotaMem.setText(info.getUsage().getTotalMemory() + "/" + info.getLimits().getMaxTotalMemory());
		quotaServices.setText(info.getUsage().getServices() + "/" + info.getLimits().getMaxServices());
		quotaURIs.setText(info.getUsage().getUrisPerApp() + "/" + info.getLimits().getMaxUrisPerApp());
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

}
