package com.erp.backend.controller;

import com.erp.backend.dto.GRRRequestDTO;
import com.erp.backend.dto.GRRResponseDTO;
import com.erp.backend.service.GRRService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

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

        return ResponseEntity.ok(grrService.createGRR(requestDTO));
    }
}
