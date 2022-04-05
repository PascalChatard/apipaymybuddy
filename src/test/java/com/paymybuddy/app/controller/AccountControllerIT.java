package com.paymybuddy.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("data.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@Order(1)
	void injectedComponentIsNotNull() {
		assertThat(mockMvc).isNotNull();
	}

	@Order(2)
	@Test
	public void testGetAccount() throws Exception {
		mockMvc.perform(get("/account/{id}", 2)).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value(2))
				.andExpect(jsonPath("$.openDate").value("2021-11-22")).andExpect(jsonPath("$.solde").value(25.50))
				// verify account owner info
				.andExpect(jsonPath("$.accountOwner.userId").value(2))
				.andExpect(jsonPath("$.accountOwner.firstName").value("dupont"))
				.andExpect(jsonPath("$.accountOwner.lastName").value("louis"))
				.andExpect(jsonPath("$.accountOwner.address").value("15 rue du rouget"))
				.andExpect(jsonPath("$.accountOwner.city").value("aix-en-provence"))
				.andExpect(jsonPath("$.accountOwner.phone").value("0745235889"))
				.andExpect(jsonPath("$.accountOwner.mail").value("dupontlouis@hotmail.fr"))
				.andExpect(jsonPath("$.accountOwner.password").value("louis2022"))
				// verify account connections info
				.andExpect(jsonPath("$.connections.[0].userId").value(3))
				.andExpect(jsonPath("$.connections.[0].lastName").value("alain"))
				.andExpect(jsonPath("$.connections.[0].firstName").value("Lejeune"))
				.andExpect(jsonPath("$.connections.[0].address").value("32 avenue léon bloum"))
				.andExpect(jsonPath("$.connections.[0].city").value("pertuis"))
				.andExpect(jsonPath("$.connections.[0].phone").value("0490255633"))
				.andExpect(jsonPath("$.connections.[0].mail").value("alejeune@outlook.com"))
				.andExpect(jsonPath("$.connections.[0].password").value("alain2022"))
				// verify account transfers info
				.andExpect(jsonPath("$.transfers.[0].transferId").value(1))
				.andExpect(jsonPath("$.transfers.[0].date").value("2022-01-01T23:00:00.000+00:00"))
				.andExpect(jsonPath("$.transfers.[0].description").value("Remboursement ciné"))
				.andExpect(jsonPath("$.transfers.[0].amount").value(15.05))
				.andExpect(jsonPath("$.transfers.[0].cost").value(0.08))
				.andExpect(jsonPath("$.transfers.[0].rate.rateId").value(1))
				.andExpect(jsonPath("$.transfers.[0].rate.value").value(0.5))
				.andExpect(jsonPath("$.transfers.[0].rate.description").value("Taux rémunération standard"))
				.andExpect(jsonPath("$.transfers.[1].transferId").value(2))
				.andExpect(jsonPath("$.transfers.[1].date").value("2022-01-01T23:00:00.000+00:00"))
				.andExpect(jsonPath("$.transfers.[1].description").value("Participation cadeau"))
				.andExpect(jsonPath("$.transfers.[1].amount").value(5.50))
				.andExpect(jsonPath("$.transfers.[1].cost").value(0.03))
				.andExpect(jsonPath("$.transfers.[1].rate.rateId").value(1))
				.andExpect(jsonPath("$.transfers.[1].rate.value").value(0.5))
				.andExpect(jsonPath("$.transfers.[1].rate.description").value("Taux rémunération standard"));

	}

}
