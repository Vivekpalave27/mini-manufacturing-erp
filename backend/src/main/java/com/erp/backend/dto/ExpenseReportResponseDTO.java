package com.erp.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpenseReportResponseDTO {

    private BigDecimal totalExpenseAmount;
    private Long totalExpenses;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ExpenseReportResponseDTO() {
    }

    public BigDecimal getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(BigDecimal totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public Long getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Long totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}