package com.erp.backend.controller;

import com.erp.backend.dto.PurchaseBillRequestDTO;
import com.erp.backend.dto.PurchaseBillResponseDTO;
import com.erp.backend.service.PurchaseBillService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-bills")
public class PurchaseBillController {

    @Autowired
    private PurchaseBillService purchaseBillService;

    // ============================
    // CREATE PURCHASE BILL
    // ============================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseBillResponseDTO> createPurchaseBill(
            @Valid @RequestBody PurchaseBillRequestDTO request) {

        return ResponseEntity.ok(
                purchaseBillService.createPurchaseBill(request)
        );
    }

    // ============================
    // GET ALL PURCHASE BILLS
    // ============================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<PurchaseBillResponseDTO>> getAllBills() {

        return ResponseEntity.ok(
                purchaseBillService.getAllBills()
        );
    }

    // ============================
    // MARK BILL AS PAID
    // ============================
    @PutMapping("/{id}/pay")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseBillResponseDTO> markBillAsPaid(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                purchaseBillService.markBillAsPaid(id)
        );
    }
}
