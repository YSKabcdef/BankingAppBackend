package net.java.portfolio.banking.mapper;

import net.java.portfolio.banking.dto.TransactionDto;
import net.java.portfolio.banking.entity.Transaction;

public class TransactionMapper {
    public static TransactionDto mapToTransactionDto(Transaction transaction){
        TransactionDto transactionDto = new TransactionDto(transaction.getId(), transaction.getAccountId(), transaction.getAmount(), transaction.getTransactionType(), transaction.getTimestamp());

     
        return transactionDto;
    }
}
