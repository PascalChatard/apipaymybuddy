package com.paymybuddy.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.Rate;
import com.paymybuddy.app.models.TransferInfos;
import com.paymybuddy.app.models.User;
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
	public void returns400_WhenThereIsNoAccountWithTheGivenId() throws Exception {

		// GIVEN

		// WHEN
		doReturn(Optional.empty()).when(accountService).findById(ArgumentMatchers.anyInt());

		// THEN
		mockMvc.perform(get("/account/{id}", 2)).andExpect(status().isBadRequest());

	}

	@Test
	@Order(3)
	public void testGetAccount() throws Exception {

		// GIVEN
		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		account.setAccountId(1);
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
		// beneficiary user id and transfer amount
		TransferInfos transferInfos = TransferInfos.builder().beneficiaryUserId(1)
				.transferDescription("Libellé du transfert").transferAmout(13.55).build();

		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		account.setAccountId(2);
		account.setOpenDate(date);
		account.setSolde(150.85);

		User user = new User();
		user.setUserId(1);
		user.setFirstName("durand");
		user.setLastName("jean");

		// WHEN
		doReturn(Optional.of(account)).when(accountService).findById(ArgumentMatchers.anyInt());
		doReturn(Optional.of(user)).when(userService).findById(ArgumentMatchers.anyInt());

//		doReturn(account).when(accountService).makeTransfer(ArgumentMatchers.any(), ArgumentMatchers.any(),
//				ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble(), ArgumentMatchers.any());

		doReturn(account).when(accountService).makeTransfer(ArgumentMatchers.any(Account.class),
				ArgumentMatchers.any(User.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble(),
				ArgumentMatchers.any(Rate.class));

		// THEN
		mockMvc.perform(put("/account/{id}/transfer", 2).content(asJsonString(transferInfos))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	@Order(5)
	public void returns400_WhenThereIsNoAccountWithTheGivenId_testMakeTransfer() throws Exception {

		// GIVEN
		// beneficiary user id and transfer amount
		TransferInfos transferInfos = TransferInfos.builder().beneficiaryUserId(1)
				.transferDescription("Libellé du transfert").transferAmout(13.55).build();

		// WHEN
		doReturn(Optional.empty()).when(accountService).findById(ArgumentMatchers.anyInt());

		// THEN
		mockMvc.perform(put("/account/{id}/transfer", 2).content(asJsonString(transferInfos))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	@Test
	@Order(6)
	public void returns400_WhenThereIsNoUserWithTheGivenId_testMakeTransfer() throws Exception {

		// GIVEN
		// beneficiary user id and transfer amount
		TransferInfos transferInfos = TransferInfos.builder().beneficiaryUserId(1)
				.transferDescription("Libellé du transfert").transferAmout(13.55).build();

		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		account.setAccountId(2);
		account.setOpenDate(date);
		account.setSolde(150.85);

		// WHEN
		doReturn(Optional.of(account)).when(accountService).findById(ArgumentMatchers.anyInt());
		doReturn(Optional.empty()).when(userService).findById(ArgumentMatchers.anyInt());

		// THEN
		mockMvc.perform(put("/account/{id}/transfer", 2).content(asJsonString(transferInfos))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
