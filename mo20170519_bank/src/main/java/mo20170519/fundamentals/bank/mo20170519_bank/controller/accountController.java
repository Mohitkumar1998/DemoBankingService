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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;
import mo20170519.fundamentals.bank.mo20170519_bank.service.accountService;

@RestController
@RequestMapping("/account")
public class accountController {
	
	@Autowired
	private accountService service;

	@GetMapping("/details/{id}")
	public account getAccountById(@PathVariable int id) {
		return service.getAccountById(id);
	}

	@GetMapping("/details/all")
	public List<account> getAllAccounts() {
		return service.getAllAccounts();
	}
	
	@GetMapping("/fundTransfer")
	public ResponseEntity<Object> transferFunds(@RequestParam() int payorAccountNo, @RequestParam() int payeeAccountNo, @RequestParam() int amount) {
		return service.transferFunds(payorAccountNo, payeeAccountNo, amount);
	}

	@PostMapping("/create")
	public ResponseEntity<Object> createAccount(@RequestBody account account) {
		return service.addAccount(account);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateAccount(@RequestBody account account, @PathVariable("id") int id) {
		return service.updateAccount(account, id);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteAccount(@PathVariable int id) {
		return service.deleteAccount(id);
	}
}