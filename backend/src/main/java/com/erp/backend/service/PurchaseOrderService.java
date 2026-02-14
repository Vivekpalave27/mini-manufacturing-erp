package com.erp.backend.service;

import com.erp.backend.dto.*;
import com.erp.backend.entity.*;
import com.erp.backend.exception.ResourceNotFoundException;
import com.erp.backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ItemRepository itemRepository;

    // ============================
    // CREATE PURCHASE ORDER
    // ============================
    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO request) {

        // 1️⃣ Validate Supplier
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found"));

        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setOrderDate(LocalDate.now());
        purchaseOrder.setCreatedAt(LocalDateTime.now());
        purchaseOrder.setStatus(PurchaseOrderStatus.CREATED);

        // Generate simple PO number
        purchaseOrder.setPoNumber("PO-" + UUID.randomUUID().toString().substring(0, 8));

        List<PurchaseOrderItem> poItems = new ArrayList<>();

        double totalAmount = 0;

        // 2️⃣ Process Each Item
        for (PurchaseOrderItemRequestDTO itemDTO : request.getItems()) {

            Item item = itemRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Item not found"));

            PurchaseOrderItem poItem = new PurchaseOrderItem();
            poItem.setPurchaseOrder(purchaseOrder);
            poItem.setItem(item);
            poItem.setQuantity(itemDTO.getQuantity());
            poItem.setUnitPrice(itemDTO.getUnitPrice());

            double subTotal = itemDTO.getQuantity() * itemDTO.getUnitPrice();
            poItem.setSubTotal(subTotal);

            totalAmount += subTotal;

            poItems.add(poItem);
        }

        purchaseOrder.setItems(poItems);
        purchaseOrder.setTotalAmount(totalAmount);

        PurchaseOrder savedPO = purchaseOrderRepository.save(purchaseOrder);

        return mapToResponse(savedPO);
    }

    // ============================
    // GET ALL PURCHASE ORDERS
    // ============================
    public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public PurchaseOrderResponseDTO approvePurchaseOrder(Long id) {

        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Purchase Order not found"));

        po.setStatus(PurchaseOrderStatus.APPROVED);

        PurchaseOrder updated = purchaseOrderRepository.save(po);

        return mapToResponse(updated);
    }

    // ============================
    // MAPPING METHOD
    // ============================
    private PurchaseOrderResponseDTO mapToResponse(PurchaseOrder po) {

        List<PurchaseOrderItemResponseDTO> itemResponses =
                po.getItems().stream()
                        .map(item -> new PurchaseOrderItemResponseDTO(
                                item.getItem().getName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getSubTotal()
                        ))
                        .collect(Collectors.toList());

        return new PurchaseOrderResponseDTO(
                po.getId(),
                po.getPoNumber(),
                po.getSupplier().getName(),
                po.getStatus().name(),
                po.getOrderDate(),
                po.getTotalAmount(),
                itemResponses
        );
    }
}
