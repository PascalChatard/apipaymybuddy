package com.paymybuddy.app.services;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.Rate;
import com.paymybuddy.app.models.Transfer;
import com.paymybuddy.app.models.User;

@Service
public class AccountService extends GenericService<Account> {


	public Account makeTransfer(Account debitedAccount, User beneficiaryUser, String descriptionTransfer,
			double amount, Rate rate) {

		if (amount < 0.0) {
			return null;
		} else {
			if (debitedAccount.getSolde() >= amount) {

				Transfer transfer = new Transfer();
				transfer.setDate(getDateTimeTransfer());
				transfer.setDescription(descriptionTransfer);
				transfer.setAmount(amount);
				transfer.setDebitedAccount(debitedAccount);
				transfer.setCreditedAccount(beneficiaryUser.getAccountUser());
				transfer.setRate(rate);
				transfer.setCost(roundToTowSignificantDigits(amount * rate.getValue() / 100));
				debitedAccount.getTransfers().add(transfer);
				debitedAccount.setSolde(roundToTowSignificantDigits(debitedAccount.getSolde() - amount));
			} else {
				return null;
			}
		}

		Account modifiedAccount = save(debitedAccount);

		return modifiedAccount;
	}

	private Timestamp getDateTimeTransfer() {

		return new Timestamp(System.currentTimeMillis());
	}

	private double roundToTowSignificantDigits(double value) {

		return (Math.round(value * 100.0) / 100.0);
	}

}
