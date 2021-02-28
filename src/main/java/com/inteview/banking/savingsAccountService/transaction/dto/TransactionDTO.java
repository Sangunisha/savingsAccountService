package com.inteview.banking.savingsAccountService.transaction.dto;

import com.inteview.banking.savingsAccountService.account.dto.SavingsAccountDTO;
import com.inteview.banking.savingsAccountService.accountStatement.dto.AccountStatementDTO;
import com.inteview.banking.savingsAccountService.base.dto.BaseDTO;
import com.inteview.banking.savingsAccountService.constants.TransactionStatusEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class TransactionDTO extends BaseDTO<TransactionDTO> {

    /*@NotNull
    private String transactionId;*/
    @NotNull
    private String customerId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum transactionStatus;
    private String failureReason;
    @NotNull
    private String comments;
    private String email;
    @NotNull
    private double amount;

    private Set<AccountStatementDTO> accountTransactions;

    private SavingsAccountDTO account;

   /* public String getTransactionId() {
        return transactionId;
    }

    public TransactionDTO setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }*/

    public String getCustomerId() {
        return customerId;
    }

    public TransactionDTO setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public TransactionStatusEnum getTransactionStatus() {
        return transactionStatus;
    }

    public TransactionDTO setTransactionStatus(TransactionStatusEnum transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public TransactionDTO setFailureReason(String failureReason) {
        this.failureReason = failureReason;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public TransactionDTO setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TransactionDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionDTO setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public Set<AccountStatementDTO> getAccountTransactions() {
        return accountTransactions;
    }

    public TransactionDTO setAccountTransactions(Set<AccountStatementDTO> accountTransactions) {
        this.accountTransactions = accountTransactions;
        return this;
    }

    public SavingsAccountDTO getAccount() {
        return account;
    }

    public TransactionDTO setAccount(SavingsAccountDTO account) {
        this.account = account;
        return this;
    }
}
