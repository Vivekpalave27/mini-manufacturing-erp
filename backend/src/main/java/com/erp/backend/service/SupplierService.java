package com.erp.backend.service;

import com.erp.backend.dto.SupplierRequestDTO;
import com.erp.backend.dto.SupplierResponseDTO;
import com.erp.backend.entity.Supplier;
import com.erp.backend.exception.ResourceNotFoundException;
import com.erp.backend.repository.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // ================================
    // CREATE SUPPLIER
    // ================================
    public SupplierResponseDTO createSupplier(SupplierRequestDTO request) {

        if (supplierRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Supplier with this email already exists");
        }

        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());

        Supplier saved = supplierRepository.save(supplier);

        return mapToResponse(saved);
    }

    // ================================
    // GET ALL SUPPLIERS
    // ================================
    public List<SupplierResponseDTO> getAllSuppliers() {

        return supplierRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================
    // GET SUPPLIER BY ID
    // ================================
    public SupplierResponseDTO getSupplierById(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        return mapToResponse(supplier);
    }

    // ================================
    // UPDATE SUPPLIER
    // ================================
    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO request) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        // Check email duplicate only if changed
        if (!supplier.getEmail().equals(request.getEmail()) &&
                supplierRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Another supplier with this email already exists");
        }

        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());

        Supplier updated = supplierRepository.save(supplier);

        return mapToResponse(updated);
    }

    // ================================
    // DELETE SUPPLIER
    // ================================
    public void deleteSupplier(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        supplierRepository.delete(supplier);
    }

    // ================================
    // MAPPING METHOD
    // ================================
    private SupplierResponseDTO mapToResponse(Supplier supplier) {
        return new SupplierResponseDTO(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress()
        );
    }
}
