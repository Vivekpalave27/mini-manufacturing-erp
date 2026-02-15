package com.erp.backend.dto;

import java.time.LocalDate;

public class PurchaseBillRequestDTO {

    private Long purchaseOrderId;
    private LocalDate billDate;

    public PurchaseBillRequestDTO() {}

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }
}
