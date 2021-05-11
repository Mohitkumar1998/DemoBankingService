package mo20170519.fundamentals.bank.mo20170519_bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;

@Repository
public interface accountRepository extends JpaRepository<account, Integer> {

	Optional<account> findByCustomerIdAndAccountTypeIgnoreCase(int customerId, String accountType);

	List<account> findByCustomerId(int customerId);
}
