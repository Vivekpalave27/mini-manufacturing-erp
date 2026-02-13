package com.erp.backend.controller;

import com.erp.backend.dto.SupplierRequestDTO;
import com.erp.backend.dto.SupplierResponseDTO;
import com.erp.backend.service.SupplierService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // ================================
    // CREATE SUPPLIER (ADMIN ONLY)
    // ================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierResponseDTO> createSupplier(
            @Valid @RequestBody SupplierRequestDTO request) {

        return ResponseEntity.ok(supplierService.createSupplier(request));
    }

    // ================================
    // GET ALL SUPPLIERS (ADMIN + STAFF)
    // ================================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers() {

        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    // ================================
    // GET SUPPLIER BY ID (ADMIN + STAFF)
    // ================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(
            @PathVariable Long id) {

        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    // ================================
    // UPDATE SUPPLIER (ADMIN ONLY)
    // ================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierResponseDTO> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO request) {

        return ResponseEntity.ok(supplierService.updateSupplier(id, request));
    }

    // ================================
    // DELETE SUPPLIER (ADMIN ONLY)
    // ================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {

        supplierService.deleteSupplier(id);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}
