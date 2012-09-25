package org.cloudfoundry.android.cfdroid.support;

import android.content.Context;

/**
 * Specialization of {@link AsyncLoader} that handles error wrapping.
 * 
 * @author Eric Bottard
 * 
 * @param <E> the loader result type in case of success
 */
public abstract class FailingAsyncLoader<E> extends AsyncLoader<Result<E>> {

	public FailingAsyncLoader(Context context) {
		super(context);
	}

	@Override
	public final Result<E> loadInBackground() {
		try {
			return Result.result(doLoadInBackground());
		} catch (Throwable t) {
			return Result.error(t);
		}
	}

	protected abstract E doLoadInBackground();

}
