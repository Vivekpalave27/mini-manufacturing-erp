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
import java.util.stream.Collectors;

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

        List<SalesOrderResponseDTO> response =
                salesOrderRepository.findAll()
                        .stream()
                        .map(order -> salesOrderService.createSalesOrder(
                                mapToRequest(order)))  // simple mapping workaround
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ✅ Confirm Sales Order (ADMIN only)
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> confirmSalesOrder(@PathVariable Long id) {

        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));

        order.setStatus(SalesOrderStatus.CONFIRMED);
        salesOrderRepository.save(order);

        return ResponseEntity.ok("Sales Order confirmed successfully");
    }

    // Simple mapper for getAll workaround
    private SalesOrderRequestDTO mapToRequest(SalesOrder order) {
        SalesOrderRequestDTO dto = new SalesOrderRequestDTO();
        dto.setCustomerName(order.getCustomerName());
        return dto;
    }
}
