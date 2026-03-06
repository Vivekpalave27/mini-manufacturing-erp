package com.erp.backend.service;

import com.erp.backend.dto.SalesReportResponseDTO;
import com.erp.backend.dto.PurchaseReportResponseDTO;
import com.erp.backend.dto.ExpenseReportResponseDTO;

import com.erp.backend.repository.SalesOrderRepository;
import com.erp.backend.repository.PurchaseOrderRepository;
import com.erp.backend.repository.ExpenseRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReportService {

    private final SalesOrderRepository salesOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ExpenseRepository expenseRepository;

    public ReportService(SalesOrderRepository salesOrderRepository,
                         PurchaseOrderRepository purchaseOrderRepository,
                         ExpenseRepository expenseRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.expenseRepository = expenseRepository;
    }

    // ================= SALES REPORT =================

    public SalesReportResponseDTO getSalesReport(LocalDate startDate,
                                                 LocalDate endDate) {

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        BigDecimal totalAmount =
                salesOrderRepository.getSalesAmountBetweenDates(start, end);

        Long totalOrders =
                salesOrderRepository.countSalesBetweenDates(start, end);

        SalesReportResponseDTO response = new SalesReportResponseDTO();

        response.setTotalSalesAmount(
                totalAmount != null ? totalAmount : BigDecimal.ZERO
        );

        response.setTotalOrders(
                totalOrders != null ? totalOrders : 0L
        );

        response.setStartDate(start);
        response.setEndDate(end);

        return response;
    }

    // ================= PURCHASE REPORT =================

    public PurchaseReportResponseDTO getPurchaseReport(LocalDate startDate,
                                                       LocalDate endDate) {

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        BigDecimal totalAmount =
                purchaseOrderRepository.getPurchaseAmountBetweenDates(start, end);

        Long totalOrders =
                purchaseOrderRepository.countPurchaseBetweenDates(start, end);

        PurchaseReportResponseDTO response = new PurchaseReportResponseDTO();

        response.setTotalPurchaseAmount(
                totalAmount != null ? totalAmount : BigDecimal.ZERO
        );

        response.setTotalOrders(
                totalOrders != null ? totalOrders : 0L
        );

        response.setStartDate(start);
        response.setEndDate(end);

        return response;
    }

    // ================= EXPENSE REPORT =================

    public ExpenseReportResponseDTO getExpenseReport(LocalDate startDate,
                                                     LocalDate endDate) {

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        BigDecimal totalAmount =
                expenseRepository.getExpenseAmountBetweenDates(start, end);

        Long totalExpenses =
                expenseRepository.countExpenseBetweenDates(start, end);

        ExpenseReportResponseDTO response = new ExpenseReportResponseDTO();

        response.setTotalExpenseAmount(
                totalAmount != null ? totalAmount : BigDecimal.ZERO
        );

        response.setTotalExpenses(
                totalExpenses != null ? totalExpenses : 0L
        );

        response.setStartDate(start);
        response.setEndDate(end);

        return response;
    }
}