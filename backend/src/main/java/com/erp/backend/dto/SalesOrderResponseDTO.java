package com.erp.backend.dto;

import java.util.List;

public class SalesOrderResponseDTO {

    private String soNumber;
    private String customerName;
    private String status;
    private Double totalAmount;
    private List<SalesOrderItemResponseDTO> items;

    public SalesOrderResponseDTO() {
    }

    public String getSoNumber() {
        return soNumber;
    }

    public void setSoNumber(String soNumber) {
        this.soNumber = soNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SalesOrderItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<SalesOrderItemResponseDTO> items) {
        this.items = items;
    }
}
