package com.erp.backend.service;

import com.erp.backend.dto.DashboardSummaryResponseDTO;
import com.erp.backend.repository.SalesOrderRepository;
import com.erp.backend.repository.PurchaseOrderRepository;
import com.erp.backend.repository.ExpenseRepository;
import com.erp.backend.repository.ItemRepository;
import com.erp.backend.repository.SupplierRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {

    private final SalesOrderRepository salesOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ExpenseRepository expenseRepository;
    private final ItemRepository itemRepository;
    private final SupplierRepository supplierRepository;

    public DashboardService(SalesOrderRepository salesOrderRepository,
                            PurchaseOrderRepository purchaseOrderRepository,
                            ExpenseRepository expenseRepository,
                            ItemRepository itemRepository,
                            SupplierRepository supplierRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.expenseRepository = expenseRepository;
        this.itemRepository = itemRepository;
        this.supplierRepository = supplierRepository;
    }

    public DashboardSummaryResponseDTO getDashboardSummary() {

        DashboardSummaryResponseDTO response = new DashboardSummaryResponseDTO();

        // ================= FINANCIAL SUMMARY =================

        BigDecimal totalSales = salesOrderRepository.getTotalSalesAmount();
        BigDecimal totalPurchase = purchaseOrderRepository.getTotalPurchaseAmount();
        BigDecimal totalExpense = expenseRepository.getTotalExpenseAmount();

        response.setTotalSalesAmount(
                totalSales != null ? totalSales : BigDecimal.ZERO
        );

        response.setTotalPurchaseAmount(
                totalPurchase != null ? totalPurchase : BigDecimal.ZERO
        );

        response.setTotalExpenseAmount(
                totalExpense != null ? totalExpense : BigDecimal.ZERO
        );

        // ================= MASTER COUNTS =================

        response.setTotalItems(itemRepository.count());
        response.setTotalSuppliers(supplierRepository.count());
        response.setTotalSalesOrders(salesOrderRepository.count());
        response.setTotalPurchaseOrders(purchaseOrderRepository.count());

        // ================= INVENTORY INTELLIGENCE =================

        Long lowStockCount = itemRepository.countLowStockItems();

        response.setLowStockItemsCount(
                lowStockCount != null ? lowStockCount : 0L
        );

        return response;
    }
}