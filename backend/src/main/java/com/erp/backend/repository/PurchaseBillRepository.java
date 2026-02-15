package com.erp.backend.repository;

import com.erp.backend.entity.PurchaseBill;
import com.erp.backend.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseBillRepository 
        extends JpaRepository<PurchaseBill, Long> {

    Optional<PurchaseBill> findByPurchaseOrder(PurchaseOrder purchaseOrder);

}
