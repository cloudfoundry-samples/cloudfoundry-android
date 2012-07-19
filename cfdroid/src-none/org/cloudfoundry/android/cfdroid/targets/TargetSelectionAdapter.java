package org.cloudfoundry.android.cfdroid.targets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cloudfoundry.android.cfdroid.R;


import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * Used in the targets listing view. Maintains a list of selected targets and
 * manages the deletion of (multiple) items thru an ActionMode.
 * 
 * @author ebottard
 * 
 */
@EBean
public class TargetSelectionAdapter extends BaseAdapter {

	protected List<CloudTarget> targets = new ArrayList<CloudTarget>();

	protected Set<Integer> selectedTargets = new HashSet<Integer>();

	@RootContext
	TargetsActivity context;

	private ActionMode actionMode;

	public void init(List<CloudTarget> targets, Set<Integer> selection) {
		this.targets = targets;
		this.selectedTargets = selection;
	}

	@Override
	public int getCount() {
		return targets.size();
	}

	@Override
	public CloudTarget getItem(int position) {
		return targets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		TargetSelectionView tsv;
		if (convertView == null) {
			tsv = TargetSelectionView_.build(context);
		} else {
			tsv = (TargetSelectionView) convertView;
		}
		CloudTarget item = getItem(position);
		tsv.bind(item, selectedTargets.contains(position));
		tsv.getCheckBox().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					selectedTargets.add(position);
					if (selectedTargets.size() == 1) {
						actionMode = context
								.startActionMode(new SelectionActionModeCallback());
					}
				} else {
					selectedTargets.remove(position);
					if (selectedTargets.isEmpty()) {
						actionMode.finish();
						actionMode = null;
					}
				}
			}
		});

		return tsv;
	}

	// TODO : ActionMode state is lost upon conf change
	private final class SelectionActionModeCallback implements
			ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			new MenuInflater(context).inflate(R.menu.delete, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			Set<CloudTarget> toRemove = new HashSet<CloudTarget>();
			for (int idx : selectedTargets) {
				toRemove.add(targets.get(idx));
			}
			targets.removeAll(toRemove);
			selectedTargets.clear();
			TargetSelectionAdapter.this.notifyDataSetChanged();
			mode.finish();
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			selectedTargets.clear();
			TargetSelectionAdapter.this.notifyDataSetChanged();
		}
	}

}
