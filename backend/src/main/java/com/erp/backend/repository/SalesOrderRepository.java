package com.erp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.backend.entity.SalesOrder;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.repository.query.Param;
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
	@Query("SELECT SUM(s.totalAmount) FROM SalesOrder s WHERE s.status = 'CONFIRMED'")
	BigDecimal getTotalSalesAmount();
	@Query("SELECT SUM(s.totalAmount) FROM SalesOrder s " +
		       "WHERE s.status = 'CONFIRMED' " +
		       "AND s.createdAt BETWEEN :startDate AND :endDate")
		BigDecimal getSalesAmountBetweenDates(
		        @Param("startDate") LocalDateTime startDate,
		        @Param("endDate") LocalDateTime endDate);
	@Query("SELECT COUNT(s) FROM SalesOrder s " +
		       "WHERE s.status = 'CONFIRMED' " +
		       "AND s.createdAt BETWEEN :startDate AND :endDate")
		Long countSalesBetweenDates(
		        @Param("startDate") LocalDateTime startDate,
		        @Param("endDate") LocalDateTime endDate);
}
