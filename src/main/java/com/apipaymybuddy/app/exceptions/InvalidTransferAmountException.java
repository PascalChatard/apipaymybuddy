package com.apipaymybuddy.app.exceptions;

public class InvalidTransferAmountException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6413207548179237662L;


	public InvalidTransferAmountException() {

		super("Invaid value, the amount cannot be negative or equal to zero.");
	}

	public InvalidTransferAmountException(double balance, double amount) {

		super(String.format("The account balance is insufficient, balance (%f)/ amount (%f).", balance, amount));
	}

}
