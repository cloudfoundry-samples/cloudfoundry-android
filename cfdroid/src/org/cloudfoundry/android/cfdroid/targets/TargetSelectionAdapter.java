package org.cloudfoundry.android.cfdroid.targets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;

import android.view.LayoutInflater;
import android.view.View;
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
public class TargetSelectionAdapter extends ItemListAdapter<CloudTarget, TargetSelectionView> {


	protected Set<Integer> selectedTargets = new HashSet<Integer>();

	public TargetSelectionAdapter(LayoutInflater inflater,
			List<CloudTarget> elements, TargetsActivity context) {
		super(R.layout.target_select_item, inflater, elements);
		this.context = context;
	}

	private TargetsActivity context;

	private ActionMode actionMode;

	public void init(List<CloudTarget> targets, Set<Integer> selection) {
		setItems(targets);
		this.selectedTargets = selection;
	}

	@Override
	protected TargetSelectionView createView(View view) {
		return new TargetSelectionView(view);
	}
	
	@Override
	protected void update(final int position, TargetSelectionView tsv,
			CloudTarget item) {
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
				toRemove.add(getItems().get(idx));
			}
			getItems().removeAll(toRemove);
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
