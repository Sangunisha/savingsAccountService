package com.inteview.banking.savingsAccountService.account.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementDTO;
import com.inteview.banking.savingsAccountService.base.dto.BaseDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavingsAccountDTO extends BaseDTO<SavingsAccountDTO> implements Serializable {

    @NotNull
    private String accountNumber;

  /*  @Enumerated(EnumType.STRING)
    @NotNull
    private AccountTypeEnum accountType;*/

    private Double balanceAmount = 0d;

    @NotNull
    private String branchId;
    @NotNull
    private String customerId;

    private Set<AccountStatementDTO> accountTransactions;

    public String getAccountNumber() {
        return accountNumber;
    }

    public SavingsAccountDTO setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

  /*  public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public SavingsAccountDTO setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
        return this;
    }*/

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public SavingsAccountDTO setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
        return this;
    }

    public String getBranchId() {
        return branchId;
    }

    public SavingsAccountDTO setBranchId(String branchId) {
        this.branchId = branchId;
        return this;
    }

    public Set<AccountStatementDTO> getAccountTransactions() {
        return accountTransactions;
    }

    public SavingsAccountDTO setAccountTransactions(Set<AccountStatementDTO> accountTransactions) {
        this.accountTransactions = accountTransactions;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public SavingsAccountDTO setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }
}
