package com.inteview.banking.savingsAccountService.account.service;

import com.inteview.banking.savingsAccountService.account.domain.SavingsAccount;
import com.inteview.banking.savingsAccountService.account.dto.SavingsAccountDTO;
import com.inteview.banking.savingsAccountService.account.repo.SavingsAccountRepo;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementDTO;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementRequestDTO;
import com.inteview.banking.savingsAccountService.accountStatement.dto.PaginatedAccountStatementResponseDTO;
import com.inteview.banking.savingsAccountService.accountStatement.service.AccountStatementService;
import com.inteview.banking.savingsAccountService.base.exceptions.SavingsAppException;
import com.inteview.banking.savingsAccountService.base.service.AbstractService;
import com.inteview.banking.savingsAccountService.constants.TransactionModeEnum;
import com.inteview.banking.savingsAccountService.constants.TransactionStatusEnum;
import com.inteview.banking.savingsAccountService.constants.TransactionTypeEnum;
import com.inteview.banking.savingsAccountService.constants.UserRoleEnum;
import com.inteview.banking.savingsAccountService.fundTransfer.dto.FundTransferDTO;
import com.inteview.banking.savingsAccountService.fundTransfer.dto.FundTransferRequestDTO;
import com.inteview.banking.savingsAccountService.fundTransfer.service.FundTransferService;
import com.inteview.banking.savingsAccountService.transaction.dto.TransactionDTO;
import com.inteview.banking.savingsAccountService.transaction.dto.TransactionRequestDTO;
import com.inteview.banking.savingsAccountService.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to manage savings account transactions
 */
@Service
public class SavingsAccountService extends AbstractService<SavingsAccount, SavingsAccountDTO> {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static final Double MINIMUM_BALANCE = 2000d;

    @Autowired
    private SavingsAccountRepo savingsAccountRepo;

    @Autowired
    private AccountStatementService accountStatementService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private FundTransferService fundTransferService;

    @Override
    public CrudRepository<SavingsAccount, String> getRepository() {
        return savingsAccountRepo;
    }

    @Override
    public SavingsAccountDTO getDTO(SavingsAccount entity, SavingsAccountDTO dto) throws SavingsAppException {
        dto.setAccountNumber(entity.getAccountNumber())
                .setBalanceAmount(entity.getBalanceAmount())
                .setBranchId(entity.getBranchId())
                .setCustomerId(entity.getCustomerId());
        return copyDTO(entity, dto);
    }

    @Override
    public SavingsAccount populateEntity(SavingsAccountDTO dto, SavingsAccount entity) throws SavingsAppException {
        return entity.setAccountNumber(dto.getAccountNumber())
                .setBalanceAmount(dto.getBalanceAmount())
                .setBranchId(dto.getBranchId())
                .setCustomerId(dto.getCustomerId())
                .setId(dto.getId());
    }

    @Override
    public SavingsAccount updateModifiedParams(SavingsAccountDTO dto, SavingsAccount existingEntity) throws SavingsAppException {
        return existingEntity
                .setBalanceAmount(dto.getBalanceAmount());
    }

    @Override
    public SavingsAccountDTO getNewDTO() throws SavingsAppException {
        return new SavingsAccountDTO();
    }

    @Override
    public SavingsAccount getNewEntity() throws SavingsAppException {
        return new SavingsAccount();
    }


    public Boolean isAuthorized(String customerId) {
       /* String loggedInCustomerId = "4028abfa77df99680177df9984230000";
        UserRoleEnum loggedInCustomerRole = UserRoleEnum.CUSTOMER;
        if (loggedInCustomerRole.equals(UserRoleEnum.CUSTOMER) && !loggedInCustomerId.equals(customerId)) {
            return false;
        }*/
        return true;
    }

