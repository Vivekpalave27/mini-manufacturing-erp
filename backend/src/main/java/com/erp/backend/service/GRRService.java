package com.erp.backend.service;

import com.erp.backend.dto.*;
import com.erp.backend.entity.*;
import com.erp.backend.exception.BusinessException;
import com.erp.backend.exception.DuplicateResourceException;
import com.erp.backend.exception.InvalidOperationException;
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
public class GRRService {

    private final GRRRepository grrRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ItemRepository itemRepository;

    public GRRService(GRRRepository grrRepository,
                      PurchaseOrderRepository purchaseOrderRepository,
                      ItemRepository itemRepository) {
        this.grrRepository = grrRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.itemRepository = itemRepository;
    }

    // ==========================
    // CREATE GRR
    // ==========================
    public GRRResponseDTO createGRR(GRRRequestDTO requestDTO) {

        PurchaseOrder purchaseOrder =
                getApprovedPurchaseOrder(requestDTO.getPurchaseOrderId());

        validateNoDuplicateGRR(purchaseOrder.getId());

        GRR grr = new GRR();

        grr.setPurchaseOrder(purchaseOrder);

        // ✅ EXPLICITLY SET ALL NOT NULL FIELDS
        grr.setGrrNumber("GRR-" + UUID.randomUUID());
        grr.setReceivedDate(LocalDate.now());
        grr.setCreatedAt(LocalDateTime.now());
        grr.setStatus(GRRStatus.CREATED);

        List<GRRItem> grrItems = requestDTO.getItems()
                .stream()
                .map(itemDTO -> {

                    Item item = itemRepository.findById(itemDTO.getItemId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Item not found with id: "
                                                    + itemDTO.getItemId()));

                    GRRItem grrItem = new GRRItem();
                    grrItem.setGrr(grr);
                    grrItem.setItem(item);
                    grrItem.setQuantity(itemDTO.getQuantity());
                    grrItem.setUnitPrice(itemDTO.getUnitPrice());
                    grrItem.setSubTotal(
                            itemDTO.getQuantity() * itemDTO.getUnitPrice());

                    return grrItem;
                })
                .collect(Collectors.toList());

        double totalAmount = grrItems.stream()
                .mapToDouble(GRRItem::getSubTotal)
                .sum();

        grr.setItems(grrItems);
        grr.setTotalAmount(totalAmount);

        GRR savedGRR = grrRepository.save(grr);

        return mapToResponse(savedGRR);
    }

    // ==========================
    // RECEIVE GRR
    // ==========================
    public void receiveGRR(Long id) {

        GRR grr = grrRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRR not found"));

        // Prevent double receive
        if (grr.getStatus() == GRRStatus.RECEIVED) {
            throw new InvalidOperationException("GRR already received");
        }

        // 🔥 Update Inventory Stock
        for (GRRItem grrItem : grr.getItems()) {

            Item item = grrItem.getItem();

            // Increase stock
            item.setStockQty(item.getStockQty() + grrItem.getQuantity());

            itemRepository.save(item);
        }

        // Update GRR Status
        grr.setStatus(GRRStatus.RECEIVED);
        grrRepository.save(grr);
    }

    // ==========================
    // CANCEL GRR
    // ==========================
    public void cancelGRR(Long id) {

        GRR grr = getGRRByIdInternal(id);

        if (grr.getStatus() == GRRStatus.RECEIVED) {
            throw new BusinessException(
                    "Received GRR cannot be cancelled.");
        }

        if (grr.getStatus() == GRRStatus.CANCELLED) {
            throw new BusinessException(
                    "GRR already cancelled.");
        }

        grr.setStatus(GRRStatus.CANCELLED);
    }

    // ==========================
    // GET METHODS
    // ==========================
    @Transactional(readOnly = true)
    public List<GRRResponseDTO> getAllGRR() {
        return grrRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GRRResponseDTO getGRRById(Long id) {
        return mapToResponse(getGRRByIdInternal(id));
    }

    // ==========================
    // PRIVATE HELPERS
    // ==========================

    private PurchaseOrder getApprovedPurchaseOrder(Long id) {

        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Purchase Order not found with id: " + id));

        if (po.getStatus() != PurchaseOrderStatus.APPROVED) {
            throw new BusinessException(
                    "GRR can only be created for APPROVED Purchase Order");
        }

        return po;
    }

    private void validateNoDuplicateGRR(Long purchaseOrderId) {
        grrRepository.findByPurchaseOrderId(purchaseOrderId)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "GRR already exists for Purchase Order ID: "
                                    + purchaseOrderId);
                });
    }

    private GRR getGRRByIdInternal(Long id) {
        return grrRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "GRR not found with id: " + id));
    }

    private void validateReceivableStatus(GRR grr) {

        if (grr.getStatus() == GRRStatus.RECEIVED) {
            throw new InvalidOperationException(
                    "GRR already received. Stock already updated.");
        }

        if (grr.getStatus() == GRRStatus.CANCELLED) {
            throw new InvalidOperationException(
                    "Cancelled GRR cannot be received.");
        }
    }

    private void validateLinkedPOApproved(GRR grr) {

        if (grr.getPurchaseOrder().getStatus()
                != PurchaseOrderStatus.APPROVED) {
            throw new BusinessException(
                    "Cannot receive GRR. Purchase Order is not approved.");
        }
    }

    private void updateStock(GRR grr) {

        for (GRRItem grrItem : grr.getItems()) {

            Item item = grrItem.getItem();

            item.setStockQty(
                    item.getStockQty() + grrItem.getQuantity());

            itemRepository.save(item);
        }
    }

    private GRRResponseDTO mapToResponse(GRR grr) {

        GRRResponseDTO response = new GRRResponseDTO();
        response.setId(grr.getId());
        response.setPurchaseOrderId(
                grr.getPurchaseOrder().getId());
        response.setGrrNumber(grr.getGrrNumber());
        response.setStatus(grr.getStatus().name());
        response.setTotalAmount(grr.getTotalAmount());

        response.setItems(
                grr.getItems()
                        .stream()
                        .map(item -> {
                            GRRItemResponseDTO dto =
                                    new GRRItemResponseDTO();
                            dto.setItemId(item.getItem().getId());
                            dto.setItemName(item.getItem().getName());
                            dto.setQuantity(item.getQuantity());
                            dto.setUnitPrice(item.getUnitPrice());
                            dto.setSubTotal(item.getSubTotal());
                            return dto;
                        })
                        .collect(Collectors.toList())
        );

        return response;
    }
}