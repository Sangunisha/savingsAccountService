package com.inteview.banking.savingsAccountService.transaction.service;

import com.inteview.banking.savingsAccountService.account.domain.SavingsAccount;
import com.inteview.banking.savingsAccountService.account.dto.SavingsAccountDTO;
import com.inteview.banking.savingsAccountService.account.service.SavingsAccountService;
import com.inteview.banking.savingsAccountService.base.exceptions.SavingsAppException;
import com.inteview.banking.savingsAccountService.base.service.AbstractService;
import com.inteview.banking.savingsAccountService.constants.TransactionStatusEnum;
import com.inteview.banking.savingsAccountService.transaction.domain.Transaction;
import com.inteview.banking.savingsAccountService.transaction.dto.TransactionDTO;
import com.inteview.banking.savingsAccountService.transaction.dto.TransactionRequestDTO;
import com.inteview.banking.savingsAccountService.transaction.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Service class to manage transaction entities
 */
@Service
public class TransactionService extends AbstractService<Transaction, TransactionDTO> {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private SavingsAccountService savingsAccountService;

    @Override
    public CrudRepository<Transaction, String> getRepository() {
        return transactionRepo;
    }

    @Override
    public TransactionDTO getDTO(Transaction entity, TransactionDTO dto) throws SavingsAppException {
        dto.setAccount(savingsAccountService.getDTO(entity.getAccount(), savingsAccountService.getNewDTO()))
                .setAmount(entity.getAmount())
                .setComments(entity.getComments())
                .setCustomerId(entity.getCustomerId())
                .setEmail(entity.getEmail())
                .setFailureReason(entity.getFailureReason())
                .setTransactionStatus(entity.getTransactionStatus());
        return copyDTO(entity, dto);
    }

    @Override
    public Transaction populateEntity(TransactionDTO dto, Transaction entity) throws SavingsAppException {
        return entity.setAccount(savingsAccountService.getEntity(dto.getAccount().getId()))
                .setAmount(dto.getAmount())
                .setComments(dto.getComments())
                .setCustomerId(dto.getCustomerId())
                .setEmail(dto.getEmail())
                .setFailureReason(dto.getFailureReason())
                .setTransactionStatus(dto.getTransactionStatus());
    }

    @Override
    public Transaction updateModifiedParams(TransactionDTO dto, Transaction existingEntity) throws SavingsAppException {
        return existingEntity.setFailureReason(dto.getFailureReason())
                .setTransactionStatus(dto.getTransactionStatus());
    }

    @Override
    public TransactionDTO getNewDTO() throws SavingsAppException {
        return new TransactionDTO();
    }

    @Override
    public Transaction getNewEntity() throws SavingsAppException {
        return new Transaction();
    }

    /**
     * Ceates Transaction
     *
     * @param transactionRequestDTO
     * @param savingsAccountDTO
     * @param inprogress
     * @return
     */
    public TransactionDTO createTransaction(TransactionRequestDTO transactionRequestDTO, SavingsAccountDTO savingsAccountDTO, TransactionStatusEnum inprogress) {
        if (transactionExists(savingsAccountDTO.getId(), TransactionStatusEnum.INPROGRESS)) {
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.invalid_state, "The account is currently under a transaction. Please try after some time");
        }
        TransactionDTO transactionDTO = new TransactionDTO()
                .setTransactionStatus(TransactionStatusEnum.INPROGRESS)
                .setAccount(savingsAccountDTO)
                .setAmount(transactionRequestDTO.getAmount())
                .setComments(transactionRequestDTO.getComments())
                .setCustomerId(transactionRequestDTO.getCustomerId());
        return create(transactionDTO);
    }

    /**
     * Checks whether exists any transaction for the account with in progress status
     *
     * @param accountId
     * @param transactionStatus
     * @return
     */
    public boolean transactionExists(String accountId, TransactionStatusEnum transactionStatus) {
        SavingsAccount savingsAccount = savingsAccountService.getEntity(accountId);
        Transaction inProgressTransaction = transactionRepo.findByAccountAndTransactionStatus(savingsAccount, transactionStatus);
        return null != inProgressTransaction;
    }
}
