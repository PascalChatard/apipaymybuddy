package com.paymybuddy.app.models;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "transfer")
public class Transfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer transferId;

	private Timestamp date;

	private String description;

	private double amount;

//	@OneToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "credited_account_id")
//	Account creditedAccount;
//
//	@OneToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "debited_account_id")
//	Account debitedAccount;

}
