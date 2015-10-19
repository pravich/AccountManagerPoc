package com.yggdrasil.europa.account.directory.exceptions;

public class InvalidCredentialException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971216399726315975L;
	
	public InvalidCredentialException(String message) {
		super(message);
	}
	
	public InvalidCredentialException(String message, Throwable cause) {
		super(message, cause);
	}
}
