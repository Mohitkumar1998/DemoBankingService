package mo20170519.fundamentals.bank.mo20170519_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.customer;

@Repository
public interface customerRepository extends JpaRepository<customer, Integer> {

}
