package com.erp.backend.controller;

import com.erp.backend.dto.GRRRequestDTO;
import com.erp.backend.dto.GRRResponseDTO;
import com.erp.backend.service.GRRService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/grr")
public class GRRController {

    private final GRRService grrService;

    public GRRController(GRRService grrService) {
        this.grrService = grrService;
    }

    // 🔥 Create GRR (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRRResponseDTO> createGRR(
            @Valid @RequestBody GRRRequestDTO requestDTO) {

        GRRResponseDTO response = grrService.createGRR(requestDTO);
        return ResponseEntity.ok(response);
    }

    // 🔥 Receive GRR (Triggers Stock Increase)
    @PutMapping("/{id}/receive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> receiveGRR(@PathVariable Long id) {

        grrService.receiveGRR(id);

        return ResponseEntity.ok("GRR received successfully. Inventory updated.");
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<GRRResponseDTO>> getAllGRR() {
        return ResponseEntity.ok(grrService.getAllGRR());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<GRRResponseDTO> getGRRById(@PathVariable Long id) {
        return ResponseEntity.ok(grrService.getGRRById(id));
    }
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cancelGRR(@PathVariable Long id) {

        grrService.cancelGRR(id);

        return ResponseEntity.ok("GRR cancelled successfully.");
    }


}
