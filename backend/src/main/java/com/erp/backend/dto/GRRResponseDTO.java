package com.erp.backend.dto;

import java.util.List;

public class GRRResponseDTO {

    private Long id;
    private String grrNumber;
    private String status;
    private Double totalAmount;
    private List<GRRItemResponseDTO> items;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrrNumber() {
        return grrNumber;
    }

    public void setGrrNumber(String grrNumber) {
        this.grrNumber = grrNumber;
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

    public List<GRRItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<GRRItemResponseDTO> items) {
        this.items = items;
    }
}
