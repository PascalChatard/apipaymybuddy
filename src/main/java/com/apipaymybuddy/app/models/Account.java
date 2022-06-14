package com.apipaymybuddy.app.models;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {

	/**
	 * ID of account table.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer accountId;

	/**
	 * Open date of the account.
	 */
	@Column(name = "opendate")
	private Date openDate;

	/**
	 * Account balance.
	 */
	private double solde;

	/**
	 * Owner of the account.
	 */
	@JsonManagedReference // avoid "Could not write JSON: Infinite recursion (StackOverflowError)->
							// exception JsonMappingException"
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "user_id")
	User accountOwner;

	/**
	 * List of associate user account.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_account", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	@Fetch(value = FetchMode.SUBSELECT)
	private List<User> connections = new ArrayList<>();

	/**
	 * List of transfer operations.
	 */
	@OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "debited_account_id")
	List<Transfer> transfers = new ArrayList<>();

}
