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

public class Result<T> {

	private T value;

	private Throwable error;

	private Result() {

	}

	public static <T> Result<T> error(Throwable t) {
		Result<T> result = new Result<T>();
		result.error = t;
		return result;
	}

	public static <T> Result<T> result(T value) {
		Result<T> result = new Result<T>();
		result.value = value;
		return result;
	}

	public boolean isError() {
		return error != null;
	}

	public Throwable getError() {
		return error;
	}

	public boolean isSuccess() {
		// value would be rightfully null
		return error == null;
	}

	public T getValue() {
		return value;
	}

}
