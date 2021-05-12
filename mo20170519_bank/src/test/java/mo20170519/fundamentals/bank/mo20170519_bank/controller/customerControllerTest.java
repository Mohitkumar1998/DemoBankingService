package mo20170519.fundamentals.bank.mo20170519_bank.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;
import mo20170519.fundamentals.bank.mo20170519_bank.entity.customer;
import mo20170519.fundamentals.bank.mo20170519_bank.service.customerService;

@WebMvcTest(value = customerController.class)
class customerControllerTest {

	@MockBean
	customerService service;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetCustomerById() throws Exception {
		account newAccount1 = new account(1200, "savings", 1000, 1);
		account newAccount2 = new account(1201, "current", 1500, 1);
		List<account> accounts = new ArrayList<account>();
		accounts.add(newAccount1);
		accounts.add(newAccount2);
		customer newCustomer = new customer(1, "Mohit", "abc@def.ghi", "123456789", accounts);
		Mockito.when(service.getCustomerById(1)).thenReturn(newCustomer);
		MvcResult result = mockMvc.perform(get("/customer/details/1")).andReturn();
		String expected = "{\"customerId\":1,\"name\":\"Mohit\",\"email\":\"abc@def.ghi\",\"contactNo\":\"123456789\",\"accounts\":[{\"accountNo\":1200,\"accountType\":\"savings\",\"balance\":1000,\"customerId\":1},{\"accountNo\":1201,\"accountType\":\"current\",\"balance\":1500,\"customerId\":1}]}";
		assertEquals(expected, result.getResponse().getContentAsString());
	}

	@Test
	void testGetAllCustomers() throws Exception {
		account newAccount1 = new account(1200, "savings", 1000, 1);
		account newAccount2 = new account(1201, "current", 1500, 1);
		List<account> accounts = new ArrayList<account>();
		accounts.add(newAccount1);
		accounts.add(newAccount2);
		customer newCustomer = new customer(1, "Mohit", "abc@def.ghi", "123456789", accounts);
		List<customer> customers = new ArrayList<customer>();
		customers.add(newCustomer);
		Mockito.when(service.getAllCustomers()).thenReturn(customers);
		MvcResult result = mockMvc.perform(get("/customer/details/all")).andReturn();
		String expected = "[{\"customerId\":1,\"name\":\"Mohit\",\"email\":\"abc@def.ghi\",\"contactNo\":\"123456789\",\"accounts\":[{\"accountNo\":1200,\"accountType\":\"savings\",\"balance\":1000,\"customerId\":1},{\"accountNo\":1201,\"accountType\":\"current\",\"balance\":1500,\"customerId\":1}]}]";
		assertEquals(expected, result.getResponse().getContentAsString());
	}

	@Test
	void testCreateCustomer() throws Exception {
		Mockito.when(service.addCustomer(Mockito.any(customer.class)))
				.thenReturn(ResponseEntity.accepted().body("Successfully created Customer and Accounts"));
		MvcResult result = mockMvc.perform(post("/customer/create").content("{\n" + "    \"customerId\": \"1\",\n"
				+ "    \"name\": \"Mohit\",\n" + "    \"email\": \"abc@def.ghi\",\n"
				+ "    \"contactNo\": \"123456789\",\n" + "    \"accounts\": [\n" + "        {\n"
				+ "            \"accountNo\":\"1200\",\n" + "            \"accountType\":\"savings\",\n"
				+ "            \"balance\":\"1000\",\n" + "            \"customerId\":\"1\"\n" + "        },\n"
				+ "        {\n" + "            \"accountNo\":\"1201\",\n" + "            \"accountType\":\"current\",\n"
				+ "            \"balance\":\"1500\",\n" + "            \"customerId\":\"1\"\n" + "        }\n"
				+ "    ]\n" + "}").contentType(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());
		assertEquals("Successfully created Customer and Accounts", result.getResponse().getContentAsString());
	}

	@Test
	void testUpdateCustomer() throws Exception {
		mockMvc.perform(post("/customer/create").content("{\n" + "    \"customerId\": \"1\",\n"
				+ "    \"name\": \"Mohit\",\n" + "    \"email\": \"abc@def.ghi\",\n"
				+ "    \"contactNo\": \"123456789\",\n" + "    \"accounts\": [\n" + "        {\n"
				+ "            \"accountNo\":\"1200\",\n" + "            \"accountType\":\"savings\",\n"
				+ "            \"balance\":\"1000\",\n" + "            \"customerId\":\"1\"\n" + "        },\n"
				+ "        {\n" + "            \"accountNo\":\"1201\",\n" + "            \"accountType\":\"current\",\n"
				+ "            \"balance\":\"1500\",\n" + "            \"customerId\":\"1\"\n" + "        }\n"
				+ "    ]\n" + "}").contentType(MediaType.APPLICATION_JSON));
		Mockito.when(service.updateCustomer(Mockito.any(customer.class), Mockito.anyInt()))
				.thenReturn(ResponseEntity.accepted().body("Successfully updated Customer details"));
		MvcResult result = mockMvc
				.perform(
						put("/customer/update/1")
								.content("{\n" + "    \"customerId\": \"1\",\n" + "    \"name\": \"Mohit\",\n"
										+ "    \"email\": \"mohit@gmail.com\",\n"
										+ "    \"contactNo\": \"123456789\",\n" + "    \"accounts\": null\n" + "}")
								.contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());
		assertEquals("Successfully updated Customer details", result.getResponse().getContentAsString());
	}

	@Test
	void testDeleteCustomer() throws Exception {
		mockMvc.perform(post("/customer/create").content("{\n" + "    \"customerId\": \"1\",\n"
				+ "    \"name\": \"Mohit\",\n" + "    \"email\": \"abc@def.ghi\",\n"
				+ "    \"contactNo\": \"123456789\",\n" + "    \"accounts\": [\n" + "        {\n"
				+ "            \"accountNo\":\"1200\",\n" + "            \"accountType\":\"savings\",\n"
				+ "            \"balance\":\"1000\",\n" + "            \"customerId\":\"1\"\n" + "        },\n"
				+ "        {\n" + "            \"accountNo\":\"1201\",\n" + "            \"accountType\":\"current\",\n"
				+ "            \"balance\":\"1500\",\n" + "            \"customerId\":\"1\"\n" + "        }\n"
				+ "    ]\n" + "}").contentType(MediaType.APPLICATION_JSON));
		Mockito.when(service.deleteCustomer(Mockito.anyInt()))
				.thenReturn(ResponseEntity.ok().body("Successfully deleted specified record"));
		MvcResult result = mockMvc.perform(delete("/customer/delete/1")).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertEquals("Successfully deleted specified record", result.getResponse().getContentAsString());
	}

}
