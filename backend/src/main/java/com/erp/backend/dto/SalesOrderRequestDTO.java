package com.erp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class SalesOrderRequestDTO {

    @NotBlank
    private String customerName;

    @NotEmpty
    private List<SalesOrderItemRequestDTO> items;

    public SalesOrderRequestDTO() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<SalesOrderItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<SalesOrderItemRequestDTO> items) {
        this.items = items;
    }
}
