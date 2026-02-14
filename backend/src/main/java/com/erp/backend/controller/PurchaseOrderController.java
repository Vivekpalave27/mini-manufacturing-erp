package com.erp.backend.controller;

import com.erp.backend.dto.PurchaseOrderRequestDTO;
import com.erp.backend.dto.PurchaseOrderResponseDTO;
import com.erp.backend.service.PurchaseOrderService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    // ============================
    // CREATE PURCHASE ORDER
    // ============================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrderResponseDTO> createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderRequestDTO request) {

        return ResponseEntity.ok(
                purchaseOrderService.createPurchaseOrder(request)
        );
    }

    // ============================
    // GET ALL PURCHASE ORDERS
    // ============================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<PurchaseOrderResponseDTO>> getAllPurchaseOrders() {

        return ResponseEntity.ok(
                purchaseOrderService.getAllPurchaseOrders()
        );
    }

    // ============================
    // APPROVE PURCHASE ORDER
    // ============================
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrderResponseDTO> approvePurchaseOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                purchaseOrderService.approvePurchaseOrder(id)
        );
    }
}
