package com.inteview.banking.savingsAccountService.account.domain;

import com.inteview.banking.savingsAccountService.accountStatement.domain.AccountStatement;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementDTO;
import com.inteview.banking.savingsAccountService.base.domain.BaseDomainObject;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class SavingsAccount extends BaseDomainObject<SavingsAccount> {

    @Size(min = 12, max = 12)
    @NotNull
    private String accountNumber;

    @NotNull
    private String customerId;

    /*@Enumerated(EnumType.STRING)
    @NotNull
    private AccountTypeEnum accountType;*/

    private Double balanceAmount = 0d;

    @NotNull
    private String branchId;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<AccountStatement> accountTransactions;

    public String getAccountNumber() {
        return accountNumber;
    }

    public SavingsAccount setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    /*public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public SavingsAccount setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
        return this;
    }*/

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public SavingsAccount setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
        return this;
    }

    public String getBranchId() {
        return branchId;
    }

    public SavingsAccount setBranchId(String branchId) {
        this.branchId = branchId;
        return this;
    }

    public Set<AccountStatement> getAccountTransactions() {
        return accountTransactions;
    }

    public SavingsAccount setAccountTransactions(Set<AccountStatement> accountTransactions) {
        this.accountTransactions = accountTransactions;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public SavingsAccount setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }
}
