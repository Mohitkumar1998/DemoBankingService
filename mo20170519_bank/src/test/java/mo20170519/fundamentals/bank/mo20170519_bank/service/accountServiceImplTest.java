package mo20170519.fundamentals.bank.mo20170519_bank.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;
import mo20170519.fundamentals.bank.mo20170519_bank.entity.customer;
import mo20170519.fundamentals.bank.mo20170519_bank.repository.accountRepository;
import mo20170519.fundamentals.bank.mo20170519_bank.repository.customerRepository;

@ExtendWith(MockitoExtension.class)
class accountServiceImplTest {

	@Mock
	private accountRepository repo;

	@Mock
	private customerRepository customerRepo;

	@InjectMocks
	private accountServiceImpl service;

	static account newAccount1;
	static account newAccount2;
	static List<account> accounts;
	static Optional<account> account1;
	static Optional<account> account2;
	static customer newCustomer;

	@BeforeAll
	static void setVariables() {
		newAccount1 = new account(1200, "savings", 1000, 1);
		newAccount2 = new account(1201, "current", 1500, 1);
		accounts = new ArrayList<account>();
		newCustomer = new customer(1, "Mohit", "abc@def.ghi", "123456789", accounts);
		accounts.add(newAccount1);
		accounts.add(newAccount2);
		account1 = Optional.of(newAccount1);
		account2 = Optional.of(newAccount2);
	}

	@Test
	void testGetAccountById() throws Exception {
		Mockito.when(repo.findById(1200)).thenReturn(account1);
		account actualResponse = service.getAccountById(1200);
		assertEquals(actualResponse, newAccount1);
		verify(repo, times(2)).findById(1200);
	}

	@Test
	void testGetAllAccounts() {
		Mockito.when(repo.findAll()).thenReturn(accounts);
		List<account> actualResponse = service.getAllAccounts();
		assertEquals(actualResponse, accounts);
		verify(repo, times(1)).findAll();
	}

	@Test
	void testTransferFunds() {
		Mockito.when(repo.findById(1200)).thenReturn(account1);
		Mockito.when(repo.findById(1201)).thenReturn(account2);
		Mockito.when(repo.findByCustomerId(Mockito.anyInt())).thenReturn(accounts);
		Mockito.when(customerRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(newCustomer));
		ResponseEntity<Object> expected=ResponseEntity.ok().body("Fund transfer successfull");
		ResponseEntity<Object> actual = service.transferFunds(1200, 1201, 500);
		assertEquals(expected, actual);
		assertEquals(account1.get().getBalance(), 500);
		assertEquals(account2.get().getBalance(), 2000);
	}

	@Test
	void testAddAccount() {
		Mockito.when(customerRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(newCustomer));
		Mockito.when(customerRepo.save(Mockito.any())).thenReturn(newCustomer);
		Mockito.when(repo.save(Mockito.any(account.class))).thenReturn(newAccount1);
		Mockito.when(repo.findByCustomerIdAndAccountTypeIgnoreCase(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		ResponseEntity<Object> expected = ResponseEntity.unprocessableEntity()
				.body("Failed to create specified account");
		ResponseEntity<Object> actual = service.addAccount(newAccount1);
		assertEquals(expected, actual);
		verify(repo, times(1)).save(newAccount1);
	}

	@Test
	void testUpdateAccount() {
		Mockito.when(customerRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(newCustomer));
		Mockito.when(customerRepo.save(Mockito.any())).thenReturn(newCustomer);
		Mockito.when(repo.save(Mockito.any(account.class))).thenReturn(newAccount1);
		Mockito.when(repo.findById(Mockito.anyInt())).thenReturn(account1);
		ResponseEntity<Object> expected = ResponseEntity.accepted().body("Account successfully updated");
		ResponseEntity<Object> actual = service.updateAccount(newAccount1, 1200);
		assertEquals(expected, actual);
		verify(repo, times(1)).save(newAccount1);
	}

	@Test
	void testDeleteAccount() {
		Mockito.when(repo.findById(Mockito.anyInt())).thenReturn(account1);
		Mockito.when(repo.findByCustomerId(Mockito.anyInt())).thenReturn(new ArrayList<account>());
		Mockito.when(customerRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(newCustomer));
		Mockito.when(customerRepo.save(Mockito.any())).thenReturn(newCustomer);
		ResponseEntity<Object> expected=ResponseEntity.unprocessableEntity().body("Failed to delete the specified record");
		ResponseEntity<Object> actual = service.deleteAccount(1200);
		assertEquals(expected , actual);
		verify(repo, times(1)).deleteById(1200);
	}

}
