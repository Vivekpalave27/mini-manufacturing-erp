package com.erp.backend.service;

import com.erp.backend.dto.SupplierRequestDTO;
import com.erp.backend.dto.SupplierResponseDTO;
import com.erp.backend.entity.Supplier;
import com.erp.backend.exception.DuplicateResourceException;
import com.erp.backend.exception.ResourceNotFoundException;
import com.erp.backend.repository.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // ==========================
    // CREATE SUPPLIER
    // ==========================
    public SupplierResponseDTO createSupplier(SupplierRequestDTO request) {

        if (supplierRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Supplier with email already exists: " + request.getEmail());
        }

        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());

        return mapToResponse(supplierRepository.save(supplier));
    }

    // ==========================
    // GET ALL SUPPLIERS
    // ==========================
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==========================
    // GET BY ID
    // ==========================
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierById(Long id) {
        return mapToResponse(getSupplierEntity(id));
    }

    // ==========================
    // UPDATE SUPPLIER
    // ==========================
    public SupplierResponseDTO updateSupplier(Long id,
                                              SupplierRequestDTO request) {

        Supplier supplier = getSupplierEntity(id);

        if (!supplier.getEmail().equals(request.getEmail()) &&
                supplierRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Another supplier with email already exists: "
                            + request.getEmail());
        }

        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());

        return mapToResponse(supplier);
    }

    // ==========================
    // DELETE SUPPLIER
    // ==========================
    public void deleteSupplier(Long id) {
        supplierRepository.delete(getSupplierEntity(id));
    }

    // ==========================
    // PRIVATE HELPERS
    // ==========================
    private Supplier getSupplierEntity(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with id: " + id));
    }

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