    /**
     * Gets the customer accounts
     *
     * @param customerId
     * @return
     */
    public List<SavingsAccountDTO> getCustomerAccounts(String customerId) {
        if (!isAuthorized(customerId)) {
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.not_permitted, "You are not permitted to do this action");
        }
        List<SavingsAccount> accounts = savingsAccountRepo.findByCustomerId(customerId);
        if (null != accounts) {
            return accounts.stream().map(acc -> getDTO(acc, getNewDTO())).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Creates new savings account
     *
     * @param account
     * @return
     */
    @Transactional
    public SavingsAccountDTO createAccount(SavingsAccountDTO account) {

        //creates the savings account
        SavingsAccountDTO savingsAccountDTO = create(account);
        logger.info("Created savings account for the customer " + account.getCustomerId());
        // credits the minimum balance
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO()
                .setTransactionType(TransactionTypeEnum.CREDIT)
                .setAmount(MINIMUM_BALANCE)
                .setComments("Registration Deposit")
                .setCustomerId(account.getCustomerId());
        savingsAccountDTO = doTransaction(savingsAccountDTO.getId(), transactionRequest);
        return savingsAccountDTO;
    }

    /**
     * Updates the account balance
     *
     * @param accountDTO
     * @return
     */
    public SavingsAccountDTO updateAccountBalance(SavingsAccountDTO accountDTO) {
        if (null != accountDTO && !isAuthorized(accountDTO.getCustomerId())) {
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.not_permitted, "You are not permitted to do this action");
        }
        SavingsAccount existingAccount = getEntity(accountDTO.getId());
        existingAccount.setBalanceAmount(accountDTO.getBalanceAmount());
        existingAccount = updateEntity(existingAccount);
        return getDTO(existingAccount, getNewDTO());
    }

