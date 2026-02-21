package com.erp.backend.dto;

import java.math.BigDecimal;

public class DashboardSummaryResponseDTO {

    // Financial Summary
    private BigDecimal totalSalesAmount;
    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalExpenseAmount;

    // Master Counts
    private Long totalItems;
    private Long totalSuppliers;
    private Long totalSalesOrders;
    private Long totalPurchaseOrders;

    // Inventory Intelligence
    private Long lowStockItemsCount;

    // Constructors
    public DashboardSummaryResponseDTO() {
    }

    // Getters and Setters

    public BigDecimal getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public void setTotalSalesAmount(BigDecimal totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }

    public BigDecimal getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public BigDecimal getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(BigDecimal totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public Long getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(Long totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }

    public Long getTotalSalesOrders() {
        return totalSalesOrders;
    }

    public void setTotalSalesOrders(Long totalSalesOrders) {
        this.totalSalesOrders = totalSalesOrders;
    }

    public Long getTotalPurchaseOrders() {
        return totalPurchaseOrders;
    }

    public void setTotalPurchaseOrders(Long totalPurchaseOrders) {
        this.totalPurchaseOrders = totalPurchaseOrders;
    }

    public Long getLowStockItemsCount() {
        return lowStockItemsCount;
    }

    public void setLowStockItemsCount(Long lowStockItemsCount) {
        this.lowStockItemsCount = lowStockItemsCount;
    }
}