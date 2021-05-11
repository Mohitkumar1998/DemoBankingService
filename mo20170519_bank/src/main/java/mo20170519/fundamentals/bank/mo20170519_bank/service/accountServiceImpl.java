package mo20170519.fundamentals.bank.mo20170519_bank.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;
import mo20170519.fundamentals.bank.mo20170519_bank.entity.customer;
import mo20170519.fundamentals.bank.mo20170519_bank.repository.accountRepository;
import mo20170519.fundamentals.bank.mo20170519_bank.repository.customerRepository;

@Service
public class accountServiceImpl implements accountService {
	@Autowired
	private customerRepository customerRepo;
	@Autowired
	private accountRepository accountRepo;

	public account getAccountById(int id) {
		if (accountRepo.findById(id).isPresent()) {
			return accountRepo.findById(id).get();
		}
		return null;
	}

	public List<account> getAllAccounts() {
		return accountRepo.findAll();
	}

	public ResponseEntity<Object> addAccount(account account) {
		if (accountRepo.findById(account.getAccountNo()).isPresent()) {
			throw new InvalidConfigurationPropertyValueException("account no", account.getAccountNo(),
					"Already exists");
		}
		if (!customerRepo.findById(account.getCustomerId()).isPresent()) {
			throw new InvalidConfigurationPropertyValueException("customer id", account.getCustomerId(),
					"customer with this id does not exist, thus account can not be created");
		}
		if (accountRepo.findByCustomerIdAndAccountTypeIgnoreCase(account.getCustomerId(), account.getAccountType())
				.isPresent()) {
			throw new InvalidConfigurationPropertyValueException("account type", account.getAccountType(),
					"account of type " + account.getAccountType() + " already exists for customer with customer id "
							+ account.getCustomerId() + ", duplicate account type not allowed");
		}
		accountRepo.save(account);
		customer thisCustomer = customerRepo.findById(account.getCustomerId()).get();
		List<account> accounts = thisCustomer.getAccounts();
		accounts.add(account);
		customerRepo.save(thisCustomer);
		if (accountRepo.findById(account.getAccountNo()).isPresent()) {
			return ResponseEntity.accepted().body("Successfully created Account");
		} else
			return ResponseEntity.unprocessableEntity().body("Failed to create specified account");
	}

	public ResponseEntity<Object> updateAccount(account account, int id) {
		if (!accountRepo.findById(id).isPresent()) {
			return ResponseEntity.unprocessableEntity().body("No Records Found");
		}
		account thisAccount = accountRepo.findById(id).get();
		if (id != account.getAccountNo() || thisAccount.getCustomerId() != account.getCustomerId()
				|| !(thisAccount.getAccountType().equals(account.getAccountType()))) {
			throw new InvalidConfigurationPropertyValueException("account no", account.getAccountNo(),
					"Account no, account type and customer id are non-editable");
		}
		thisAccount.setBalance(account.getBalance());
		accountRepo.save(thisAccount);
		customer thisCustomer = customerRepo.findById(account.getCustomerId()).get();
		List<account> accounts = thisCustomer.getAccounts();
		accounts.removeAll(accounts);
		accounts.addAll(accountRepo.findByCustomerId(account.getCustomerId()));
		customerRepo.save(thisCustomer);
		if (accountRepo.findById(account.getAccountNo()).isPresent()) {
			return ResponseEntity.accepted().body("Account successfully updated");
		} else
			return ResponseEntity.unprocessableEntity().body("Failed to create specified account");

	}

	public ResponseEntity<Object> deleteAccount(int id) {
		if (!accountRepo.findById(id).isPresent()) {
			return ResponseEntity.unprocessableEntity().body("No Records Found");
		}
		int customerId = accountRepo.findById(id).get().getCustomerId();
		customer thisCustomer = customerRepo.findById(customerId).get();
		List<account> accounts = thisCustomer.getAccounts();
		accounts.removeAll(accounts);
		accountRepo.deleteById(id);
		accounts.addAll(accountRepo.findByCustomerId(customerId));
		customerRepo.save(thisCustomer);
		if (customerRepo.findById(id).isPresent()) {
			return ResponseEntity.unprocessableEntity().body("Failed to delete the specified record");
		} else
			return ResponseEntity.ok().body("Successfully deleted specified record");
	}

	public ResponseEntity<Object> transferFunds(int payorAccountNo, int payeeAccountNo, int amount) {
		if(!accountRepo.findById(payorAccountNo).isPresent()) {
			return ResponseEntity.unprocessableEntity().body("Invalid Payor Account No");
		}
		if(!accountRepo.findById(payeeAccountNo).isPresent()) {
			return ResponseEntity.unprocessableEntity().body("Invalid Payee Account No");
		}
		if(payeeAccountNo==payorAccountNo) {
			return ResponseEntity.unprocessableEntity().body("Payor and payee can not be the same");
		}
		account payor = accountRepo.findById(payorAccountNo).get();
		account payee = accountRepo.findById(payeeAccountNo).get();
		if(payor.getBalance()<amount) {
			return ResponseEntity.unprocessableEntity().body("Amount is more than balance");
		}
		payee.setBalance(payee.getBalance()+amount);
		payor.setBalance(payor.getBalance()-amount);
		accountRepo.save(payor);
		accountRepo.save(payee);
		List<account> accountsPayor = customerRepo.findById(payor.getCustomerId()).get().getAccounts();
		accountsPayor.removeAll(accountsPayor);
		accountsPayor.addAll(accountRepo.findByCustomerId(payor.getCustomerId()));
		List<account> accountsPayee = customerRepo.findById(payee.getCustomerId()).get().getAccounts();
		accountsPayee.removeAll(accountsPayee);
		accountsPayee.addAll(accountRepo.findByCustomerId(payee.getCustomerId()));
		return ResponseEntity.ok().body("Fund transfer successfull");
	}
}
