package com.apipaymybuddy.app.exceptions;

public class UserIdException extends RuntimeException {



	/**
	 * 
	 */
	private static final long serialVersionUID = 7181550607312797579L;

	public UserIdException(int id) {
		super(String.format("User with Id (%d) does not exist.", id));
	}

}
