package com.erp.backend.service;

import com.erp.backend.dto.InventoryResponseDTO;
import com.erp.backend.entity.Item;
import com.erp.backend.repository.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private ItemRepository itemRepository;

    public List<InventoryResponseDTO> getAllInventory() {

        List<Item> items = itemRepository.findAll();

        return items.stream()
                .map(item -> new InventoryResponseDTO(
                        item.getId(),
                        item.getName(),
                        item.getSku(),
                        item.getPrice(),
                        item.getStockQty()
                ))
                .collect(Collectors.toList());
    }
}
