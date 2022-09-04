package com.apipaymybuddy.app.exceptions;

public class UserIdException extends RuntimeException {

	public UserIdException(int id) {
		super(String.format("User with Id (%d) does not exist.", id));
	}

}
