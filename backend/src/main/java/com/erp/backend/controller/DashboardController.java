package com.erp.backend.controller;

import com.erp.backend.dto.DashboardSummaryResponseDTO;
import com.erp.backend.service.DashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<DashboardSummaryResponseDTO> getDashboardSummary() {

        DashboardSummaryResponseDTO response =
                dashboardService.getDashboardSummary();

        return ResponseEntity.ok(response);
    }
}