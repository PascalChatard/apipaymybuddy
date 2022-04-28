package com.paymybuddy.app.exceptions;

public class TransferDescriptionNotFoundException extends RuntimeException {

	public TransferDescriptionNotFoundException() {
		super("You must give a transfer description");
	}

}
