package com.erp.backend.service;

import com.erp.backend.dto.ItemRequestDTO;
import com.erp.backend.dto.ItemResponseDTO;
import com.erp.backend.entity.Item;

import com.erp.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    // CREATE ITEM
    public ItemResponseDTO createItem(ItemRequestDTO request) {

        Item item = new Item();
        item.setName(request.getName());
        item.setSku(request.getSku());
        item.setUnit(request.getUnit());
        item.setPrice(request.getPrice());

        // ERP Rule: Stock always starts at 0
        item.setStockQty(0);

        Item savedItem = itemRepository.save(item);

        return mapToResponse(savedItem);
    }

    // GET ALL ITEMS
    public List<ItemResponseDTO> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET ITEM BY ID
    public ItemResponseDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return mapToResponse(item);
    }

    // UPDATE ITEM
    public ItemResponseDTO updateItem(Long id, ItemRequestDTO request) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setName(request.getName());
        item.setSku(request.getSku());
        item.setUnit(request.getUnit());
        item.setPrice(request.getPrice());

        // 🚫 Do NOT modify stockQty here

        Item updatedItem = itemRepository.save(item);

        return mapToResponse(updatedItem);
    }

    // DELETE ITEM
    public void deleteItem(Long id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        itemRepository.delete(item);
    }

    // ENTITY → RESPONSE DTO MAPPER
    private ItemResponseDTO mapToResponse(Item item) {

        ItemResponseDTO response = new ItemResponseDTO();

        response.setId(item.getId());
        response.setName(item.getName());
        response.setSku(item.getSku());
        response.setUnit(item.getUnit());
        response.setPrice(item.getPrice());
        response.setStockQty(item.getStockQty());
        response.setCreatedAt(item.getCreatedAt());

        return response;
    }
 

    

}
