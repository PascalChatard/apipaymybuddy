package com.apipaymybuddy.app.exceptions;

public class AccountIdException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3517290402487304159L;

	public AccountIdException(int accountId) {
		super(String.format("Account with ID (%d) does not exist.", accountId));
	}


}
