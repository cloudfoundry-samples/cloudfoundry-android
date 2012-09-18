package org.cloudfoundry.android.cfdroid.applications;

import java.util.List;

import javax.inject.Inject;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.client.lib.CloudApplication;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;

public class ApplicationInfoFragment extends RoboSherlockListFragment implements DetailPaneEventsCallback {
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		List<String> urls = getCloudApplication().getUris();
		setListAdapter(new ItemListAdapter<String, ApplicationLinkView>(R.layout.application_url_item, getActivity().getLayoutInflater(), urls) {

			@Override
			protected ApplicationLinkView createView(View view) {
				return new ApplicationLinkView(view);
			}
		});
	}
	
	@Inject
	private CloudFoundry client;
	
	private int position;
	
	@Override
	public void selectionChanged(int position) {
		this.position = position;
	}
	
	private CloudApplication getCloudApplication() {
		return client.getApplications(false).get(position); 
	}
	
	/*default*/static class ApplicationLinkView extends BaseViewHolder<String> {
		private TextView text;
		private ApplicationLinkView(View v) {
			text = (TextView) v;
			text.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
		@Override
		public void bind(String item) {
			text.setText("http://" + item);
		}
	}

}
