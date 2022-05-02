package com.paymybuddy.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.paymybuddy.app.models.TransferInfos;

@ActiveProfiles("dev")
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


		mockMvc.perform(get("/account/{id}", 2)).andExpect(status().isOk()).andExpect(view().name("accountPage"))
				.andExpect(model().attributeExists("account"))
				.andExpect(model().attribute("account", hasProperty("accountId", is(2))))
				.andExpect(model().attribute("account", hasProperty("solde", is(25.50))));
	}

	@Test
	@Order(3)
	public void testMakeTransfer() throws Exception {

		// WHEN
		ResultActions perform = mockMvc.perform(post("/account/{id}", 2).param("beneficiaryUserId", "1")
				.param("transferDescription", "Libellé du transfert").param("transferAmout", "13.55")
				.flashAttr("transferInfos", new TransferInfos()));


		// THEN
		perform.andExpect(status().isFound());

	}

}
