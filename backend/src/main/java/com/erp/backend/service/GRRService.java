package com.erp.backend.service;

import com.erp.backend.dto.*;
import com.erp.backend.entity.*;
import com.erp.backend.repository.*;
import com.erp.backend.exception.BusinessException;
import com.erp.backend.exception.DuplicateResourceException;
import com.erp.backend.exception.InvalidOperationException;
import com.erp.backend.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GRRService {

    private final GRRRepository grrRepository;
    private final GRRItemRepository grrItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ItemRepository itemRepository;

    public GRRService(GRRRepository grrRepository,
                      GRRItemRepository grrItemRepository,
                      PurchaseOrderRepository purchaseOrderRepository,
                      ItemRepository itemRepository) {
        this.grrRepository = grrRepository;
        this.grrItemRepository = grrItemRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public GRRResponseDTO createGRR(GRRRequestDTO requestDTO) {

        // 1️⃣ Validate Purchase Order exists
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(requestDTO.getPurchaseOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found"));

        // 2️⃣ Validate PO status = APPROVED
        if (!purchaseOrder.getStatus().name().equals("APPROVED")) {
            throw new IllegalStateException("GRR can only be created for APPROVED Purchase Order");
        }

        // 3️⃣ Prevent duplicate GRR
        grrRepository.findByPurchaseOrderId(purchaseOrder.getId())
                .ifPresent(existing -> {
                	throw new DuplicateResourceException("GRR already exists for this Purchase Order");

                });

        // 4️⃣ Create GRR entity
        GRR grr = new GRR();
        grr.setPurchaseOrder(purchaseOrder);

        List<GRRItem> grrItems = new ArrayList<>();
        double totalAmount = 0.0;

        // 5️⃣ Process each item
        for (GRRItemRequestDTO itemDTO : requestDTO.getItems()) {

            Item item = itemRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

            GRRItem grrItem = new GRRItem();
            grrItem.setGrr(grr);
            grrItem.setItem(item);
            grrItem.setQuantity(itemDTO.getQuantity());
            grrItem.setUnitPrice(itemDTO.getUnitPrice());

            double subTotal = itemDTO.getQuantity() * itemDTO.getUnitPrice();
            grrItem.setSubTotal(subTotal);

            totalAmount += subTotal;

            grrItems.add(grrItem);
        }

        grr.setItems(grrItems);
        grr.setTotalAmount(totalAmount);

        GRR savedGRR = grrRepository.save(grr);

        return mapToResponse(savedGRR);
    }

    private GRRResponseDTO mapToResponse(GRR grr) {

        GRRResponseDTO response = new GRRResponseDTO();
        response.setId(grr.getId());
        response.setGrrNumber(grr.getGrrNumber());
        response.setStatus(grr.getStatus().name());
        response.setTotalAmount(grr.getTotalAmount());

        List<GRRItemResponseDTO> itemResponses = new ArrayList<>();

        for (GRRItem item : grr.getItems()) {

            GRRItemResponseDTO dto = new GRRItemResponseDTO();
            dto.setItemId(item.getItem().getId());
            dto.setItemName(item.getItem().getName());
            dto.setQuantity(item.getQuantity());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setSubTotal(item.getSubTotal());

            itemResponses.add(dto);
        }

        response.setItems(itemResponses);

        return response;
    }
    @Transactional
    public void receiveGRR(Long id) {

        // 1️⃣ Validate GRR exists
        GRR grr = grrRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRR not found"));

        // 2️⃣ Validate GRR status
        if (grr.getStatus() == GRRStatus.RECEIVED) {
            throw new InvalidOperationException("GRR already received. Stock already updated.");
        }

        if (grr.getStatus() == GRRStatus.CANCELLED) {
            throw new InvalidOperationException("Cancelled GRR cannot be received.");
        }

        // 3️⃣ Validate linked Purchase Order is APPROVED
        PurchaseOrder po = grr.getPurchaseOrder();
        if (!po.getStatus().name().equals("APPROVED")) {
            throw new IllegalStateException("Cannot receive GRR. Purchase Order is not approved.");
        }

        // 4️⃣ Increase stock for each item
        for (GRRItem grrItem : grr.getItems()) {

            Item item = grrItem.getItem();

            int currentStock = item.getStockQty();
            int receivedQty = grrItem.getQuantity();

            item.setStockQty(currentStock + receivedQty);

            itemRepository.save(item);
        }

        // 5️⃣ Update GRR status
        grr.setStatus(GRRStatus.RECEIVED);

        grrRepository.save(grr);
    }
    public List<GRRResponseDTO> getAllGRR() {
        return grrRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public GRRResponseDTO getGRRById(Long id) {

        GRR grr = grrRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRR not found"));

        return mapToResponse(grr);
    }
    @Transactional
    public void cancelGRR(Long id) {

        GRR grr = grrRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRR not found"));

        if (grr.getStatus() == GRRStatus.RECEIVED) {
        	throw new BusinessException("Received GRR cannot be cancelled.");

        }

        if (grr.getStatus() == GRRStatus.CANCELLED) {
            throw new IllegalStateException("GRR already cancelled.");
        }

        grr.setStatus(GRRStatus.CANCELLED);

        grrRepository.save(grr);
    }


}
