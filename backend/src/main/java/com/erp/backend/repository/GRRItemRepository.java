package com.erp.backend.repository;

import com.erp.backend.entity.GRRItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GRRItemRepository extends JpaRepository<GRRItem, Long> {
}
