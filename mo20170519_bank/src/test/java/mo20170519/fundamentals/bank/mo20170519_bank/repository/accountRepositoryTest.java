package mo20170519.fundamentals.bank.mo20170519_bank.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class accountRepositoryTest {

	@Autowired
	private accountRepository repo;

	static account newAccount1;
	static account newAccount2;
	static List<account> accounts;
	static Optional<account> account1;
	static Optional<account> account2;

	@BeforeAll
	static void setVariables() {
		newAccount1 = new account(1200, "savings", 1000, 1);
		newAccount2 = new account(1201, "current", 1500, 1);
		accounts = new ArrayList<account>();
		accounts.add(newAccount1);
		accounts.add(newAccount2);
		account1 = Optional.of(newAccount1);
		account2 = Optional.of(newAccount2);
	}

	@Test
	void testEmptyRecords() {
		assertEquals(0, repo.findAll().size());
	}

	@Test
	void testSave() {
		repo.save(new account(1202, "salary", 2000, 1));
		assertEquals(1, repo.findAll().size());
		assertEquals(1202, repo.findAll().get(0).getAccountNo());
		assertEquals(2000, repo.findAll().get(0).getBalance());
		assertEquals(1, repo.findAll().get(0).getCustomerId());
	}

	@Test
	void testFindAll() {
		repo.save(newAccount1);
		repo.save(newAccount2);
		assertEquals(accounts.size(), repo.findAll().size());
		assertEquals(accounts.get(0).getAccountNo(), repo.findAll().get(0).getAccountNo());
		assertEquals(accounts.get(1).getAccountType(), repo.findAll().get(1).getAccountType());
	}
	
	@Test
	void testFindById() {
		repo.save(newAccount1);
		assertEquals(1200, repo.findById(1200).get().getAccountNo());
		assertEquals(1000, repo.findById(1200).get().getBalance());
		assertEquals(1, repo.findById(1200).get().getCustomerId());
	}

	@Test
	void testFindByCustomerId() {
		repo.save(newAccount1);
		repo.save(newAccount2);
		assertEquals(account1.get().getAccountNo(), repo.findById(1200).get().getAccountNo());
	}
	
	@Test
	void testDeleteById() {
		repo.save(newAccount1);
		repo.save(newAccount2);
		assertEquals(2, repo.findAll().size());
		repo.deleteById(1201);
		assertEquals(1, repo.findAll().size());
	}

	@Test
	void testDeleteAll() {
		repo.save(newAccount1);
		repo.save(newAccount2);
		assertEquals(2, repo.findAll().size());
		repo.deleteAll();
		assertEquals(0, repo.findAll().size());
	}
	
	@Test
	void testUpdate() {
		repo.save(newAccount1);
		assertEquals(1200, repo.findById(1200).get().getAccountNo());
		assertEquals(1000, repo.findById(1200).get().getBalance());
		assertEquals(1, repo.findById(1200).get().getCustomerId());
		newAccount1.setBalance(1500);
		repo.save(newAccount1);
		assertEquals(1500, repo.findById(1200).get().getBalance());
	}
}
