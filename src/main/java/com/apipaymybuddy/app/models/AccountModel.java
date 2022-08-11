package com.apipaymybuddy.app.models;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountModel {

	/**
	 * ID of account table.
	 */
	private Integer accountId;

	/**
	 * Open date of the account.
	 */
	private Date openDate;

	/**
	 * Account balance.
	 */
	private double solde;

	/**
	 * Owner of the account.
	 */
	UserModel accountOwner;

	/**
	 * List of associate user account.
	 */
	private List<UserModel> connections = new ArrayList<>();

	/**
	 * List of transfer operations.
	 */

	List<TransferModel> transfers = new ArrayList<>();

}
