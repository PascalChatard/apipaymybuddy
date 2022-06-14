package com.apipaymybuddy.app.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferInfos {

	/**
	 * Beneficiary user Id.
	 */
	private int beneficiaryUserId;

	/**
	 * Transfer description.
	 */
	private String transferDescription;

	/**
	 * Transfer amount.
	 */
	private double transferAmout;

}
