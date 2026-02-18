package com.erp.backend.dto;

import java.util.List;

public class GRRRequestDTO {

    private Long purchaseOrderId;
    private List<GRRItemRequestDTO> items;

    // Getters and Setters
    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public List<GRRItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<GRRItemRequestDTO> items) {
        this.items = items;
    }
}
