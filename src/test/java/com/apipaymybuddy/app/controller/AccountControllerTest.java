package com.apipaymybuddy.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.apipaymybuddy.app.models.Account;
import com.apipaymybuddy.app.models.Rate;
import com.apipaymybuddy.app.models.TransferInfos;
import com.apipaymybuddy.app.models.User;
import com.apipaymybuddy.app.services.AccountService;
import com.apipaymybuddy.app.services.RateService;
import com.apipaymybuddy.app.services.UserService;


@ActiveProfiles("dev")
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
		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		account.setAccountId(2);
		account.setOpenDate(date);
		account.setSolde(150.85);

		User user = new User();
		user.setUserId(1);
		user.setFirstName("durand");
		user.setLastName("jean");

		Rate rate = new Rate();
		rate.setRateId(1);
		rate.setDescription("Tax de rémunération");
		rate.setValue(0.05);

		// WHEN
		doReturn(Optional.of(account)).when(accountService).findById(ArgumentMatchers.anyInt());
		doReturn(Optional.of(user)).when(userService).findById(ArgumentMatchers.anyInt());
		doReturn(Optional.of(rate)).when(rateService).findById(ArgumentMatchers.anyInt());

		doReturn(account).when(accountService).makeTransfer(ArgumentMatchers.any(Account.class),
				ArgumentMatchers.any(User.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble(),
				ArgumentMatchers.any(Rate.class));

		ResultActions perform = mockMvc.perform(
				post("/account/{id}", 2).param("beneficiaryUserId", "1")
						.param("transferDescription", "Libellé du transfert").param("transferAmout", "13.55")
						.flashAttr("transferInfos", new TransferInfos()));

		// THEN
		perform.andExpect(status().isCreated());

	}

	@Test
	@Order(5)
	public void returns400_WhenThereIsNoAccountWithTheGivenId_testMakeTransfer() throws Exception {

		// WHEN
		doReturn(Optional.empty()).when(accountService).findById(ArgumentMatchers.anyInt());

		ResultActions perform = mockMvc.perform(post("/account/{id}", 2).param("beneficiaryUserId", "1")
				.param("transferDescription", "Libellé du transfert").param("transferAmout", "13.55")
				.flashAttr("transferInfos", new TransferInfos()));

		// THEN
		perform.andExpect(status().isBadRequest());

	}

	@Test
	@Order(6)
	public void returns400_WhenThereIsNoUserWithTheGivenId_testMakeTransfer() throws Exception {

		// GIVEN
		Date date = Date.valueOf("2022-01-23");
		Account account = new Account();
		account.setAccountId(2);
		account.setOpenDate(date);
		account.setSolde(150.85);

		// WHEN
		doReturn(Optional.of(account)).when(accountService).findById(ArgumentMatchers.anyInt());
		doReturn(Optional.empty()).when(userService).findById(ArgumentMatchers.anyInt());

		ResultActions perform = mockMvc.perform(post("/account/{id}", 2).param("beneficiaryUserId", "1")
				.param("transferDescription", "Libellé du transfert").param("transferAmout", "13.55")
				.flashAttr("transferInfos", new TransferInfos()));

		// THEN
		perform.andExpect(status().isBadRequest());
	}

	@Test
	@Order(7)
	public void returns400_WhenThereIsNoDescriptionTransferGiven_testMakeTransfer() throws Exception {

		// GIVEN
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

		ResultActions perform = mockMvc
				.perform(post("/account/{id}", 2).param("beneficiaryUserId", "1").param("transferDescription", "")
						.param("transferAmout", "13.55").flashAttr("transferInfos", new TransferInfos()));

		// THEN
		perform.andExpect(status().isBadRequest());
	}

	@Test
	@Order(8)
	public void returns400_WhenThereIsNoGivenAmountTransfer_testMakeTransfer() throws Exception {

		// GIVEN
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

		ResultActions perform = mockMvc.perform(post("/account/{id}", 2).param("beneficiaryUserId", "1")
				.param("transferDescription", "Libellé du transfert").param("transferAmout", "0")
				.flashAttr("transferInfos", new TransferInfos()));

		// THEN
		perform.andExpect(status().isBadRequest());
	}

}
