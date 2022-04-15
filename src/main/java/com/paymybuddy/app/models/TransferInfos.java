package com.paymybuddy.app.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
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
