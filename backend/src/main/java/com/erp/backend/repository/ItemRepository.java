package com.erp.backend.repository;

import com.erp.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
public interface ItemRepository extends JpaRepository<Item, Long> {
	@Query("SELECT COUNT(i) FROM Item i WHERE i.stockQty < 10")
	Long countLowStockItems();
}
