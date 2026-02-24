package com.erp.backend.service;

import com.erp.backend.dto.*;
import com.erp.backend.entity.*;
import com.erp.backend.exception.BusinessException;
import com.erp.backend.exception.ResourceNotFoundException;
import com.erp.backend.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final ItemRepository itemRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
                                SupplierRepository supplierRepository,
                                ItemRepository itemRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.supplierRepository = supplierRepository;
        this.itemRepository = itemRepository;
    }

    // ==========================
    // CREATE PURCHASE ORDER
    // ==========================
    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO request) {

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with id: " + request.getSupplierId()));

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setOrderDate(LocalDate.now());
        purchaseOrder.setCreatedAt(LocalDateTime.now());
        purchaseOrder.setStatus(PurchaseOrderStatus.CREATED);
        purchaseOrder.setPoNumber("PO-" + UUID.randomUUID().toString().substring(0, 8));

        List<PurchaseOrderItem> poItems = request.getItems()
                .stream()
                .map(itemDTO -> {

                    Item item = itemRepository.findById(itemDTO.getItemId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Item not found with id: " + itemDTO.getItemId()));

                    PurchaseOrderItem poItem = new PurchaseOrderItem();
                    poItem.setPurchaseOrder(purchaseOrder);
                    poItem.setItem(item);
                    poItem.setQuantity(itemDTO.getQuantity());
                    poItem.setUnitPrice(itemDTO.getUnitPrice());

                    double subTotal =
                            itemDTO.getQuantity() * itemDTO.getUnitPrice();

                    poItem.setSubTotal(subTotal);

                    return poItem;
                })
                .collect(Collectors.toList());

        double totalAmount = poItems.stream()
                .mapToDouble(PurchaseOrderItem::getSubTotal)
                .sum();

        purchaseOrder.setItems(poItems);
        purchaseOrder.setTotalAmount(totalAmount);

        PurchaseOrder savedPO = purchaseOrderRepository.save(purchaseOrder);

        return mapToResponse(savedPO);
    }

    // ==========================
    // APPROVE PURCHASE ORDER
    // ==========================
    public PurchaseOrderResponseDTO approvePurchaseOrder(Long id) {

        PurchaseOrder po = getPurchaseOrderById(id);

        validateNotAlreadyApproved(po);

        po.setStatus(PurchaseOrderStatus.APPROVED);

        return mapToResponse(po);
    }

    // ==========================
    // GET ALL PURCHASE ORDERS
    // ==========================
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==========================
    // PRIVATE HELPERS
    // ==========================
    private PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Purchase Order not found with id: " + id));
    }

    private void validateNotAlreadyApproved(PurchaseOrder po) {
        if (po.getStatus() == PurchaseOrderStatus.APPROVED) {
            throw new BusinessException(
                    "Purchase Order already approved: " + po.getPoNumber());
        }
    }

    private PurchaseOrderResponseDTO mapToResponse(PurchaseOrder po) {

        List<PurchaseOrderItemResponseDTO> itemResponses =
                po.getItems()
                        .stream()
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