package com.erp.backend.controller;

import com.erp.backend.dto.*;
import com.erp.backend.service.SalesOrderService;
import com.erp.backend.entity.SalesOrder;
import com.erp.backend.entity.SalesOrderStatus;
import com.erp.backend.repository.SalesOrderRepository;
import com.erp.backend.exception.ResourceNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;
    private final SalesOrderRepository salesOrderRepository;

    public SalesOrderController(SalesOrderService salesOrderService,
                                SalesOrderRepository salesOrderRepository) {
        this.salesOrderService = salesOrderService;
        this.salesOrderRepository = salesOrderRepository;
    }

    // ✅ Create Sales Order (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalesOrderResponseDTO> createSalesOrder(
            @Valid @RequestBody SalesOrderRequestDTO requestDTO) {

        return ResponseEntity.ok(salesOrderService.createSalesOrder(requestDTO));
    }

    // ✅ Get All Sales Orders (ADMIN + STAFF)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<SalesOrderResponseDTO>> getAllSalesOrders() {
        return ResponseEntity.ok(salesOrderService.getAllSalesOrders());
    }

    // ✅ Confirm Sales Order (ADMIN only)
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalesOrderResponseDTO> confirmOrder(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.confirmOrder(id));
    }
}
