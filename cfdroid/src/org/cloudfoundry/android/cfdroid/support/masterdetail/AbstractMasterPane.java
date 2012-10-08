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
package org.cloudfoundry.android.cfdroid.support.masterdetail;

import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;

import android.view.View;
import android.widget.ListView;

/**
 * Helper class for a master fragment that retrieves data through a loader.
 * 
 * @param <I>
 *            the kind of domain object this fragment deals with
 * @author Eric Bottard
 */
public abstract class AbstractMasterPane<I> extends ListLoadingFragment<I> {

	protected MasterDetailEventsCallback communicationChannel() {
		return (MasterDetailEventsCallback) getActivity();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		communicationChannel().onLeftPaneSelection(position);
	}

}
