package com.paymybuddy.app.exceptions;

public class UnrealizedAddNewConnectionException extends RuntimeException {

	public UnrealizedAddNewConnectionException(int userId, int accountId) {

		super(String.format("The add new connection with userId (%d) to account (%d) could not be realized", userId,
				accountId));
	}

}
