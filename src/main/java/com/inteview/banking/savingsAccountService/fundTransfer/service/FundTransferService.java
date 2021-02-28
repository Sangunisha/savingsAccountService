package com.inteview.banking.savingsAccountService.fundTransfer.service;

import com.inteview.banking.savingsAccountService.account.dto.SavingsAccountDTO;
import com.inteview.banking.savingsAccountService.account.service.SavingsAccountService;
import com.inteview.banking.savingsAccountService.base.exceptions.SavingsAppException;
import com.inteview.banking.savingsAccountService.base.service.AbstractService;
import com.inteview.banking.savingsAccountService.fundTransfer.domain.FundTransfer;
import com.inteview.banking.savingsAccountService.fundTransfer.dto.FundTransferDTO;
import com.inteview.banking.savingsAccountService.fundTransfer.dto.FundTransferRequestDTO;
import com.inteview.banking.savingsAccountService.fundTransfer.repo.FundTransferRepo;
import com.inteview.banking.savingsAccountService.transaction.dto.TransactionDTO;
import com.inteview.banking.savingsAccountService.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Service class to manage fund transfer data
 */
@Service
public class FundTransferService extends AbstractService<FundTransfer, FundTransferDTO> {

    @Autowired
    private FundTransferRepo fundTransferRepo;

    @Autowired
    private SavingsAccountService savingsAccountService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public CrudRepository<FundTransfer, String> getRepository() {
        return fundTransferRepo;
    }

    @Override
    public FundTransferDTO getDTO(FundTransfer entity, FundTransferDTO dto) throws SavingsAppException {
        dto.setAmount(entity.getAmount())
                .setComments(entity.getComments())
                .setCustomerId(entity.getCustomerId())
                .setEmail(entity.getEmail())
                .setFundTransferType(entity.getFundTransferType())
                .setFromAccount(savingsAccountService.getDTO(entity.getFromAccount(), savingsAccountService.getNewDTO()))
                .setToAccount(savingsAccountService.getDTO(entity.getToAccount(), savingsAccountService.getNewDTO()))
                .setToBeneficiary(entity.getToBeneficiary())
                .setTransaction(transactionService.getDTO(entity.getTransaction(), transactionService.getNewDTO()));
        return copyDTO(entity, dto);
    }

    @Override
    public FundTransfer populateEntity(FundTransferDTO dto, FundTransfer entity) throws SavingsAppException {
        return entity.setAmount(dto.getAmount())
                .setComments(dto.getComments())
                .setCustomerId(dto.getCustomerId())
                .setEmail(dto.getEmail())
                .setFundTransferType(dto.getFundTransferType())
                .setFromAccount(savingsAccountService.getEntity(dto.getFromAccount().getId()))
                .setToAccount(savingsAccountService.getEntity(dto.getToAccount().getId()))
                .setToBeneficiary(dto.getToBeneficiary())
                .setTransaction(transactionService.getEntity(dto.getTransaction().getId()))
                .setId(dto.getId());
    }

    @Override
    public FundTransfer updateModifiedParams(FundTransferDTO dto, FundTransfer existingEntity) throws SavingsAppException {
        return existingEntity;
    }

    @Override
    public FundTransferDTO getNewDTO() throws SavingsAppException {
        return new FundTransferDTO();
    }

    @Override
    public FundTransfer getNewEntity() throws SavingsAppException {
        return new FundTransfer();
    }


    /**
     * Creates fund transfer
     * @param fundTransferRequestDTO
     * @param fromAccountDTO
     * @param toAccountDTO
     * @param transactionDTO
     * @return
     */
    public FundTransferDTO createFundTransfer(FundTransferRequestDTO fundTransferRequestDTO, SavingsAccountDTO fromAccountDTO, SavingsAccountDTO toAccountDTO, TransactionDTO transactionDTO) {
        FundTransferDTO fundTransferDTO = new FundTransferDTO()
                .setFundTransferType(fundTransferRequestDTO.getFundTransferType())
                .setAmount(fundTransferRequestDTO.getAmount())
                .setComments(fundTransferRequestDTO.getComments())
                .setCustomerId(fundTransferRequestDTO.getCustomerId())
                .setEmail(fundTransferRequestDTO.getEmail())
                .setFromAccount(fromAccountDTO)
                .setToAccount(toAccountDTO)
                .setTransaction(transactionDTO);
        return create(fundTransferDTO);
    }

}
