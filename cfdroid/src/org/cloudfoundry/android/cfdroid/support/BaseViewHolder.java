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

/**
 * Base class for views that can populate themselves from an item of kind I.
 * 
 * @author ebottard
 * 
 * @param <I>
 *            the kind of item this view supports.
 */
public abstract class BaseViewHolder<I> {

	public abstract void bind(I item);

	public void bind(I item, int position) {
		bind(item);
	}

}
