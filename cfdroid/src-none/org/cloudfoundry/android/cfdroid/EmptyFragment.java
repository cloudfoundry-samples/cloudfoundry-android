package org.cloudfoundry.android.cfdroid;

import com.googlecode.androidannotations.annotations.EFragment;

import android.support.v4.app.Fragment;

@EFragment
public class EmptyFragment extends Fragment implements HasTitle {

	@Override
	public CharSequence getTitle() {
		return "F" + System.identityHashCode(this);
	}

}
