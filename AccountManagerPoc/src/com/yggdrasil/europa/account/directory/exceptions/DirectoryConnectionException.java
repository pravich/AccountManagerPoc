package com.yggdrasil.europa.account.directory.exceptions;

public class DirectoryConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 721998568178711984L;

	public DirectoryConnectionException() {
		super();
	}
	
	public DirectoryConnectionException(String meassage) {
		super(meassage);
	}
	
	public DirectoryConnectionException(String meassage, Throwable cause) {
		super(meassage, cause);
	}
}