    /**
     * Deletes the savings account
     *
     * @param accountId
     * @return
     */
    public Boolean deleteAccount(String accountId) {
        SavingsAccountDTO accountDTO = get(accountId);
        if (null != accountDTO && !isAuthorized(accountDTO.getCustomerId())) {
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.not_permitted, "You are not permitted to do this action");
        }
        return disable(accountId);
    }

    /**
     * Validates the requests
     *
     * @param accountDTO
     */
    private void validateRequest(SavingsAccountDTO accountDTO) {
        if (null == accountDTO) {
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.invalid_request_exception, "Savings Account does not exist.");
        }
        if (!isAuthorized(accountDTO.getCustomerId())) {
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.not_permitted, "You are not permitted to do this action");
        }
    }

    /**
     * Do transact (credit/debit)
     *
     * @param accountId
     * @param transactionRequestDTO
     * @return
     */
    @Transactional
    public SavingsAccountDTO doTransaction(String accountId, TransactionRequestDTO transactionRequestDTO) {

        SavingsAccountDTO savingsAccountDTO = get(accountId);
        validateRequest(savingsAccountDTO);
        Double newBalance = getNewBalance(transactionRequestDTO.getAmount(), savingsAccountDTO.getBalanceAmount(), transactionRequestDTO.getTransactionType());

        //Starts the transaction
        TransactionDTO transactionDTO = transactionService.createTransaction(transactionRequestDTO, savingsAccountDTO, TransactionStatusEnum.INPROGRESS);
        try {
            // Generates the account statement and updates the account balance
            savingsAccountDTO = doAccountTransaction(savingsAccountDTO, transactionDTO, newBalance, transactionRequestDTO.getTransactionType());
            transactionDTO.setTransactionStatus(TransactionStatusEnum.SUCCESS);
            if (transactionRequestDTO.getTransactionType().equals(TransactionTypeEnum.CREDIT)) {
                logger.info("Deposited the amount " + transactionRequestDTO.getAmount() + " to account " + savingsAccountDTO.getAccountNumber());
            } else {
                logger.info("Debited the amount " + transactionRequestDTO.getAmount() + " from account " + savingsAccountDTO.getAccountNumber());
            }
        } catch (Exception e) {
            transactionDTO.setTransactionStatus(TransactionStatusEnum.FAILED);
            logger.info("Transaction of amount " + transactionRequestDTO.getAmount() + " is failed for account " + savingsAccountDTO.getAccountNumber());

        }
        // Completes the transaction
        transactionDTO = transactionService.update(transactionDTO);
        return savingsAccountDTO;
    }

    /**
     * Estimates the balance amount on transaction. Checks the current balance and minimu balance
     *
     * @param transactionAmount
     * @param newBalance
     * @param transactionType
     * @return
     */
    private Double getNewBalance(Double transactionAmount, Double newBalance, TransactionTypeEnum transactionType) {
        switch (transactionType) {
            case DEBIT:
                newBalance = newBalance - transactionAmount;
                if (newBalance < 0) {
                    throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.invalid_request_exception, "Insufficient account balance.");
                }
                if (newBalance < MINIMUM_BALANCE) {
                    throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.invalid_request_exception, "You cannot withdraw this amount due to the minimum balance.");
                }
                break;
            case CREDIT:
                newBalance = newBalance + transactionAmount;
                break;
        }
        return newBalance;
    }

    /**
     * Method for transferring funds
     *
     * @param fundTransferRequestDTO
     * @return
     */
    @Transactional
    public FundTransferDTO transferFund(FundTransferRequestDTO fundTransferRequestDTO) {

        SavingsAccountDTO fromAccountDTO = get(fundTransferRequestDTO.getFromAccount());
        validateRequest(fromAccountDTO);
        SavingsAccountDTO toAccountDTO = get(fundTransferRequestDTO.getToAccount());
        validateRequest(toAccountDTO);

        Double fromAccountNewBalance = getNewBalance(fundTransferRequestDTO.getAmount(), fromAccountDTO.getBalanceAmount(), TransactionTypeEnum.DEBIT);
        Double toAccountNewBalance = getNewBalance(fundTransferRequestDTO.getAmount(), toAccountDTO.getBalanceAmount(), TransactionTypeEnum.CREDIT);
        logger.info("Fund transfer initiated from account " + fromAccountDTO.getAccountNumber() + " to account " + toAccountDTO.getAccountNumber());

        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO()
                .setAmount(fundTransferRequestDTO.getAmount())
                .setComments(fundTransferRequestDTO.getComments())
                .setEmail(fundTransferRequestDTO.getEmail())
                .setCustomerId(fundTransferRequestDTO.getCustomerId());
        TransactionDTO transactionDTO = transactionService.createTransaction(transactionRequestDTO, fromAccountDTO, TransactionStatusEnum.INPROGRESS);

        try {
            fromAccountDTO = doAccountTransaction(fromAccountDTO, transactionDTO, fromAccountNewBalance, TransactionTypeEnum.DEBIT);
            toAccountDTO = doAccountTransaction(toAccountDTO, transactionDTO, toAccountNewBalance, TransactionTypeEnum.CREDIT);
            transactionDTO.setTransactionStatus(TransactionStatusEnum.SUCCESS);
        } catch (Exception e) {
            transactionDTO.setTransactionStatus(TransactionStatusEnum.FAILED);
        }
        transactionDTO = transactionService.update(transactionDTO);

        FundTransferDTO fundTransferDTO = fundTransferService.createFundTransfer(fundTransferRequestDTO, fromAccountDTO, toAccountDTO, transactionDTO);
        logger.info("Successfully transferred amount " + fundTransferRequestDTO.getAmount() + " from account " + fromAccountDTO.getAccountNumber() + " to account " + toAccountDTO.getAccountNumber());

        return fundTransferDTO;
    }

    /**
     * Creates the account statement and updates the balance amount
     *
     * @param accountDTO
     * @param transactionDTO
     * @param newBalance
     * @param transactionType
     * @return
     */
    private SavingsAccountDTO doAccountTransaction(SavingsAccountDTO accountDTO, TransactionDTO transactionDTO, Double newBalance, TransactionTypeEnum transactionType) {
        try {
            AccountStatementDTO accountStatement = accountStatementService.createAccountStatement(accountDTO, transactionDTO, newBalance, transactionType);
        } catch (Exception e) {
            transactionDTO.setFailureReason(e.getMessage());
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.operation_failed_exception, "Transaction failed due to, " + e.getMessage());
        }
        accountDTO.setBalanceAmount(newBalance);
        return updateAccountBalance(accountDTO);
    }

    /**
     * Gets the paginated account statements
     *
     * @param accountStatementRequest
     * @return
     */
    public PaginatedAccountStatementResponseDTO getAccountStatements(AccountStatementRequestDTO accountStatementRequest) {
        SavingsAccount savingsAccount = getEntity(accountStatementRequest.getAccountId());
        SavingsAccountDTO savingsAccountDTO = getDTO(savingsAccount, getNewDTO());
        validateRequest(savingsAccountDTO);
        List<AccountStatementDTO> accountStatements = accountStatementService.getAccountStatements(savingsAccount, accountStatementRequest);
        PaginatedAccountStatementResponseDTO response = new PaginatedAccountStatementResponseDTO()
                .setAccount(savingsAccountDTO)
                .setAccountStatements(accountStatements)
                .setPage(accountStatementRequest.getPage())
                .setPageSize(accountStatementRequest.getPageSize());
        return response;
    }
}
