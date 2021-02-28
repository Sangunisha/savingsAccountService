package com.inteview.banking.savingsAccountService.fundTransfer.domain;

import com.inteview.banking.savingsAccountService.account.domain.SavingsAccount;
import com.inteview.banking.savingsAccountService.base.domain.BaseDomainObject;
import com.inteview.banking.savingsAccountService.constants.FundTransferTypeEnum;
import com.inteview.banking.savingsAccountService.transaction.domain.Transaction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class FundTransfer extends BaseDomainObject<FundTransfer> {

    @NotNull
    private String customerId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private FundTransferTypeEnum fundTransferType;

    @NotNull
    private String comments;
    private String email;
    @NotNull
    private double amount;

    @OneToOne(fetch = FetchType.LAZY)
    private SavingsAccount fromAccount;
    @OneToOne
    private SavingsAccount toAccount;
    @OneToOne
    private Transaction transaction;
    // Future scope
    private String toBeneficiary;

    public String getCustomerId() {
        return customerId;
    }

    public FundTransfer setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public FundTransferTypeEnum getFundTransferType() {
        return fundTransferType;
    }

    public FundTransfer setFundTransferType(FundTransferTypeEnum fundTransferType) {
        this.fundTransferType = fundTransferType;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public FundTransfer setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public FundTransfer setEmail(String email) {
        this.email = email;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public FundTransfer setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public SavingsAccount getFromAccount() {
        return fromAccount;
    }

    public FundTransfer setFromAccount(SavingsAccount fromAccount) {
        this.fromAccount = fromAccount;
        return this;
    }

    public SavingsAccount getToAccount() {
        return toAccount;
    }

    public FundTransfer setToAccount(SavingsAccount toAccount) {
        this.toAccount = toAccount;
        return this;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public FundTransfer setTransaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public String getToBeneficiary() {
        return toBeneficiary;
    }

    public FundTransfer setToBeneficiary(String toBeneficiary) {
        this.toBeneficiary = toBeneficiary;
        return this;
    }
}
