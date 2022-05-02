package com.paymybuddy.app.exceptions;

public class InvalidTransferAmountException extends RuntimeException {

	public InvalidTransferAmountException() {

		super("Invaid value, the amount cannot be negative or equal to zero.");
	}

	public InvalidTransferAmountException(double balance, double amount) {

		super(String.format("The account balance is insufficient, balance (%f)/ amount (%f).", balance, amount));
	}

}
