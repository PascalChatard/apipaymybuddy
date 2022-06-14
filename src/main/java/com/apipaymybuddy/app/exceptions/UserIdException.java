package com.apipaymybuddy.app.exceptions;

public class UserIdException extends RuntimeException {

	public UserIdException(int userId) {
		super(String.format("User with ID (%d) does not exist.", userId));
	}

}
