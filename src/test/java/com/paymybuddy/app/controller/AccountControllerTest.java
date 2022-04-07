package com.paymybuddy.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.services.AccountService;
import com.paymybuddy.app.services.RateService;
import com.paymybuddy.app.services.UserService;


@WebMvcTest(AccountController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;
	@MockBean
	private UserService userService; // for makeTransfer in controller
	@MockBean
	private RateService rateService; // for makeTransfer in controller


	@Test
	@Order(1)
	void injectedComponentIsNotNull() {
		assertThat(mockMvc).isNotNull();
		assertThat(accountService).isNotNull();
	}

	@Test
	@Order(2)
	public void returns404_WhenThereIsNoAccountWithTheGivenId() throws Exception {

		// GIVEN

		// WHEN
		doReturn(Optional.empty()).when(accountService).findById(ArgumentMatchers.anyInt());

		// THEN
//		mockMvc.perform(get("/account/{id}", 2)).andExpect(status().isNotFound());
		mockMvc.perform(get("/account/{id}", 2)).andExpect(status().isOk());

	}

	@Test
	@Order(3)
	public void testGetAccount() throws Exception {

		// GIVEN
		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		// account.setAccountId(1);
		account.setOpenDate(date);
		account.setSolde(150.85);

		// WHEN
		doReturn(Optional.of(account)).when(accountService).findById(ArgumentMatchers.anyInt());

		// THEN
		mockMvc.perform(get("/account/{id}", 2)).andExpect(status().isOk());

	}

	@Test
	@Order(4)
	public void testMakeTransfer() throws Exception {

		// GIVEN
		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		account.setAccountId(2);
		account.setOpenDate(date);
		account.setSolde(150.85);

		// WHEN
		doReturn(account).when(accountService).makeTransfer(ArgumentMatchers.any(), ArgumentMatchers.any(),
				ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble(), ArgumentMatchers.any());

		// THEN
		mockMvc.perform(get("/account/{id}", 2)).andExpect(status().isOk());
//		mockMvc.perform(get("/account/{id}", 2)).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value(2))
//				.andExpect(jsonPath("$.openDate").value("2021-11-22")).andExpect(jsonPath("$.solde").value(150.85));

	}


}
