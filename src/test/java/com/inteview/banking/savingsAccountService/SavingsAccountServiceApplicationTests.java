package com.inteview.banking.savingsAccountService;

import com.inteview.banking.savingsAccountService.account.dto.SavingsAccountDTO;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementRequestDTO;
import com.inteview.banking.savingsAccountService.accountStatement.dto.PaginatedAccountStatementResponseDTO;
import com.inteview.banking.savingsAccountService.base.exceptions.SavingsAppException;
import com.inteview.banking.savingsAccountService.constants.FundTransferTypeEnum;
import com.inteview.banking.savingsAccountService.constants.TransactionTypeEnum;
import com.inteview.banking.savingsAccountService.fundTransfer.dto.FundTransferDTO;
import com.inteview.banking.savingsAccountService.fundTransfer.dto.FundTransferRequestDTO;
import com.inteview.banking.savingsAccountService.transaction.dto.TransactionRequestDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SavingsAccountServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
		locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SavingsAccountServiceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}


	private static String customerId = "55465fgfg76645";
	private static String accountId;
	private static String accountNumber;

	@Test
	public void contextLoads() {

	}
	private SavingsAccountDTO getAccount() {
		SavingsAccountDTO accountDTO = new SavingsAccountDTO()
				.setBranchId("TestBranch")
				.setCustomerId("55465fgfg76645")
				.setAccountNumber("111122223333");
		return accountDTO;
	}

	@Test
	@Order(1)
	public void testCreateSavingsAccount() throws SavingsAppException {
		SavingsAccountDTO accountDTO = getAccount();
		SavingsAccountDTO response = this.restTemplate.postForObject(getRootUrl() + "/savingsAccount/create",
				accountDTO, SavingsAccountDTO.class);
		accountId = response.getId();
		accountNumber = response.getAccountNumber();
		assertNotNull(response);
	}

	@Test
	@Order(2)
	public void testGetAccount() throws SavingsAppException {
		SavingsAccountDTO accountDTO = restTemplate.getForObject(getRootUrl() + "/savingsAccount/" + accountId, SavingsAccountDTO.class);
		assertNotNull(accountDTO);
	}

	@Test
	@Order(4)
	public void testPutAccountBalance() throws SavingsAppException {
		SavingsAccountDTO accountDTO = restTemplate.getForObject(getRootUrl() + "/savingsAccount/" + accountId, SavingsAccountDTO.class);
		accountDTO.setBalanceAmount(200d);
		restTemplate.put(getRootUrl() + "/savingsAccount/accountBalance/" + accountId, accountDTO);
		SavingsAccountDTO updatedAccount = restTemplate.getForObject(getRootUrl() + "/savingsAccount/" + accountId, SavingsAccountDTO.class);
		assertNotNull(updatedAccount);
	}

	@Test
	@Order(5)
	public void testTransaction() throws SavingsAppException {

		SavingsAccountDTO fromAccountDTO = restTemplate.getForObject(getRootUrl() + "/savingsAccount/" + accountId, SavingsAccountDTO.class);

		TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO()
				.setAmount(500)
				.setComments("test transaction")
				.setCustomerId(customerId)
				.setEmail("testEmail")
				.setTransactionType(TransactionTypeEnum.CREDIT);

		restTemplate.put(getRootUrl() + "/savingsAccount/transaction/" + accountId, transactionRequestDTO);

		assertNotNull(fromAccountDTO);
	}

	@Test
	@Order(6)
	public void testFundTransfer() throws SavingsAppException {

		SavingsAccountDTO fromAccountDTO = restTemplate.getForObject(getRootUrl() + "/savingsAccount/" + accountId, SavingsAccountDTO.class);

		SavingsAccountDTO toAccountDTO = getAccount();
		toAccountDTO.setCustomerId("newCustomer1");
		toAccountDTO.setAccountNumber("123122223333");
		toAccountDTO = this.restTemplate.postForObject(getRootUrl() + "/savingsAccount/create",
				toAccountDTO, SavingsAccountDTO.class);

		FundTransferRequestDTO fundTransferRequestDTO = new FundTransferRequestDTO()
				.setFundTransferType(FundTransferTypeEnum.NEFT)
				.setAmount(200)
				.setComments("test transfer")
				.setCustomerId(customerId)
				.setEmail("testEmail")
				.setFromAccount(fromAccountDTO.getId())
				.setToAccount(toAccountDTO.getId());

		FundTransferDTO fundTransferDTO = restTemplate.postForObject(getRootUrl() + "/savingsAccount/fundTransfer", fundTransferRequestDTO, FundTransferDTO.class);
		assertNotNull(fundTransferDTO);
	}

	@Test
	@Order(7)
	public void testGetAccountStatements() throws SavingsAppException {
		AccountStatementRequestDTO  accountStatementRequest = new AccountStatementRequestDTO()
				.setAccountId(accountId)
				.setPage(0)
				.setPageSize(10);
		PaginatedAccountStatementResponseDTO accountDTO = restTemplate.postForObject(getRootUrl() + "/savingsAccount/statements/" + accountId, accountStatementRequest, PaginatedAccountStatementResponseDTO.class);
		assertNotNull(accountDTO);
	}

	@Test
	@Order(8)
	public void testDeleteAccount() throws SavingsAppException {

		SavingsAccountDTO savingsAccountDTO = getAccount();
		savingsAccountDTO.setCustomerId("newCustomer");
		savingsAccountDTO.setAccountNumber("123127623333");
		savingsAccountDTO = this.restTemplate.postForObject(getRootUrl() + "/savingsAccount/create",
				savingsAccountDTO, SavingsAccountDTO.class);
		restTemplate.delete(getRootUrl() + "/savingsAccount/" + savingsAccountDTO.getId());
		try {
			savingsAccountDTO = restTemplate.getForObject(getRootUrl() + "/savingsAccount/" + savingsAccountDTO.getId(), SavingsAccountDTO.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
