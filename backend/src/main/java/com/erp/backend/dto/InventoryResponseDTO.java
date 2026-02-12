package com.erp.backend.dto;

public class InventoryResponseDTO {

    private Long itemId;
    private String name;
    private String sku;
    private Double price;
    private Integer stock;

    public InventoryResponseDTO(Long itemId, String name, String sku, Double price, Integer stock) {
        this.itemId = itemId;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.stock = stock;
    }

    public Long getItemId() { return itemId; }
    public String getName() { return name; }
    public String getSku() { return sku; }
    public Double getPrice() { return price; }
    public Integer getStock() { return stock; }
}
