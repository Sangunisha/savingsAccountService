package com.inteview.banking.savingsAccountService.transaction.dto;

import com.inteview.banking.savingsAccountService.base.dto.BaseDTO;
import com.inteview.banking.savingsAccountService.constants.TransactionTypeEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class TransactionRequestDTO {

    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum transactionType;
    @NotNull
    private String comments;
    private String email;
    @NotNull
    private double amount;

    private String customerId;

    public TransactionTypeEnum getTransactionType() {
        return transactionType;
    }

    public TransactionRequestDTO setTransactionType(TransactionTypeEnum transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public TransactionRequestDTO setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TransactionRequestDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionRequestDTO setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public TransactionRequestDTO setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }
}
