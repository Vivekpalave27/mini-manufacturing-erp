package com.erp.backend.service;

import com.erp.backend.dto.SalesReportResponseDTO;
import com.erp.backend.dto.PurchaseReportResponseDTO;
import com.erp.backend.dto.ExpenseReportResponseDTO;

import com.erp.backend.repository.SalesOrderRepository;
import com.erp.backend.repository.PurchaseOrderRepository;
import com.erp.backend.repository.ExpenseRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public SalesReportResponseDTO getSalesReport(LocalDateTime startDate,
                                                 LocalDateTime endDate) {

        BigDecimal totalAmount =
                salesOrderRepository.getSalesAmountBetweenDates(startDate, endDate);

        Long totalOrders =
                salesOrderRepository.countSalesBetweenDates(startDate, endDate);

        SalesReportResponseDTO response = new SalesReportResponseDTO();

        response.setTotalSalesAmount(
                totalAmount != null ? totalAmount : BigDecimal.ZERO
        );

        response.setTotalOrders(
                totalOrders != null ? totalOrders : 0L
        );

        response.setStartDate(startDate);
        response.setEndDate(endDate);

        return response;
    }

    // ================= PURCHASE REPORT =================

    public PurchaseReportResponseDTO getPurchaseReport(LocalDateTime startDate,
                                                       LocalDateTime endDate) {

        BigDecimal totalAmount =
                purchaseOrderRepository.getPurchaseAmountBetweenDates(startDate, endDate);

        Long totalOrders =
                purchaseOrderRepository.countPurchaseBetweenDates(startDate, endDate);

        PurchaseReportResponseDTO response = new PurchaseReportResponseDTO();

        response.setTotalPurchaseAmount(
                totalAmount != null ? totalAmount : BigDecimal.ZERO
        );

        response.setTotalOrders(
                totalOrders != null ? totalOrders : 0L
        );

        response.setStartDate(startDate);
        response.setEndDate(endDate);

        return response;
    }

    // ================= EXPENSE REPORT =================

    public ExpenseReportResponseDTO getExpenseReport(LocalDateTime startDate,
                                                     LocalDateTime endDate) {

        BigDecimal totalAmount =
                expenseRepository.getExpenseAmountBetweenDates(startDate, endDate);

        Long totalExpenses =
                expenseRepository.countExpenseBetweenDates(startDate, endDate);

        ExpenseReportResponseDTO response = new ExpenseReportResponseDTO();

        response.setTotalExpenseAmount(
                totalAmount != null ? totalAmount : BigDecimal.ZERO
        );

        response.setTotalExpenses(
                totalExpenses != null ? totalExpenses : 0L
        );

        response.setStartDate(startDate);
        response.setEndDate(endDate);

        return response;
    }
}