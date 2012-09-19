package org.cloudfoundry.android.cfdroid.cloud;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.client.lib.CloudInfo;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;

/**
 * A fragment for displaying basic information about the current cloud.
 * 
 * @author Eric Bottard
 * 
 */
public class BasicInfoFragment extends
		ListLoadingFragment<BasicInfoFragment.TitleAndValue> {

	/* default */static class TitleAndValue {
		private int label;

		private Object value;

		private TitleAndValue(int label, Object value) {
			this.label = label;
			this.value = value;
		}
	}

	/* default */static class TitleAndValueView extends
			BaseViewHolder<TitleAndValue> {

		private TextView label;

		private TextView value;

		private TitleAndValueView(View container) {
			label = (TextView) container.findViewById(R.id.label);
			value = (TextView) container.findViewById(R.id.value);
		}

		@Override
		public void bind(TitleAndValue item) {
			label.setText(item.label);
			value.setText(item.value.toString());
		}

	}

	@Inject
	private CloudFoundry client;

	@Override
	public Loader<List<TitleAndValue>> onCreateLoader(int id, Bundle args) {
		return new AsyncLoader<List<TitleAndValue>>(getActivity()) {

			@Override
			public List<TitleAndValue> loadInBackground() {
				CloudInfo info = client.getCloudInfo();
				List<TitleAndValue> result = new ArrayList<BasicInfoFragment.TitleAndValue>();
				result.add(new TitleAndValue(R.string.cloud_info_description,
						info.getDescription()));
				result.add(new TitleAndValue(R.string.cloud_info_name, info
						.getName()));
				result.add(new TitleAndValue(R.string.cloud_info_build, info
						.getBuild()));
				result.add(new TitleAndValue(R.string.cloud_info_user, info
						.getUser()));
				result.add(new TitleAndValue(R.string.cloud_info_quota_apps,
						info.getUsage().getApps() + " / "
								+ info.getLimits().getMaxApps()));
				result.add(new TitleAndValue(R.string.cloud_info_quota_mem,
						info.getUsage().getTotalMemory() + " / "
								+ info.getLimits().getMaxTotalMemory()));
				result.add(new TitleAndValue(
						R.string.cloud_info_quota_services, info.getUsage()
								.getServices()
								+ " / "
								+ info.getLimits().getMaxServices()));
				result.add(new TitleAndValue(R.string.cloud_info_quota_uris,
						info.getUsage().getUrisPerApp() + " / "
								+ info.getLimits().getMaxUrisPerApp()));
				return result;
			}
		};
	}

	@Override
	protected ItemListAdapter<TitleAndValue, ?> adapterFor(
			List<TitleAndValue> items) {
		return new ItemListAdapter<BasicInfoFragment.TitleAndValue, BaseViewHolder<TitleAndValue>>(
				R.layout.label_and_value_list_item, getActivity()
						.getLayoutInflater(), items) {

			@Override
			protected BaseViewHolder<TitleAndValue> createView(View view) {
				return new TitleAndValueView(view);
			}
		};
	}

	@Override
	protected int loaderId() {
		return R.id.cloud_info_loader;
	}
}
