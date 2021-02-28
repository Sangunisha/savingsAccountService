package com.inteview.banking.savingsAccountService.accountStatement.domain;

import com.inteview.banking.savingsAccountService.account.domain.SavingsAccount;
import com.inteview.banking.savingsAccountService.base.domain.BaseDomainObject;
import com.inteview.banking.savingsAccountService.constants.TransactionModeEnum;
import com.inteview.banking.savingsAccountService.constants.TransactionTypeEnum;
import com.inteview.banking.savingsAccountService.transaction.domain.Transaction;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class AccountStatement extends BaseDomainObject<AccountStatement> {

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum transactionType;
    @NotNull
    private TransactionModeEnum transactionMode = TransactionModeEnum.ONLINE;
    @NotNull
    private double transactionAmount = 0;
    @NotNull
    private double oldBalanceAmount = 0;
    @NotNull
    private double newBalanceAmount = 0;
    @NotNull
    @ManyToOne
    private Transaction transaction;
    @ManyToOne
    private SavingsAccount account;

    public TransactionTypeEnum getTransactionType() {
        return transactionType;
    }

    public AccountStatement setTransactionType(TransactionTypeEnum transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public TransactionModeEnum getTransactionMode() {
        return transactionMode;
    }

    public AccountStatement setTransactionMode(TransactionModeEnum transactionMode) {
        this.transactionMode = transactionMode;
        return this;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public AccountStatement setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public double getOldBalanceAmount() {
        return oldBalanceAmount;
    }

    public AccountStatement setOldBalanceAmount(double oldBalanceAmount) {
        this.oldBalanceAmount = oldBalanceAmount;
        return this;
    }

    public double getNewBalanceAmount() {
        return newBalanceAmount;
    }

    public AccountStatement setNewBalanceAmount(double newBalanceAmount) {
        this.newBalanceAmount = newBalanceAmount;
        return this;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public AccountStatement setTransaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public SavingsAccount getAccount() {
        return account;
    }

    public AccountStatement setAccount(SavingsAccount account) {
        this.account = account;
        return this;
    }
}
