package mo20170519.fundamentals.bank.mo20170519_bank.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;

public interface accountService {

	account getAccountById(int id);

	List<account> getAllAccounts();

	ResponseEntity<Object> transferFunds(int payorAccountNo, int payeeAccountNo, int amount);

	ResponseEntity<Object> addAccount(account account);

	ResponseEntity<Object> updateAccount(account account, int id);

	ResponseEntity<Object> deleteAccount(int id);
	
}
