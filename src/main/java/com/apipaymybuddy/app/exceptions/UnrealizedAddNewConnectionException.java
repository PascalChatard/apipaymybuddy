package com.apipaymybuddy.app.exceptions;

public class UnrealizedAddNewConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6260730733642932386L;

	public UnrealizedAddNewConnectionException(int userId, int accountId) {

		super(String.format("The add new connection with userId (%d) to account (%d) could not be realized", userId,
				accountId));
	}

}
