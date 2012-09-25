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
