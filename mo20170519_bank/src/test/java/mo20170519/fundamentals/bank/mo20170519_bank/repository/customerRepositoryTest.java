package mo20170519.fundamentals.bank.mo20170519_bank.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;
import mo20170519.fundamentals.bank.mo20170519_bank.entity.customer;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class customerRepositoryTest {

	@Autowired
	private customerRepository repo;

	static account newAccount1;
	static account newAccount2;
	static account newAccount3;
	static List<account> accounts;
	static customer customer1;
	static customer customer2;
	static List<customer> customers;

	@BeforeAll
	static void setVariables() {
		newAccount1 = new account(1200, "savings", 1000, 1);
		newAccount2 = new account(1201, "current", 1500, 1);
		accounts = new ArrayList<account>();
		accounts.add(newAccount1);
		customer1 = new customer(1, "Mohit", "abc@a", "123456789", accounts);
		accounts.add(newAccount2);
		newAccount3 = new account(1202, "salary", 500, 2);
		accounts = new ArrayList<account>();
		customer2 = new customer(2, "Rohit", "def@b", "987654321", accounts);
		customers = new ArrayList<customer>();
		customers.add(customer1);
		customers.add(customer2);
	}
	
	@Test
	void testEmptyRecords() {
		assertEquals(0, repo.findAll().size());
	}

	@Test
	void testSave() {
		repo.save(new customer(3, "Ankit", "ghi@c", "123987465", null));
		assertEquals(1, repo.findAll().size());
		assertEquals(3, repo.findAll().get(0).getCustomerId());
		assertEquals("Ankit", repo.findAll().get(0).getName());
		assertEquals("ghi@c", repo.findAll().get(0).getEmail());
		assertEquals("123987465", repo.findAll().get(0).getContactNo());
		assertEquals(null, repo.findAll().get(0).getAccounts());
	}
	
	@Test
	void testFindAll() {
		repo.save(customer1);
		repo.save(customer2);
		assertEquals(customers.size(), repo.findAll().size());
		assertEquals(1, repo.findAll().get(0).getCustomerId());
		assertEquals(2, repo.findAll().get(1).getCustomerId());
	}

	@Test
	void testFindById() {
		repo.save(customer1);
		repo.save(customer2);
		assertEquals(1, repo.findById(1).get().getCustomerId());
		assertEquals(customer1.getEmail(), repo.findById(1).get().getEmail());
		assertEquals(customer1.getName(), repo.findById(1).get().getName());
		assertEquals(customer1.getContactNo(), repo.findById(1).get().getContactNo());
		assertEquals(customer1.getAccounts().size(), repo.findById(1).get().getAccounts().size());
	}
	
	@Test
	void testFindByName() {
		repo.save(customer1);
		repo.save(customer2);
		assertEquals(1, repo.findByName("Mohit").get().getCustomerId());
		assertEquals(customer1.getEmail(), repo.findByName("Mohit").get().getEmail());
		assertEquals(customer1.getName(), repo.findByName("Mohit").get().getName());
		assertEquals(customer1.getContactNo(), repo.findByName("Mohit").get().getContactNo());
		assertEquals(customer1.getAccounts().size(), repo.findByName("Mohit").get().getAccounts().size());
	}

	@Test
	void testDeleteById() {
		repo.save(customer1);
		repo.save(customer2);
		assertEquals(2, repo.findAll().size());
		repo.deleteById(1);
		assertEquals(1, repo.findAll().size());
	}

	@Test
	void testDeleteAll() {
		repo.save(customer1);
		repo.save(customer2);
		assertEquals(2, repo.findAll().size());
		repo.deleteAll();
		assertEquals(0, repo.findAll().size());
	}

	@Test
	void testUpdate() {
		repo.save(customer1);
		assertEquals(1, repo.findById(1).get().getCustomerId());
		assertEquals(customer1.getEmail(), repo.findById(1).get().getEmail());
		assertEquals(customer1.getName(), repo.findById(1).get().getName());
		assertEquals(customer1.getContactNo(), repo.findById(1).get().getContactNo());
		assertEquals(customer1.getAccounts().size(), repo.findById(1).get().getAccounts().size());
		customer1.setEmail("change@d");
		repo.save(customer1);
		assertEquals(1, repo.findById(1).get().getCustomerId());
		assertEquals(customer1.getEmail(), repo.findById(1).get().getEmail());
		assertEquals(customer1.getName(), repo.findById(1).get().getName());
		assertEquals(customer1.getContactNo(), repo.findById(1).get().getContactNo());
		assertEquals(customer1.getAccounts().size(), repo.findById(1).get().getAccounts().size());
	}
}
