package mo20170519.fundamentals.bank.mo20170519_bank.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.customer;

public interface customerService {

	customer getCustomerById(int id);

	List<customer> getAllCustomers();

	ResponseEntity<Object> addCustomer(customer customer);

	ResponseEntity<Object> updateCustomer(customer customer, int id);

	ResponseEntity<Object> deleteCustomer(int id);

}
