package com.erp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.backend.entity.SalesOrder;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
	@Query("SELECT SUM(s.totalAmount) FROM SalesOrder s WHERE s.status = 'CONFIRMED'")
	BigDecimal getTotalSalesAmount();
}
