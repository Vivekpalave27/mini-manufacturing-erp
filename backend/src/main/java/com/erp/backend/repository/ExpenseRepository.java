package com.erp.backend.repository;

import com.erp.backend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	@Query("SELECT SUM(e.amount) FROM Expense e")
	BigDecimal getTotalExpenseAmount();
	@Query("SELECT SUM(e.amount) FROM Expense e " +
		       "WHERE e.createdAt BETWEEN :startDate AND :endDate")
		BigDecimal getExpenseAmountBetweenDates(
		        @Param("startDate") LocalDateTime startDate,
		        @Param("endDate") LocalDateTime endDate);
	@Query("SELECT COUNT(e) FROM Expense e " +
		       "WHERE e.createdAt BETWEEN :startDate AND :endDate")
		Long countExpenseBetweenDates(
		        @Param("startDate") LocalDateTime startDate,
		        @Param("endDate") LocalDateTime endDate);
}