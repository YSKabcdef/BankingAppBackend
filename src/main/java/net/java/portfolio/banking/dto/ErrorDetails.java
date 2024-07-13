package net.java.portfolio.banking.dto;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime timestamp, String message, String details, String errorCode) {

}
