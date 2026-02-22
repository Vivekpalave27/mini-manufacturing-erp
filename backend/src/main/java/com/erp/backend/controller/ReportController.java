package com.erp.backend.controller;

import com.erp.backend.dto.SalesReportResponseDTO;
import com.erp.backend.dto.PurchaseReportResponseDTO;
import com.erp.backend.dto.ExpenseReportResponseDTO;
import com.erp.backend.service.ReportService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ================= SALES REPORT =================

    @GetMapping("/sales")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public SalesReportResponseDTO getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {

        return reportService.getSalesReport(startDate, endDate);
    }

    // ================= PURCHASE REPORT =================

    @GetMapping("/purchase")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public PurchaseReportResponseDTO getPurchaseReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {

        return reportService.getPurchaseReport(startDate, endDate);
    }

    // ================= EXPENSE REPORT =================

    @GetMapping("/expenses")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ExpenseReportResponseDTO getExpenseReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {

        return reportService.getExpenseReport(startDate, endDate);
    }
}