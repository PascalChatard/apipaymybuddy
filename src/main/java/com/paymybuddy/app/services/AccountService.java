package com.paymybuddy.app.services;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.Rate;
import com.paymybuddy.app.models.Transfer;
import com.paymybuddy.app.models.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService extends GenericService<Account> {


	public Account makeTransfer(Account debitedAccount, User beneficiaryUser, String descriptionTransfer,
			double amount, Rate rate) {

		log.debug("Debut methode makeTransfer, arg: Account {}, User {}, description {}, montant {}, taux {}",
				debitedAccount, beneficiaryUser, descriptionTransfer, amount, rate);
		log.info("Réalisation d'un transfert ({}), montant ({})", descriptionTransfer, amount);

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
				// Debit the amount of the user's account
				debitedAccount.setSolde(roundToTowSignificantDigits(debitedAccount.getSolde() - amount));
				log.debug("Nouveau solde compte débité ({})", debitedAccount.getSolde());

				// Credit the Beneficiary Account
				beneficiaryUser.getAccountUser()
						.setSolde(roundToTowSignificantDigits(beneficiaryUser.getAccountUser().getSolde() + amount));
				log.debug("Nouveau solde compte créditer ({})", beneficiaryUser.getAccountUser().getSolde());

			} else {
				return null;
			}
		}

		Account modifiedDebitedAccount = save(debitedAccount);
		log.info("Mise à jour compte débité suite au transfert ({}), montant ({})", descriptionTransfer, amount);

		Account modifiedCreditedAccount = save(beneficiaryUser.getAccountUser());
		log.info("Mise à jour compte crédité suite au transfert ({}), montant ({})", descriptionTransfer, amount);

		log.debug("Fin methode makeTransfer");
		return modifiedDebitedAccount;
	}

	private Timestamp getDateTimeTransfer() {
		log.trace("Exécute methode getDateTimeTransfer");
		return new Timestamp(System.currentTimeMillis());
	}

	private double roundToTowSignificantDigits(double value) {

		log.trace("Exécute methode roundToTowSignificantDigits");
		return (Math.round(value * 100.0) / 100.0);
	}

}
