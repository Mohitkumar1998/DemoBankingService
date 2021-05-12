package mo20170519.fundamentals.bank.mo20170519_bank.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import mo20170519.fundamentals.bank.mo20170519_bank.entity.account;
import mo20170519.fundamentals.bank.mo20170519_bank.entity.customer;
import mo20170519.fundamentals.bank.mo20170519_bank.service.accountService;

@WebMvcTest(value = accountController.class)
class accountControllerTest {

	@MockBean
	accountService service;

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	void testGetAccountById() throws Exception {
		account newAccount1 = new account(1200, "savings", 1000, 1);
		Mockito.when(service.getAccountById(1200)).thenReturn(newAccount1);
		MvcResult result = mockMvc.perform(get("/account/details/1200")).andReturn();
		String expected = "{\n" + "    \"accountNo\": 1200,\n" + "    \"accountType\": \"savings\",\n"
				+ "    \"balance\": 1000,\n" + "    \"customerId\": 1\n" + "}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	void testGetAllAccounts() throws Exception {
		account newAccount1 = new account(1200, "savings", 1000, 1);
		account newAccount2 = new account(1201, "current", 1500, 1);
		List<account> accounts = new ArrayList<account>();
		accounts.add(newAccount1);
		accounts.add(newAccount2);
		Mockito.when(service.getAllAccounts()).thenReturn(accounts);
		MvcResult result = mockMvc.perform(get("/account/details/all")).andReturn();
		String expected = "[\n" + "    {\n" + "        \"accountNo\": 1200,\n"
				+ "        \"accountType\": \"savings\",\n" + "        \"balance\": 1000,\n"
				+ "        \"customerId\": 1\n" + "    },\n" + "    {\n" + "        \"accountNo\": 1201,\n"
				+ "        \"accountType\": \"current\",\n" + "        \"balance\": 1500,\n"
				+ "        \"customerId\": 1\n" + "    }\n" + "]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}

	@Test
	void testTransferFunds() throws Exception {
		mockMvc.perform(post("/account/create")
				.content("{\n" + "            \"accountNo\":\"1200\",\n" + "            \"accountType\":\"savings\",\n"
						+ "            \"balance\":\"1000\",\n" + "            \"customerId\":\"1\"\n" + "        }")
				.contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(post("/account/create")
				.content("{\n" + "            \"accountNo\":\"1201\",\n" + "            \"accountType\":\"current\",\n"
						+ "            \"balance\":\"1500\",\n" + "            \"customerId\":\"1\"\n" + "        }")
				.contentType(MediaType.APPLICATION_JSON));
		Mockito.when(service.transferFunds(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
		.thenReturn(ResponseEntity.ok().body("Fund transfer successfull"));
		MvcResult result = mockMvc.perform(get("/account/fundTransfer?payorAccountNo=1201&payeeAccountNo=1200&amount=1000")).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertEquals("Fund transfer successfull", result.getResponse().getContentAsString());
	}

	@Test
	void testCreateAccount() throws Exception {
		Mockito.when(service.addAccount(Mockito.any(account.class)))
				.thenReturn(ResponseEntity.accepted().body("Successfully created Account"));
		MvcResult result = mockMvc.perform(post("/account/create")
				.content("{\n" + "    \"accountNo\": \"1202\",\n" + "    \"accountType\": \"salary\",\n"
						+ "    \"balance\": \"5000\",\n" + "    \"customerId\": \"1\"\n" + "}")
				.contentType(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());
		assertEquals("Successfully created Account", result.getResponse().getContentAsString());
	}

	@Test
	void testUpdateAccount() throws Exception {
		mockMvc.perform(post("/account/create")
				.content("{\n" + "    \"accountNo\": \"1202\",\n" + "    \"accountType\": \"salary\",\n"
						+ "    \"balance\": \"5000\",\n" + "    \"customerId\": \"1\"\n" + "}")
				.contentType(MediaType.APPLICATION_JSON));
		Mockito.when(service.updateAccount(Mockito.any(account.class), Mockito.anyInt()))
				.thenReturn(ResponseEntity.accepted().body("Account successfully updated"));
		MvcResult result = mockMvc.perform(put("/account/update/1202")
				.content("{\n" + "    \"accountNo\": \"1202\",\n" + "    \"accountType\": \"salary\",\n"
						+ "    \"balance\": \"10000\",\n" + "    \"customerId\": \"1\"\n" + "}")
				.contentType(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());
		assertEquals("Account successfully updated", result.getResponse().getContentAsString());
	}

	@Test
	void testDeleteAccount() throws Exception {
		mockMvc.perform(post("/account/create")
				.content("{\n" + "    \"accountNo\": \"1202\",\n" + "    \"accountType\": \"salary\",\n"
						+ "    \"balance\": \"5000\",\n" + "    \"customerId\": \"1\"\n" + "}")
				.contentType(MediaType.APPLICATION_JSON));
		Mockito.when(service.deleteAccount(Mockito.anyInt()))
				.thenReturn(ResponseEntity.ok().body("Successfully deleted specified record"));
		MvcResult result = mockMvc.perform(delete("/account/delete/1202")).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertEquals("Successfully deleted specified record", result.getResponse().getContentAsString());
	}

}
