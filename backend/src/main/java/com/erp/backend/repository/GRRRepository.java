package com.erp.backend.repository;

import com.erp.backend.entity.GRR;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GRRRepository extends JpaRepository<GRR, Long> {

    // 🔥 To prevent duplicate GRR for same Purchase Order
    Optional<GRR> findByPurchaseOrderId(Long purchaseOrderId);
}
