package org.cloudfoundry.android.cfdroid.support;

import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.android.cfdroid.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;

/**
 * List loading fragment for a specific type
 * 
 * @param <E>
 */
public abstract class ListLoadingFragment<E> extends RoboSherlockListFragment
		implements LoaderCallbacks<Result<List<E>>> {

	public static List ERROR = new ArrayList();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListShown(false);
		getLoaderManager().initLoader(loaderId(), null, this);
	}

	protected abstract int loaderId();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
		if (optionsMenu.findItem(R.id.refresh) == null) {
			// Prevent double addition of a refresh button
			// as this could be added by multiple fragments
			inflater.inflate(R.menu.refresh, optionsMenu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			refresh();
			// allow others who could have a refresh action
			// to do their work
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Refresh the fragment's list
	 */
	public void refresh() {
		if (getActivity() != null)
			getLoaderManager().restartLoader(loaderId(), null, this);
	}

	public void onLoadFinished(Loader<Result<List<E>>> loader,
			Result<List<E>> items) {
		if (items.isError()) {
			AlertDialog.Builder builder = new Builder(getActivity())
					.setTitle(R.string.error_title);

			builder.create().show();
		} else {
			setList(items.getValue());
			if (isResumed())
				setListShown(true);
			else
				setListShownNoAnimation(true);
		}
	}

	/**
	 * Allows you to update the list's items without using setListAdapter(),
	 * which makes the list jump back to the top.
	 */
	protected void setList(List<E> items) {
		@SuppressWarnings("unchecked")
		ItemListAdapter<E, ?> listAdapter = (ItemListAdapter<E, ?>) getListAdapter();
		if (listAdapter == null) {
			setListAdapter(adapterFor(items));
		} else {
			listAdapter.setItems(items);
		}
	}

	/**
	 * Create adapter for list of items
	 * 
	 * @param items
	 * @return list adapter
	 */
	protected abstract ItemListAdapter<E, ?> adapterFor(List<E> items);

	@Override
	public void onLoaderReset(Loader<Result<List<E>>> listLoader) {
	}

}
