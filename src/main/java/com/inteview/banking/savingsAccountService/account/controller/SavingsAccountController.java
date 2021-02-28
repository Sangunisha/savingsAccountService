package com.inteview.banking.savingsAccountService.account.controller;

import com.inteview.banking.savingsAccountService.account.dto.SavingsAccountDTO;
import com.inteview.banking.savingsAccountService.account.service.SavingsAccountService;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementRequestDTO;
import com.inteview.banking.savingsAccountService.accountStatement.dto.PaginatedAccountStatementResponseDTO;
import com.inteview.banking.savingsAccountService.base.exceptions.SavingsAppException;
import com.inteview.banking.savingsAccountService.fundTransfer.dto.FundTransferDTO;
import com.inteview.banking.savingsAccountService.fundTransfer.dto.FundTransferRequestDTO;
import com.inteview.banking.savingsAccountService.transaction.dto.TransactionRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that manages savings account actions
 */
@RestController
@RequestMapping("/savingsAccount")
public class SavingsAccountController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SavingsAccountService savingsAccountService;

    /**
     * Gets all savings accounts of the customer
     *
     * @param customerId
     * @return
     */
    @GetMapping("/all/{customerId}")
    public List<SavingsAccountDTO> getAccounts(@PathVariable("customerId") String customerId) {

        return savingsAccountService.getCustomerAccounts(customerId);
    }

    /**
     *
     * @param accountId
     * @return
     */
    @GetMapping("/{accountId}")
    public SavingsAccountDTO getAccountDetails(@PathVariable("accountId") String accountId) {
        SavingsAccountDTO accountDTO = savingsAccountService.get(accountId);
        if (null != accountDTO && !savingsAccountService.isAuthorized(accountDTO.getCustomerId())) {
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.not_permitted, "You are not permitted to do this action");
        }
        return accountDTO;
    }

    /**
     * Creates new savings account
     * @param account
     * @return
     */
    @PostMapping("/create")
    public SavingsAccountDTO createAccount(@RequestBody SavingsAccountDTO account) {
        return savingsAccountService.createAccount(account);
    }

    /**
     * Updates account balance
     *
     * @param accountId
     * @param accountDTO
     * @return
     */
    @PutMapping("/accountBalance/{accountId}")
    public SavingsAccountDTO updateAccountBalance(@PathVariable("accountId") String accountId, @RequestBody SavingsAccountDTO accountDTO) {

        return savingsAccountService.updateAccountBalance(accountDTO);
    }

    /**
     * Deletes the account
     *
     * @param accountId
     * @return
     */
    @DeleteMapping("/{accountId}")
    public Boolean deleteAccount(@PathVariable("accountId") String accountId) {
        return savingsAccountService.deleteAccount(accountId);
    }

    /**
     * Does transaction (credit/debit)
     * @param accountId
     * @param transactionRequestDTO
     * @return
     */
    @PutMapping("/transaction/{accountId}")
    public SavingsAccountDTO doTransaction(@PathVariable("accountId") String accountId, @RequestBody TransactionRequestDTO transactionRequestDTO) {

        return savingsAccountService.doTransaction(accountId, transactionRequestDTO);
    }

    /**
     * Does fund transfer
     * @param fundTransferRequestDTO
     * @return
     */
    @PostMapping("/fundTransfer")
    public FundTransferDTO doFundTransfer(@RequestBody FundTransferRequestDTO fundTransferRequestDTO) {
        return savingsAccountService.transferFund(fundTransferRequestDTO);
    }

    /**
     * Gets paginated account statements
     * @param accountId
     * @param requestDTO
     * @return
     */
    @PostMapping("/statements/{accountId}")
    public PaginatedAccountStatementResponseDTO getAccountStatements(@PathVariable("accountId") String accountId, @RequestBody AccountStatementRequestDTO requestDTO) {
        return savingsAccountService.getAccountStatements(requestDTO);
    }
}
