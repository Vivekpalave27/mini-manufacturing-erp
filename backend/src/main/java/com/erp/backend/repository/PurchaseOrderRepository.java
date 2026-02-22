package com.erp.backend.repository;

import com.erp.backend.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PurchaseOrderRepository 
        extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByPoNumber(String poNumber);
    @Query("SELECT SUM(p.totalAmount) FROM PurchaseOrder p WHERE p.status = 'APPROVED'")
    BigDecimal getTotalPurchaseAmount();
    @Query("SELECT SUM(p.totalAmount) FROM PurchaseOrder p " +
    	       "WHERE p.status = 'APPROVED' " +
    	       "AND p.createdAt BETWEEN :startDate AND :endDate")
    	BigDecimal getPurchaseAmountBetweenDates(
    	        @Param("startDate") LocalDateTime startDate,
    	        @Param("endDate") LocalDateTime endDate);
    @Query("SELECT COUNT(p) FROM PurchaseOrder p " +
    	       "WHERE p.status = 'APPROVED' " +
    	       "AND p.createdAt BETWEEN :startDate AND :endDate")
    	Long countPurchaseBetweenDates(
    	        @Param("startDate") LocalDateTime startDate,
    	        @Param("endDate") LocalDateTime endDate);
}
