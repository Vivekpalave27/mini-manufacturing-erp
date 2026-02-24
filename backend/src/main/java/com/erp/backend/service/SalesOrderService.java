package com.erp.backend.service;

import com.erp.backend.dto.*;
import com.erp.backend.entity.*;
import com.erp.backend.repository.*;
import com.erp.backend.exception.BusinessException;
import com.erp.backend.exception.InsufficientStockException;
import com.erp.backend.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final ItemRepository itemRepository;

    public SalesOrderService(SalesOrderRepository salesOrderRepository,
                             ItemRepository itemRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.itemRepository = itemRepository;
    }

    // ==========================
    // CREATE SALES ORDER
    // ==========================
    public SalesOrderResponseDTO createSalesOrder(SalesOrderRequestDTO requestDTO) {

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomerName(requestDTO.getCustomerName());
        salesOrder.setOrderDate(LocalDate.now());
        salesOrder.setCreatedAt(LocalDateTime.now());
        salesOrder.setStatus(SalesOrderStatus.CREATED);
        salesOrder.setSoNumber("SO-" + UUID.randomUUID());

        double totalAmount = 0.0;

        List<SalesOrderItem> orderItems = requestDTO.getItems()
                .stream()
                .map(itemDTO -> {

                    Item item = itemRepository.findById(itemDTO.getItemId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Item not found with id: " + itemDTO.getItemId()));

                    SalesOrderItem orderItem = new SalesOrderItem();
                    orderItem.setSalesOrder(salesOrder);
                    orderItem.setItem(item);
                    orderItem.setQuantity(itemDTO.getQuantity());
                    orderItem.setUnitPrice(itemDTO.getUnitPrice());

                    double subTotal = itemDTO.getQuantity() * itemDTO.getUnitPrice();
                    orderItem.setSubTotal(subTotal);

                    return orderItem;
                })
                .collect(Collectors.toList());

        totalAmount = orderItems.stream()
                .mapToDouble(SalesOrderItem::getSubTotal)
                .sum();

        salesOrder.setItems(orderItems);
        salesOrder.setTotalAmount(totalAmount);

        SalesOrder savedOrder = salesOrderRepository.save(salesOrder);

        return mapToResponse(savedOrder);
    }

    // ==========================
    // CONFIRM SALES ORDER
    // ==========================
    public SalesOrderResponseDTO confirmOrder(Long id) {

        SalesOrder order = getOrderById(id);

        validateOrderNotConfirmed(order);
        validateStockAvailability(order);

        deductStock(order);

        order.setStatus(SalesOrderStatus.CONFIRMED);

        return mapToResponse(order);
    }

    // ==========================
    // GET ALL ORDERS
    // ==========================
    @Transactional(readOnly = true)
    public List<SalesOrderResponseDTO> getAllSalesOrders() {
        return salesOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==========================
    // PRIVATE HELPERS
    // ==========================

    private SalesOrder getOrderById(Long id) {
        return salesOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Sales Order not found with id: " + id));
    }

    private void validateOrderNotConfirmed(SalesOrder order) {
        if (order.getStatus() == SalesOrderStatus.CONFIRMED) {
            throw new BusinessException(
                    "Sales Order already confirmed: " + order.getSoNumber());
        }
    }

    private void validateStockAvailability(SalesOrder order) {
        for (SalesOrderItem orderItem : order.getItems()) {

            Item item = orderItem.getItem();

            if (item.getStockQty() < orderItem.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for item: " + item.getName());
            }
        }
    }

    private void deductStock(SalesOrder order) {
        for (SalesOrderItem orderItem : order.getItems()) {

            Item item = orderItem.getItem();
            item.setStockQty(item.getStockQty() - orderItem.getQuantity());

            itemRepository.save(item);
        }
    }

    private SalesOrderResponseDTO mapToResponse(SalesOrder salesOrder) {

        SalesOrderResponseDTO response = new SalesOrderResponseDTO();
        response.setSoNumber(salesOrder.getSoNumber());
        response.setCustomerName(salesOrder.getCustomerName());
        response.setStatus(salesOrder.getStatus().name());
        response.setTotalAmount(salesOrder.getTotalAmount());

        response.setItems(
                salesOrder.getItems()
                        .stream()
                        .map(item -> {
                            SalesOrderItemResponseDTO dto =
                                    new SalesOrderItemResponseDTO();
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