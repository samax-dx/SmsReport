package com.telcobright.SmsReport.Models;

public class BalanceUpdateError {
    String errorMessage;
    Double requestedAmount;

    public BalanceUpdateError(String errorMessage, Double requestedAmount) {
        this.errorMessage = errorMessage;
        this.requestedAmount = requestedAmount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
}
