package com.inteview.banking.savingsAccountService.account.repo;

import com.inteview.banking.savingsAccountService.account.domain.SavingsAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsAccountRepo extends CrudRepository<SavingsAccount, String> {

    List<SavingsAccount> findByCustomerId(String customerId);
}
