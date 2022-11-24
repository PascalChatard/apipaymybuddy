package com.apipaymybuddy.app.exceptions;

public class UserCreationException  extends RuntimeException {
	
	public UserCreationException() {

		super("The user account creation could not be realized");
	}

}

