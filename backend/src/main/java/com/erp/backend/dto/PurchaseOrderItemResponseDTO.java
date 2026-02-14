package com.erp.backend.dto;

public class PurchaseOrderItemResponseDTO {

    private String itemName;
    private Integer quantity;
    private Double unitPrice;
    private Double subTotal;

    public PurchaseOrderItemResponseDTO(String itemName,
                                        Integer quantity,
                                        Double unitPrice,
                                        Double subTotal) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
    }

	public String getItemName() {
		return itemName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public Double getSubTotal() {
		return subTotal;
	}

    // Getters
    
}
