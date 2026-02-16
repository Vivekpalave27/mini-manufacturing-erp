package com.erp.backend.service;

import com.erp.backend.dto.*;
import com.erp.backend.entity.*;
import com.erp.backend.repository.*;
import com.erp.backend.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final ItemRepository itemRepository;

    public SalesOrderService(SalesOrderRepository salesOrderRepository,
                             SalesOrderItemRepository salesOrderItemRepository,
                             ItemRepository itemRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public SalesOrderResponseDTO createSalesOrder(SalesOrderRequestDTO requestDTO) {

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomerName(requestDTO.getCustomerName());
        salesOrder.setOrderDate(LocalDate.now());
        salesOrder.setCreatedAt(LocalDateTime.now());
        salesOrder.setStatus(SalesOrderStatus.CREATED);
        salesOrder.setSoNumber("SO-" + UUID.randomUUID());

        List<SalesOrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (SalesOrderItemRequestDTO itemDTO : requestDTO.getItems()) {

            Item item = itemRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

            SalesOrderItem orderItem = new SalesOrderItem();
            orderItem.setSalesOrder(salesOrder);
            orderItem.setItem(item);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(itemDTO.getUnitPrice());

            double subTotal = itemDTO.getQuantity() * itemDTO.getUnitPrice();
            orderItem.setSubTotal(subTotal);

            totalAmount += subTotal;

            orderItems.add(orderItem);
        }

        salesOrder.setTotalAmount(totalAmount);
        salesOrder.setItems(orderItems);

        SalesOrder savedOrder = salesOrderRepository.save(salesOrder);

        return mapToResponse(savedOrder);
    }

    private SalesOrderResponseDTO mapToResponse(SalesOrder salesOrder) {

        SalesOrderResponseDTO response = new SalesOrderResponseDTO();
        response.setSoNumber(salesOrder.getSoNumber());
        response.setCustomerName(salesOrder.getCustomerName());
        response.setStatus(salesOrder.getStatus().name());
        response.setTotalAmount(salesOrder.getTotalAmount());

        List<SalesOrderItemResponseDTO> itemResponses = new ArrayList<>();

        for (SalesOrderItem item : salesOrder.getItems()) {

            SalesOrderItemResponseDTO itemDTO = new SalesOrderItemResponseDTO();
            itemDTO.setItemName(item.getItem().getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setUnitPrice(item.getUnitPrice());
            itemDTO.setSubTotal(item.getSubTotal());

            itemResponses.add(itemDTO);
        }

        response.setItems(itemResponses);

        return response;
    }
}
