package com.apipaymybuddy.app.exceptions;

public class AccountIdException extends RuntimeException {


	public AccountIdException(int accountId) {
		super(String.format("Account with ID (%d) does not exist.", accountId));
	}


}
