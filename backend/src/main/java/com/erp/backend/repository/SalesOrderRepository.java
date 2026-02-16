package com.erp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.backend.entity.SalesOrder;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

}
