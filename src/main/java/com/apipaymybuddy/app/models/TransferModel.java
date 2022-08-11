package com.apipaymybuddy.app.models;


import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferModel {

	/**
	 * ID of transfer table.
	 */
	private Integer transferId;

	/**
	 * Operation date.
	 */
	private Timestamp date;

	/**
	 * Description of transfer.
	 */
	private String description;

	/**
	 * Amount of transfer.
	 */
	private double amount;

	/**
	 * Cost of transfer.
	 */
	private double cost;

	/**
	 * Firstname and lastname transfer recipient.
	 */
	String transferRecipient;

}
