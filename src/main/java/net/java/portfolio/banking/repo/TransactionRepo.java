package net.java.portfolio.banking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.java.portfolio.banking.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);

}
