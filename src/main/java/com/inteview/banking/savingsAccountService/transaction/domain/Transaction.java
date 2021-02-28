package com.inteview.banking.savingsAccountService.transaction.domain;

import com.inteview.banking.savingsAccountService.account.domain.SavingsAccount;
import com.inteview.banking.savingsAccountService.accountStatement.domain.AccountStatement;
import com.inteview.banking.savingsAccountService.base.domain.BaseDomainObject;
import com.inteview.banking.savingsAccountService.constants.TransactionStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Transaction extends BaseDomainObject<Transaction> {

 /*   @NotNull
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

    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY)
    private Set<AccountStatement> accountTransactions;

    @ManyToOne
    private SavingsAccount account;

   /* public String getTransactionId() {
        return transactionId;
    }

    public Transaction setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }*/

    public String getCustomerId() {
        return customerId;
    }

    public Transaction setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public TransactionStatusEnum getTransactionStatus() {
        return transactionStatus;
    }

    public Transaction setTransactionStatus(TransactionStatusEnum transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public Transaction setFailureReason(String failureReason) {
        this.failureReason = failureReason;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public Transaction setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Transaction setEmail(String email) {
        this.email = email;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Transaction setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public Set<AccountStatement> getAccountTransactions() {
        return accountTransactions;
    }

    public Transaction setAccountTransactions(Set<AccountStatement> accountTransactions) {
        this.accountTransactions = accountTransactions;
        return this;
    }

    public SavingsAccount getAccount() {
        return account;
    }

    public Transaction setAccount(SavingsAccount account) {
        this.account = account;
        return this;
    }
}
