package com.apipaymybuddy.app.exceptions;

public class TransferDescriptionNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6954919430950990763L;

	public TransferDescriptionNotFoundException() {
		super("You must give a transfer description");
	}

}
