package com.apipaymybuddy.app.exceptions;

public class UserEmailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4963321896364375580L;

	public UserEmailException(String email) {
		super(String.format("User with Email (%s) does not exist.", email));
	}

}
