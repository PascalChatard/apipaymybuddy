package com.paymybuddy.app.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.paymybuddy.app.exceptions.InvalidTransferAmountException;
import com.paymybuddy.app.models.Account;
import com.paymybuddy.app.models.Rate;
import com.paymybuddy.app.models.Transfer;
import com.paymybuddy.app.models.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AccountService extends GenericService<Account> {

	/**
	 * makeTransfer - Make a money transfer from account to beneficiary user
	 * 
	 * @param debitedAccount      The account object to be debited
	 * @param beneficiaryUser     The beneficiary user object of the transfer
	 * @param descriptionTransfer Transfer description
	 * @param amount              Amount of transfer
	 * @param rate                Transfer rate object of pay
	 * @return The Account object full filled and updated
	 */
	public Account makeTransfer(Account debitedAccount, User beneficiaryUser, String descriptionTransfer, double amount,
			Rate rate) throws IOException {

		log.debug("Debut methode makeTransfer, arg: Account ({}), User ({}), description ({}), montant ({}), taux ({})",
				debitedAccount, beneficiaryUser, descriptionTransfer, amount, rate);
		log.info("Réalisation d'un transfert ({}), montant ({})", descriptionTransfer, amount);

		if (debitedAccount.getSolde() < amount) {
			// the account balance is insufficient
			log.error("The account balance is insufficient, balance ({})/ amount ({}).", debitedAccount.getSolde(),
					amount);

			throw new InvalidTransferAmountException(debitedAccount.getSolde(), amount);
		}

		// check there is a valid transfer amount
		if (amount <= 0) {

			log.error("Invaid value, the amount cannot be negative or equal to zero ({}).", amount);
			throw new InvalidTransferAmountException();
		}

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


		Account modifiedDebitedAccount = save(debitedAccount);
		log.info("Mise à jour compte débité suite au transfert ({}), montant ({})",
				modifiedDebitedAccount.getAccountId(), descriptionTransfer, amount);

		Account modifiedCreditedAccount = save(beneficiaryUser.getAccountUser());
		log.info("Mise à jour compte crédité ({}) suite au transfert ({}), montant ({})",
				modifiedCreditedAccount.getAccountId(), descriptionTransfer, amount);

		log.debug("Fin methode makeTransfer");
		return modifiedDebitedAccount;
	}

	/**
	 * getAvailableUserForConnection - return all users who are not account owner
	 * and who are not in connections
	 * 
	 * @param allUsers all user in BDD
	 * @param account  the account on which you want to add a connection
	 * @return The User list available for add new connection
	 */
	public List<User> getAvailableUserForConnection(Iterable<User> allUsers, Account account) {

		List<User> connections = account.getConnections();
		// extracts the owning user of all user
		List<User> availableUsers = StreamSupport.stream(allUsers.spliterator(), false)
				.filter(user -> user.getUserId() != account.getAccountOwner().getUserId()).collect(Collectors.toList());

		// indicates users already in the connection
		List<User> presentUsers = new ArrayList<User>();

		// searches for Users that are in connections
		for (User user : availableUsers) {
			for (User connection : connections) {
				log.debug("Verify user available / user in connection -> ({})/({})", user.getFirstName(),
						connection.getFirstName());
				if (connection.getMail().equals(user.getMail())) {

					presentUsers.add(user);
					log.debug("The user is present in connections ({})", user.getFirstName());
					break;
				}
			}
		}

		// extracts users already in the connection
		presentUsers.forEach(user -> availableUsers.remove(user));
		return availableUsers;
	}

	/**
	 * getDateTimeTransfer - Get the current Timestamp in SQL format
	 * 
	 * @return A Timestamp object
	 */
	private Timestamp getDateTimeTransfer() {
		log.debug("Debut methode getDateTimeTransfer, sans arg.");

		Timestamp time = new Timestamp(System.currentTimeMillis());

		log.debug("Fin methode getDateTimeTransfer");
		return time;
	}

	/**
	 * roundToTowSignificantDigits - Get a double type value rounded to 2
	 * significant digits
	 * 
	 * @param value the value to round
	 * @return The rounded value
	 */
	private double roundToTowSignificantDigits(double value) {

		log.debug("Debut methode roundToTowSignificantDigits, arg: value ({})", value);

		double roundedValue = (Math.round(value * 100.0) / 100.0);

		log.debug("Fin methode roundToTowSignificantDigits");
		return roundedValue;
	}

}
