package com.erp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.backend.entity.SalesOrderItem;

public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, Long> {

}
