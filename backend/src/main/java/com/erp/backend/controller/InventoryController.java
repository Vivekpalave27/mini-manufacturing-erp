package com.erp.backend.controller;

import com.erp.backend.dto.InventoryResponseDTO;
import com.erp.backend.service.InventoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // VIEW INVENTORY (ADMIN + STAFF)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {

        return ResponseEntity.ok(inventoryService.getAllInventory());
    }
}
