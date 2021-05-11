package mo20170519.fundamentals.bank.mo20170519_bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.customer;
import mo20170519.fundamentals.bank.mo20170519_bank.service.customerService;

@RestController
@RequestMapping("/customer")
public class customerController {
	@Autowired
	private customerService service;

	@GetMapping("/details/{id}")
	public customer getCustomerById(@PathVariable int id) {
		return service.getCustomerById(id);
	}

	@GetMapping("/details/all")
	public List<customer> getAllCustomers() {
		return service.getAllCustomers();
	}

	@PostMapping("/create")
	public ResponseEntity<Object> createCustomer(@RequestBody customer customer) {
		return service.addCustomer(customer);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateCustomer(@RequestBody customer customer, @PathVariable("id") int id) {
		return service.updateCustomer(customer, id);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteCustomer(@PathVariable int id) {
		return service.deleteCustomer(id);
	}
}
