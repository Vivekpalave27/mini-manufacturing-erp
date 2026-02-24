package com.erp.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public class PurchaseOrderRequestDTO {

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<PurchaseOrderItemRequestDTO> items;

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public List<PurchaseOrderItemRequestDTO> getItems() { return items; }
    public void setItems(List<PurchaseOrderItemRequestDTO> items) { this.items = items; }
}