package org.cloudfoundry.android.cfdroid.targets;

import java.util.List;

import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;

import android.view.LayoutInflater;
import android.view.View;

public class TargetAdapter extends ItemListAdapter<CloudTarget, TargetView>{

	public TargetAdapter(int viewId, LayoutInflater inflater,
			List<CloudTarget> elements) {
		super(viewId, inflater, elements);
	}

	@Override
	protected void update(int position, TargetView view, CloudTarget item) {
		view.bind(item);
	}

	@Override
	protected TargetView createView(View view) {
		return new TargetView(view);
	}

}
