package org.cloudfoundry.android.cfdroid.support;

import java.util.List;

import org.cloudfoundry.android.cfdroid.R;

import android.app.Activity;
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
public abstract class ListLoadingFragment<E> extends RoboSherlockListFragment implements LoaderCallbacks<List<E>> {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.refresh:
            refresh();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Refresh the fragment's list
     */
    public void refresh() {
        if (getActivity() != null)
            getLoaderManager().restartLoader(0, null, this);
    }

    public void onLoadFinished(Loader<List<E>> loader, List<E> items) {
        setList(items);

        if (isResumed())
            setListShown(true);
        else
            setListShownNoAnimation(true);
    }

    /**
     * Allows you to update the list's items without using setListAdapter(), which makes the list jump back to the top.
     */
    private void setList(List<E> items) {
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
    public void onLoaderReset(Loader<List<E>> listLoader) {
    }

}
