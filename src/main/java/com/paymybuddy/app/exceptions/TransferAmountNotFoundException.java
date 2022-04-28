package com.paymybuddy.app.exceptions;

public class TransferAmountNotFoundException extends RuntimeException {

	public TransferAmountNotFoundException() {

		super("No transfer amount value, the amount cannot be null or equal to zero.");
	}

}
