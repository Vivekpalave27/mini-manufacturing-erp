package com.erp.backend.dto;

import java.time.LocalDate;
import java.util.List;

public class PurchaseOrderResponseDTO {

    private Long id;
    private String poNumber;
    private String supplierName;
    private String status;
    private LocalDate orderDate;
    private Double totalAmount;
    private List<PurchaseOrderItemResponseDTO> items;

    public PurchaseOrderResponseDTO(Long id,
                                    String poNumber,
                                    String supplierName,
                                    String status,
                                    LocalDate orderDate,
                                    Double totalAmount,
                                    List<PurchaseOrderItemResponseDTO> items) {
        this.id = id;
        this.poNumber = poNumber;
        this.supplierName = supplierName;
        this.status = status;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = items;
    }

	public Long getId() {
		return id;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public String getStatus() {
		return status;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public List<PurchaseOrderItemResponseDTO> getItems() {
		return items;
	}

    
    // Getters
}
