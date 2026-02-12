package com.erp.backend.controller;

import com.erp.backend.dto.ItemRequestDTO;
import com.erp.backend.dto.ItemResponseDTO;
import com.erp.backend.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // ================================
    // CREATE ITEM (ADMIN ONLY)
    // ================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemResponseDTO> createItem(
            @RequestBody ItemRequestDTO request) {

        return ResponseEntity.ok(itemService.createItem(request));
    }

    // ================================
    // GET ALL ITEMS (ADMIN + STAFF)
    // ================================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {

        return ResponseEntity.ok(itemService.getAllItems());
    }

    // ================================
    // GET ITEM BY ID (ADMIN + STAFF)
    // ================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ItemResponseDTO> getItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(itemService.getItemById(id));
    }

    // ================================
    // UPDATE ITEM DETAILS (ADMIN ONLY)
    // NOTE: Stock is NOT updated here
    // Stock changes only via GRR & Sales Order
    // ================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemResponseDTO> updateItem(
            @PathVariable Long id,
            @RequestBody ItemRequestDTO request) {

        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    // ================================
    // DELETE ITEM (ADMIN ONLY)
    // ================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {

        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted successfully");
    }
}
