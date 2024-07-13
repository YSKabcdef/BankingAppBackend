package net.java.portfolio.banking.service.impl;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.java.portfolio.banking.dto.AccountDto;
import net.java.portfolio.banking.entity.Account;
import net.java.portfolio.banking.entity.mapper.AccountMapper;
import net.java.portfolio.banking.repo.AccountRepo;
import net.java.portfolio.banking.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepo accountRepo;

    private AccountServiceImpl(AccountRepo accountRepo){
        this.accountRepo = accountRepo;
    }
    
    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepo.findById(id).orElseThrow(() -> new RuntimeException("Account does not exist"));
        accountRepo.delete(account);
        
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto){
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepo.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }
    @Override
    public AccountDto withdrawAccount(Long id, double amount) {
        Account account = accountRepo.findById(id).orElseThrow(() -> new RuntimeException("Account does not exist"));
        if(account.getBalance() < amount){
            throw new RuntimeException("Insufficient Amount");
        }
        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepo.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id){
        Account account = accountRepo.findById(id).orElseThrow(() -> new RuntimeException("Account does not exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto depositAccount(Long id, double amount) {
        Account account = accountRepo.findById(id).orElseThrow(() -> new RuntimeException("Account does not exist"));
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepo.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> findAllAccounts() {
        // TODO Auto-generated method stub
        List<Account> accounts = accountRepo.findAll();
        return accounts.stream().map((account)->AccountMapper.mapToAccountDto(account)).collect(Collectors.toList());
        
    }
}
