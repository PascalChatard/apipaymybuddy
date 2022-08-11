package com.apipaymybuddy.app.models;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transfer")
public class Transfer {

	/**
	 * ID of transfer table.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer transferId;

	/**
	 * Operation date.
	 */
	private Timestamp date;

	/**
	 * Description of transfer.
	 */
	@NonNull
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
	 * Credited Account by the transfer.
	 */
	// @JsonIgnore // avoid "Could not write JSON: Infinite recursion
	// (StackOverflowError)"
	@JsonBackReference
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "credited_account_id")
	Account creditedAccount;

	/**
	 * Debited Account by the transfer
	 */
	// @JsonIgnore // avoid "Could not write JSON: Infinite recursion
	// (StackOverflowError)"
	@JsonBackReference
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "debited_account_id")
	Account debitedAccount;

	/**
	 * Pay rate applied.
	 */
	@JsonBackReference
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rate_id")
	Rate rate;

}
