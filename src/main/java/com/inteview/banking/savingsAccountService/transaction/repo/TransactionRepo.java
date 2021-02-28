package com.inteview.banking.savingsAccountService.transaction.repo;

import com.inteview.banking.savingsAccountService.account.domain.SavingsAccount;
import com.inteview.banking.savingsAccountService.constants.TransactionStatusEnum;
import com.inteview.banking.savingsAccountService.transaction.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends CrudRepository<Transaction, String> {

    Transaction findByAccountAndTransactionStatus(SavingsAccount accountId, TransactionStatusEnum transactionStatus);
}
