package com.inteview.banking.savingsAccountService.accountStatement.dto;

import com.inteview.banking.savingsAccountService.account.dto.SavingsAccountDTO;

import java.util.List;


public class PaginatedAccountStatementResponseDTO {


    private SavingsAccountDTO account;

    private List<AccountStatementDTO> accountStatements;

    private int pageSize;
    private int page;

    public SavingsAccountDTO getAccount() {
        return account;
    }

    public PaginatedAccountStatementResponseDTO setAccount(SavingsAccountDTO account) {
        this.account = account;
        return this;
    }

    public List<AccountStatementDTO> getAccountStatements() {
        return accountStatements;
    }

    public PaginatedAccountStatementResponseDTO setAccountStatements(List<AccountStatementDTO> accountStatements) {
        this.accountStatements = accountStatements;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PaginatedAccountStatementResponseDTO setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getPage() {
        return page;
    }

    public PaginatedAccountStatementResponseDTO setPage(int page) {
        this.page = page;
        return this;
    }
}
