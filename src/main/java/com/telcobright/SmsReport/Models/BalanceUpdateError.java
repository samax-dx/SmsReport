package com.telcobright.SmsReport.Models;

public class BalanceUpdateError {
    String errorMessage;
    double requestedAmount;

    public BalanceUpdateError(String errorMessage, double requestedAmount) {
        this.errorMessage = errorMessage;
        this.requestedAmount = requestedAmount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
}
