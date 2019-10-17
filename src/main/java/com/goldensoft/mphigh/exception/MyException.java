package com.goldensoft.mphigh.exception;

import java.util.Arrays;

public class MyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6285474531280198364L;

	
	public MyException(String msg) {
		super(msg);
	}
	
	public MyException(Throwable throwable) {
		super(throwable);
	}

	@Override
	public String toString() {
		return "MyException [getMessage()=" + getMessage() + ", getLocalizedMessage()=" + getLocalizedMessage()
				+ ", getCause()=" + getCause() + ", toString()=" + super.toString() + ", fillInStackTrace()="
				+ fillInStackTrace() + ", getStackTrace()=" + Arrays.toString(getStackTrace())
				+ ", getSuppressed()="
				+ Arrays.toString(getSuppressed()) + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
	
	
	
}
