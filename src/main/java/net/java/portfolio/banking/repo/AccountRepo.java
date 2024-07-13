package net.java.portfolio.banking.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import net.java.portfolio.banking.entity.Account;

public interface AccountRepo extends JpaRepository<Account,Long>{
    
}
