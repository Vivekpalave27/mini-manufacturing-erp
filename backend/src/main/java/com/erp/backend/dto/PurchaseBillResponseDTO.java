package com.erp.backend.dto;

import java.time.LocalDate;

public class PurchaseBillResponseDTO {

    private Long id;
    private String billNumber;
    private String supplierName;
    private Double totalAmount;
    private String status;
    private LocalDate billDate;

    public PurchaseBillResponseDTO(
            Long id,
            String billNumber,
            String supplierName,
            Double totalAmount,
            String status,
            LocalDate billDate) {

        this.id = id;
        this.billNumber = billNumber;
        this.supplierName = supplierName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.billDate = billDate;
    }

    public Long getId() {
        return id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getBillDate() {
        return billDate;
    }
}
