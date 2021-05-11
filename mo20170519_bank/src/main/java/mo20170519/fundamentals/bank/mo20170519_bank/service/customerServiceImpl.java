package mo20170519.fundamentals.bank.mo20170519_bank.service;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;
import mo20170519.fundamentals.bank.mo20170519_bank.entity.customer;
import mo20170519.fundamentals.bank.mo20170519_bank.repository.accountRepository;
import mo20170519.fundamentals.bank.mo20170519_bank.repository.customerRepository;

@Service
public class customerServiceImpl implements customerService {
	@Autowired
	private customerRepository customerRepo;
	@Autowired
	private accountRepository accountRepo;

	@Transactional
	public ResponseEntity<Object> addCustomer(customer customer) {
		if(customerRepo.findById(customer.getCustomerId()).isPresent()) {
			throw new InvalidConfigurationPropertyValueException("id", customer.getCustomerId(), "Already exists");
		}
		customer newCustomer = new customer();
		newCustomer.setCustomerId(customer.getCustomerId());
		newCustomer.setName(customer.getName());
		newCustomer.setEmail(customer.getEmail());
		newCustomer.setContactNo(customer.getContactNo());
		List<account> accounts = new ArrayList<account>();
		if(customer.getAccounts()!=null) {
			accounts = customer.getAccounts();
		}
		Map<String, Integer> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		for(int i=0;i<accounts.size();i++) {
			if(map.containsKey(accounts.get(i).getAccountType())) {
				throw new InvalidConfigurationPropertyValueException("accountType", accounts.get(i).getAccountType(), "Two accounts with same account type can not exist for 1 customer");
			}else {
				map.put(accounts.get(i).getAccountType(), accounts.get(i).getAccountNo());
			}
		}
		newCustomer.setAccounts(customer.getAccounts());
		customer savedCustomer = customerRepo.save(newCustomer);
		if (customerRepo.findById(savedCustomer.getCustomerId()).isPresent()) {
			return ResponseEntity.accepted().body("Successfully created Customer and Accounts");
		} else
			return ResponseEntity.unprocessableEntity().body("Failed to create specified customer");
	}
	
	
	@Transactional
	public ResponseEntity<Object> updateCustomer(customer customer, int id) {
		customer savedCustomer=customerRepo.findById(id).orElseThrow(()-> new InvalidConfigurationPropertyValueException("id", id, "Customer with this id does not exist"));
		if(customer.getAccounts()!=null) {
			throw new InvalidConfigurationPropertyValueException("accounts", customer.getAccounts(), "Only Customer details can be updated, account details updation is not allowed from here");
		}
		if(customer.getCustomerId()!=id) {
			throw new InvalidConfigurationPropertyValueException("id", id, "Customer id different in path and request body");
		}
		savedCustomer.setName(customer.getName());
		savedCustomer.setEmail(customer.getEmail());
		savedCustomer.setContactNo(customer.getContactNo());
		if (customerRepo.findById(savedCustomer.getCustomerId()).isPresent()) {
			return ResponseEntity.accepted().body("Successfully updated Customer details");
		} else
			return ResponseEntity.unprocessableEntity().body("Failed to create specified customer");
	}

	public ResponseEntity<Object> deleteCustomer(int id) {
		if (customerRepo.findById(id).isPresent()) {
			customerRepo.deleteById(id);
			if (customerRepo.findById(id).isPresent()) {
				return ResponseEntity.unprocessableEntity().body("Failed to delete the specified record");
			} else
				return ResponseEntity.ok().body("Successfully deleted specified record");
		} else
			return ResponseEntity.unprocessableEntity().body("No Records Found");
	}

	public customer getCustomerById(int id) {
		if(customerRepo.findById(id).isPresent()) {
			return customerRepo.findById(id).get();
		}
		return null;
	}

	public List<customer> getAllCustomers() {
		return customerRepo.findAll();
	}
}
