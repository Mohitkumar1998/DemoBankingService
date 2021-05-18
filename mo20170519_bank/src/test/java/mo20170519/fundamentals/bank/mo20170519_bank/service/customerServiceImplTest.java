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
import mo20170519.fundamentals.bank.mo20170519_bank.repository.customerRepository;

@ExtendWith(MockitoExtension.class)
class customerServiceImplTest {

	@Mock
	private customerRepository repo;

	@InjectMocks
	private customerServiceImpl service;

	static account newAccount1;
	static account newAccount2;
	static List<account> accounts;
	static customer newCustomer;
	static List<customer> allCustomer;

	@BeforeAll
	static void setVariables() {
		newAccount1 = new account(1200, "savings", 1000, 1);
		newAccount2 = new account(1201, "current", 1500, 1);
		accounts = new ArrayList<account>();
		accounts.add(newAccount1);
		accounts.add(newAccount2);
		newCustomer = new customer(1, "Mohit", "abc@def.ghi", "123456789", accounts);
		allCustomer = new ArrayList<customer>();
		allCustomer.add(newCustomer);
	}

	@Test
	void testAddCustomer() {
		Mockito.when(repo.findById(1)).thenReturn(Optional.empty()).thenReturn(Optional.of(newCustomer));
		Mockito.when(repo.save(Mockito.any(customer.class))).thenReturn(newCustomer);
		ResponseEntity<Object> expected = ResponseEntity.accepted().body("Successfully created Customer and Accounts");
		ResponseEntity<Object> actual = service.addCustomer(newCustomer);
		assertEquals(expected, actual);
		verify(repo, times(1)).save(Mockito.any(customer.class));
	}

	@Test
	void testUpdateCustomer() {
		Mockito.when(repo.findById(Mockito.anyInt())).thenReturn(Optional.of(newCustomer));
		Mockito.when(repo.save(Mockito.any(customer.class))).thenReturn(newCustomer);
		ResponseEntity<Object> expected = ResponseEntity.accepted().body("Successfully updated Customer details");
		ResponseEntity<Object> actual = service.updateCustomer(new customer(1, "Mohit", "change@a", "12222333", null), 1);
		assertEquals(expected, actual);
		verify(repo, times(1)).save(Mockito.any(customer.class));
	}

	@Test
	void testDeleteCustomer() {
		Mockito.when(repo.findById(Mockito.anyInt())).thenReturn(Optional.of(newCustomer)).thenReturn(Optional.empty());
		ResponseEntity<Object> expected=ResponseEntity.ok().body("Successfully deleted specified record");
		ResponseEntity<Object> actual = service.deleteCustomer(1);
		assertEquals(expected, actual);
		verify(repo, times(1)).deleteById(1);
	}

	@Test
	void testGetCustomerById() {
		Mockito.when(repo.findById(1)).thenReturn(Optional.of(newCustomer));
		customer actualResponse = service.getCustomerById(1);
		assertEquals(actualResponse, newCustomer);
		verify(repo, times(2)).findById(1);
	}

	@Test
	void testGetAllCustomers() {
		Mockito.when(repo.findAll()).thenReturn(allCustomer);
		List<customer> actualResponse = service.getAllCustomers();
		assertEquals(actualResponse, allCustomer);
		verify(repo, times(1)).findAll();
	}

}
