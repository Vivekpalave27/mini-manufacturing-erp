package com.erp.backend.service;

import com.erp.backend.dto.PurchaseBillRequestDTO;
import com.erp.backend.dto.PurchaseBillResponseDTO;
import com.erp.backend.entity.*;
import com.erp.backend.exception.ResourceNotFoundException;
import com.erp.backend.repository.PurchaseBillRepository;
import com.erp.backend.repository.PurchaseOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseBillService {

    @Autowired
    private PurchaseBillRepository purchaseBillRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    // ============================
    // CREATE PURCHASE BILL
    // ============================
    public PurchaseBillResponseDTO createPurchaseBill(PurchaseBillRequestDTO request) {

        // 1️⃣ Validate Purchase Order
        PurchaseOrder po = purchaseOrderRepository.findById(request.getPurchaseOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Purchase Order not found"));

        // 2️⃣ Ensure PO is APPROVED
        if (po.getStatus() != PurchaseOrderStatus.APPROVED) {
            throw new RuntimeException("Purchase Order must be APPROVED before billing");
        }

        // 3️⃣ Ensure Bill not already created
        if (purchaseBillRepository.findByPurchaseOrder(po).isPresent()) {
            throw new RuntimeException("Purchase Bill already exists for this Purchase Order");
        }

        PurchaseBill bill = new PurchaseBill();

        bill.setPurchaseOrder(po);
        bill.setBillDate(request.getBillDate());
        bill.setCreatedAt(LocalDateTime.now());
        bill.setStatus(PurchaseBillStatus.CREATED);

        // Copy total from PO
        bill.setTotalAmount(po.getTotalAmount());

        // Generate Bill Number
        bill.setBillNumber("PB-" + UUID.randomUUID().toString().substring(0, 8));

        PurchaseBill savedBill = purchaseBillRepository.save(bill);

        return mapToResponse(savedBill);
    }

    // ============================
    // MARK BILL AS PAID
    // ============================
    public PurchaseBillResponseDTO markBillAsPaid(Long id) {

        PurchaseBill bill = purchaseBillRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Purchase Bill not found"));

        bill.setStatus(PurchaseBillStatus.PAID);

        PurchaseBill updated = purchaseBillRepository.save(bill);

        return mapToResponse(updated);
    }

    // ============================
    // GET ALL BILLS
    // ============================
    public List<PurchaseBillResponseDTO> getAllBills() {
        return purchaseBillRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ============================
    // MAPPING METHOD
    // ============================
    private PurchaseBillResponseDTO mapToResponse(PurchaseBill bill) {

        return new PurchaseBillResponseDTO(
                bill.getId(),
                bill.getBillNumber(),
                bill.getPurchaseOrder().getSupplier().getName(),
                bill.getTotalAmount(),
                bill.getStatus().name(),
                bill.getBillDate()
        );
    }
}
