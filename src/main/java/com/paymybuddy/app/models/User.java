package com.paymybuddy.app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer userId;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	private String address;

	private String city;

	private String phone;

	private String mail;

	private String password;

//	@OneToOne(mappedBy = "accountOwner", cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	@OneToOne(mappedBy = "accountOwner")
	Account accountUser;

}
