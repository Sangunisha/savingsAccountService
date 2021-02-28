package com.inteview.banking.savingsAccountService.fundTransfer.repo;

import com.inteview.banking.savingsAccountService.fundTransfer.domain.FundTransfer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundTransferRepo extends CrudRepository<FundTransfer, String> {

}
