package com.apipaymybuddy.app.exceptions;

public class UnrealizedTransferException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6348730200837840912L;

	public UnrealizedTransferException(String description, double amout) {

		super(String.format("The transfer (%s) of amount (%f) could not be realized", description, amout));
	}

}
