package com.erp.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "grr")
public class GRR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grr_number", nullable = false, unique = true)
    private String grrNumber;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @Column(name = "received_date", nullable = false)
    private LocalDate receivedDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 🔥 Add totalAmount
    @Column(name = "total_amount")
    private Double totalAmount;

    // 🔥 Add GRR Status
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GRRStatus status;

    // 🔥 Add OneToMany relationship
    @OneToMany(mappedBy = "grr", cascade = CascadeType.ALL)
    private List<GRRItem> items;

    // Constructors
    public GRR() {
        this.grrNumber = "GRR-" + UUID.randomUUID();
        this.receivedDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        this.status = GRRStatus.CREATED;
    }

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

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public LocalDate getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDate receivedDate) {
		this.receivedDate = receivedDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public GRRStatus getStatus() {
		return status;
	}

	public void setStatus(GRRStatus status) {
		this.status = status;
	}

	public List<GRRItem> getItems() {
		return items;
	}

	public void setItems(List<GRRItem> items) {
		this.items = items;
	}

    // Getters and Setters
    // (Generate automatically in IDE)
    
}
