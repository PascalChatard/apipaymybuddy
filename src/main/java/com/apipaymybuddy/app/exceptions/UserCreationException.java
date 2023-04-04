package com.apipaymybuddy.app.exceptions;

public class UserCreationException  extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4937453244491645625L;

	public UserCreationException() {

		super("The user account creation could not be realized");
	}

}

