package net.java.portfolio.banking.service.impl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.java.portfolio.banking.dto.AccountDto;
import net.java.portfolio.banking.dto.TransactionDto;
import net.java.portfolio.banking.dto.TransferFundDto;
import net.java.portfolio.banking.entity.Account;
import net.java.portfolio.banking.entity.Transaction;
import net.java.portfolio.banking.exception.AccountException;
import net.java.portfolio.banking.mapper.AccountMapper;
import net.java.portfolio.banking.mapper.TransactionMapper;
import net.java.portfolio.banking.repo.AccountRepo;
import net.java.portfolio.banking.repo.TransactionRepo;
import net.java.portfolio.banking.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepo accountRepo;
    private TransactionRepo transactionRepo;
    private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    private static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
    private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";
    private AccountServiceImpl(AccountRepo accountRepo, TransactionRepo transactionRepo){
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }
    
    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepo.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
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
        Account account = accountRepo.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        if(account.getBalance() < amount){
            throw new RuntimeException("Insufficient Amount");
        }
        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepo.save(account);
        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepo.save(transaction);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id){
        Account account = accountRepo.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto depositAccount(Long id, double amount) {
        Account account = accountRepo.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepo.save(transaction);
        Account savedAccount = accountRepo.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> findAllAccounts() {
        List<Account> accounts = accountRepo.findAll();
        return accounts.stream().map((account)->AccountMapper.mapToAccountDto(account)).collect(Collectors.toList());
        
    }

    @Override
    public void transferFunds(TransferFundDto transferFundDto) {
        // retrieve the account from each sender  
        Account fromAccount = accountRepo.findById(transferFundDto.fromAccountId()).orElseThrow(() -> new AccountException("Account does not exist"));
        Account toAccount = accountRepo.findById(transferFundDto.toAccountId()).orElseThrow(() -> new AccountException("Account does not exist"));
        if(fromAccount.getBalance()<transferFundDto.amount()){
            throw new RuntimeException("Insufficient Amount");
        }
        fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());
        accountRepo.save(fromAccount);
        Transaction transaction = new Transaction();
        transaction.setAccountId(transferFundDto.fromAccountId());
        transaction.setAmount(transferFundDto.amount());
        transaction.setTransactionType(TRANSACTION_TYPE_TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepo.save(transaction);
        toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());
        accountRepo.save(toAccount);
    }

    @Override
    public List<TransactionDto> getAccountTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepo.findByAccountIdOrderByTimestampDesc(accountId);
        
        return transactions.stream().map((transaction)->TransactionMapper.mapToTransactionDto(transaction)).collect(Collectors.toList());
    }
}
