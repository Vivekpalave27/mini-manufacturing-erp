package com.erp.backend.repository;

import com.erp.backend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	@Query("SELECT SUM(e.amount) FROM Expense e")
	BigDecimal getTotalExpenseAmount();
}