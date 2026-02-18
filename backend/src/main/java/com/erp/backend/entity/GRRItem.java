package com.erp.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grr_items")
public class GRRItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Link to GRR
    @ManyToOne
    @JoinColumn(name = "grr_id", nullable = false)
    private GRR grr;

    // 🔥 Link to Item
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // Quantity received
    @Column(nullable = false)
    private Integer quantity;

    // 🔥 Unit price copied from Purchase Order
    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    // 🔥 Subtotal = quantity × unitPrice
    @Column(name = "sub_total", nullable = false)
    private Double subTotal;

    // --------------------
    // Constructors
    // --------------------
    public GRRItem() {}

    // --------------------
    // Getters and Setters
    // --------------------

    public Long getId() {
        return id;
    }

    public GRR getGrr() {
        return grr;
    }

    public void setGrr(GRR grr) {
        this.grr = grr;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
