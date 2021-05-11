package mo20170519.fundamentals.bank.mo20170519_bank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="account")
public class account {
	@Id
	@Column(name="account_number")
	private int accountNo;
	@Column(name="account_type")
	private String accountType;
	@Column(name="balance")
	private int balance;
	@Column(name="customer_id")
	private int customerId;
	public account(int accountNo, String accountType, int balance, int customerId) {
		this.accountNo = accountNo;
		this.accountType = accountType;
		this.balance = balance;
		this.customerId = customerId;
	}
	public account() {
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
}
