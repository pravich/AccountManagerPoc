package com.yggdrasil.europa.account.database.exceptions;

public class DatabaseConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1600259798063744534L;
	
	public DatabaseConnectionException() {
		super();
	}
	
	public DatabaseConnectionException(String meassage) {
		super(meassage);
	}
	
	public DatabaseConnectionException(String meassage, Throwable cause) {
		super(meassage, cause);
	}
}
