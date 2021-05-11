package mo20170519.fundamentals.bank.mo20170519_bank.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="customer")
public class customer {
	@Id
	@Column(name="customer_id")
	private int customerId;
	@Column(name="name")
	private String name;
	@Column(name="email")
	private String email;
	@Column(name="contact_no")
	private String contactNo;
	@OneToMany(targetEntity = account.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_customer_id")
	private List<account> accounts;
	public customer(int customerId, String name, String email, String contactNo, List<account> accounts) {
		this.customerId = customerId;
		this.name = name;
		this.email = email;
		this.contactNo = contactNo;
		this.accounts = accounts;
	}
	public customer() {
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public List<account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<account> accounts) {
		this.accounts = accounts;
	}
}
