package com.inteview.banking.savingsAccountService.accountStatement.service;

import com.inteview.banking.savingsAccountService.account.domain.SavingsAccount;
import com.inteview.banking.savingsAccountService.account.dto.SavingsAccountDTO;
import com.inteview.banking.savingsAccountService.account.service.SavingsAccountService;
import com.inteview.banking.savingsAccountService.accountStatement.domain.AccountStatement;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementDTO;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementRequestDTO;
import com.inteview.banking.savingsAccountService.accountStatement.repo.AccountStatementRepo;
import com.inteview.banking.savingsAccountService.base.exceptions.SavingsAppException;
import com.inteview.banking.savingsAccountService.base.service.AbstractService;
import com.inteview.banking.savingsAccountService.constants.TransactionModeEnum;
import com.inteview.banking.savingsAccountService.constants.TransactionTypeEnum;
import com.inteview.banking.savingsAccountService.transaction.dto.TransactionDTO;
import com.inteview.banking.savingsAccountService.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to manage account statements
 */
@Service
public class AccountStatementService extends AbstractService<AccountStatement, AccountStatementDTO> {

    @Autowired
    private AccountStatementRepo accountStatementRepo;

    @Autowired
    private SavingsAccountService savingsAccountService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public CrudRepository<AccountStatement, String> getRepository() {
        return accountStatementRepo;
    }

    @Override
    public AccountStatementDTO getDTO(AccountStatement entity, AccountStatementDTO dto) throws SavingsAppException {
        dto.setAccount(savingsAccountService.getDTO(entity.getAccount(), savingsAccountService.getNewDTO()))
                .setNewBalanceAmount(entity.getNewBalanceAmount())
                .setOldBalanceAmount(entity.getOldBalanceAmount())
                .setTransactionAmount(entity.getTransactionAmount())
                .setTransactionMode(entity.getTransactionMode())
                .setTransactionType(entity.getTransactionType());
               // .setTransaction(transactionService.getDTO(entity.getTransaction(), transactionService.getNewDTO()));
        return copyDTO(entity, dto);
    }

    @Override
    public AccountStatement populateEntity(AccountStatementDTO dto, AccountStatement entity) throws SavingsAppException {
        return entity.setNewBalanceAmount(dto.getNewBalanceAmount())
                .setOldBalanceAmount(dto.getOldBalanceAmount())
                .setTransactionAmount(dto.getTransactionAmount())
                .setTransactionMode(dto.getTransactionMode())
                .setTransactionType(dto.getTransactionType())
                .setAccount(savingsAccountService.getEntity(dto.getAccount().getId()))
                .setTransaction(transactionService.getEntity(dto.getTransaction().getId()))
                .setId(dto.getId());
    }

    @Override
    public AccountStatement updateModifiedParams(AccountStatementDTO dto, AccountStatement existingEntity) throws SavingsAppException {
        return existingEntity;
    }

    @Override
    public AccountStatementDTO getNewDTO() throws SavingsAppException {
        return new AccountStatementDTO();
    }

    @Override
    public AccountStatement getNewEntity() throws SavingsAppException {
        return new AccountStatement();
    }

    /**
     * Creates account statement
     * @param accountDTO
     * @param transactionDTO
     * @param newBalance
     * @param transactionType
     * @return
     */
    public AccountStatementDTO createAccountStatement(SavingsAccountDTO accountDTO, TransactionDTO transactionDTO, Double newBalance, TransactionTypeEnum transactionType) {
        AccountStatementDTO accountStatementDTO = new AccountStatementDTO()
                .setAccount(accountDTO)
                .setNewBalanceAmount(newBalance)
                .setOldBalanceAmount(accountDTO.getBalanceAmount())
                .setTransactionAmount(transactionDTO.getAmount())
                .setTransaction(transactionDTO)
                .setTransactionMode(TransactionModeEnum.ONLINE)
                .setTransactionType(transactionType);
        return create(accountStatementDTO);
    }

    /**
     * Gets paginated account statements
     * @param savingsAccount
     * @param accountStatementRequest
     * @return
     */
    public List<AccountStatementDTO> getAccountStatements(SavingsAccount savingsAccount, AccountStatementRequestDTO accountStatementRequest) {
        int page = 1;
        int pageSize = 20;
        if (null != accountStatementRequest.getPage() && null != accountStatementRequest.getPageSize()) {
            page = accountStatementRequest.getPage();
            pageSize = accountStatementRequest.getPageSize();
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AccountStatement> accountStatements = accountStatementRepo.findByAccountOrderByCreatedTsDesc(savingsAccount, pageable);
        if (null != accountStatements && !accountStatements.isEmpty()) {
            return accountStatements.stream().map(acc -> getDTO(acc, getNewDTO())).collect(Collectors.toList());
        }
        return null;
    }
}
