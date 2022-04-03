package com.paymybuddy.app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "rate")
public class Rate {

	/**
	 * ID of rate table.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer rateId;

	/**
	 * Value of pay rate.
	 */
	private double value;

	/**
	 * Description of pay rate.
	 */
	private String description;

}
