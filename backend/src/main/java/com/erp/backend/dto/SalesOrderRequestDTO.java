package com.erp.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public class SalesOrderRequestDTO {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<SalesOrderItemRequestDTO> items;

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<SalesOrderItemRequestDTO> getItems() { return items; }
    public void setItems(List<SalesOrderItemRequestDTO> items) { this.items = items; }
}