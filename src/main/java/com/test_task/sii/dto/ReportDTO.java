package com.test_task.sii.dto;

import java.math.BigDecimal;

public class ReportDTO {
    private String gymName;
    private BigDecimal amount;
    private String currency;

    public ReportDTO(String gymName, BigDecimal amount, String currency) {
        this.gymName = gymName;
        this.amount = amount;
        this.currency = currency;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
