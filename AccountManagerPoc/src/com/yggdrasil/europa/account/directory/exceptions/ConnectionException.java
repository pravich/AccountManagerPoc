package com.yggdrasil.europa.account.directory.exceptions;

public class ConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 721998568178711984L;

	public ConnectionException() {
		super();
	}
	
	public ConnectionException(String meassage) {
		super(meassage);
	}
	
	public ConnectionException(String meassage, Throwable cause) {
		super(meassage, cause);
	}
}
