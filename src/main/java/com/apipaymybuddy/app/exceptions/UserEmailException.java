package com.apipaymybuddy.app.exceptions;

public class UserEmailException extends RuntimeException {

	public UserEmailException(String email) {
		super(String.format("User with Email (%s) does not exist.", email));
	}

}
