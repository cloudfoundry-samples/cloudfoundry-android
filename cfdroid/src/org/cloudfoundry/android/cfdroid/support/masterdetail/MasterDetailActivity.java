package org.cloudfoundry.android.cfdroid.support.masterdetail;

import java.util.List;

import javax.annotation.Nullable;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

/**
 * Helper class for doing layout dependent master/detail activities. Subclasses
 * should set a content view that will either
 * <ul>
 * <li>have a single {@link FrameLayout} with id {@code fragment_container}</li>
 * <li>have a two-pane layout containing fragments with ids {@code left_pane}
 * and {@code right_pane}</li>
 * </ul>
 * 
 * 
 * @author ebottard
 * 
 * @param <I>
 *            the type of domain object this activity manages
 * @param <M>
 *            the actual type of the "master" fragment
 * @param <D>
 *            the actual type of the "detail" fragment
 */
public abstract class MasterDetailActivity<I, M extends Fragment, D extends Fragment & DetailPaneEventsCallback>
		extends RoboSherlockFragmentActivity implements
		MasterDetailEventsCallback<I> {

	@Nullable
	@InjectFragment(R.id.left_pane)
	M leftPane;

	@Nullable
	@InjectFragment(R.id.right_pane)
	D rightPane;

	@Nullable
	@InjectView(R.id.fragment_container)
	FrameLayout fragmentContainer;

	private List<I> data;

	private int position = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		data = (List<I>) getLastCustomNonConfigurationInstance();
		if (savedInstanceState != null) {
			position = savedInstanceState.getInt(
					MasterDetailEventsCallback.KEY_SELECTION, -1);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		// If not null, we're in the single pane layout
		if (fragmentContainer != null) {
			if (position != -1) {
				return;
			}

			Fragment initialFragment = makeLeftFragment();

			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, initialFragment, "left").commit();
		}
	}

	@Override
	public final Object onRetainCustomNonConfigurationInstance() {
		return data;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(MasterDetailEventsCallback.KEY_SELECTION, position);
	}

	@Override
	public void onLeftPaneSelection(int position) {
		this.position = position;
		if (fragmentContainer == null) {
			// two-pane layout, direct update
			rightPane.selectionChanged();
		} else {
			Fragment rightFragment = makeRightFragment();
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, rightFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}

	@Override
	public void onNewData(List<I> data) {
		this.data = data;
	}

	@Override
	public I getSelectedItem() {
		if (data != null && position != -1) {
			return data.get(position);
		} else {
			return null;
		}
	}

	@Override
	public int getSelectedPosition() {
		return position;
	}

	protected abstract M makeLeftFragment();

	protected abstract D makeRightFragment();
}
