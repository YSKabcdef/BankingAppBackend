package net.java.portfolio.banking.service;

import java.util.List;

import net.java.portfolio.banking.dto.AccountDto;
import net.java.portfolio.banking.dto.TransferFundDto;

public interface AccountService {
    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountById(Long id);

    AccountDto depositAccount(Long id, double amount);

    AccountDto withdrawAccount(Long id, double amount);

    List<AccountDto> findAllAccounts();

    void deleteAccount(Long id);
    
    void transferFunds(TransferFundDto transferFundDto);
}   
