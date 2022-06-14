package com.apipaymybuddy.app.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

	/**
	 * ID of user table.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer userId;

	/**
	 * FirstName of user.
	 */
	@Column(name = "firstname")
	private String firstName;

	/**
	 * Lastname of user.
	 */
	@Column(name = "lastname")
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

	/**
	 * Password of user.
	 */
	private String password;

	/**
	 * Account of user.
	 */
	@JsonBackReference // avoid "Could not write JSON: Infinite recursion (StackOverflowError)->
						// exception JsonMappingException"
	@OneToOne(mappedBy = "accountOwner", cascade = { CascadeType.PERSIST }, orphanRemoval = true)
	private Account accountUser;

}
