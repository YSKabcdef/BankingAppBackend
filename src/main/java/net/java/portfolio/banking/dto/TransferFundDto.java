package net.java.portfolio.banking.dto;

public record TransferFundDto(Long fromAccountId, Long toAccountId, double amount) {
    
}
