package com.inteview.banking.savingsAccountService.accountStatement.repo;

import com.inteview.banking.savingsAccountService.account.domain.SavingsAccount;
import com.inteview.banking.savingsAccountService.accountStatement.domain.AccountStatement;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountStatementRepo extends CrudRepository<AccountStatement, String> {

    List<AccountStatement> findByAccountOrderByCreatedTsDesc(SavingsAccount accountId, Pageable pageable);
}
