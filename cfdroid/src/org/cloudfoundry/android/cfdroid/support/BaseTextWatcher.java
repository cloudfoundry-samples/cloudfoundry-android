package org.cloudfoundry.android.cfdroid.support;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * No-op implementation of {@link TextWatcher}.
 * @author ebottard
 *
 */
public class BaseTextWatcher implements TextWatcher {

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
