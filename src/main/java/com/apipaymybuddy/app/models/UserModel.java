package com.apipaymybuddy.app.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {

	/**
	 * ID of user table.
	 */

	private Integer userId;

	/**
	 * FirstName of user.
	 */
	private String firstName;

	/**
	 * Lastname of user.
	 */
	private String lastName;

	/**
	 * Address of user.
	 */
	private String address;

	/**
	 * City of user.
	 */
	private String city;

	/**
	 * Phone of user.
	 */
	private String phone;

	/**
	 * E-mail of user.
	 */
	private String mail;


//	/**
//	 * Account of user.
//	 */
//	private Account accountUser;

}
