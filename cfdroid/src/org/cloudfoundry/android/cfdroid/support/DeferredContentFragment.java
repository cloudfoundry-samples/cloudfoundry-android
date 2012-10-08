/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloudfoundry.android.cfdroid.support;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.InjectView;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Base class for fragments that rely on data being available asynchronously and
 * should show a "Loading..." screen until then.
 * 
 * @author Eric Bottard
 * 
 */
public abstract class DeferredContentFragment<T> extends RoboSherlockFragment  implements LoaderCallbacks<T>{
	
	@InjectView(R.id.progressContainer)
	private View progressContainer;
	
	@InjectView(R.id.actual_content)
	private View actualContent;
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	getLoaderManager().initLoader(R.id.deferred_content_fragment_loader, null, this);
    }

	@Override
	public void onLoadFinished(Loader<T> loader, T data) {
		onDataAvailable(data);
		progressContainer.setVisibility(View.GONE);
		actualContent.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoaderReset(Loader<T> arg0) {
		progressContainer.setVisibility(View.VISIBLE);
		actualContent.setVisibility(View.GONE);
	}
	
	public void onDataAvailable(T data) {
		
	}

}
