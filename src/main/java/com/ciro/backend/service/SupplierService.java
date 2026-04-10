package com.ciro.backend.service;

import com.ciro.backend.dto.SupplierCreateDTO;
import com.ciro.backend.dto.SupplierResponseDTO;
import com.ciro.backend.entity.Supplier;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public SupplierResponseDTO save(SupplierCreateDTO dto) {
        Supplier supplier = new Supplier();
        supplier.setFullName(dto.getFullName());
        supplier.setAddress(dto.getAddress());
        supplier.setCity(dto.getCity());
        supplier.setDni(dto.getDni());
        supplier.setObservations(dto.getObservations());

        Supplier savedSupplier = supplierRepository.save(supplier);
        return mapToDTO(savedSupplier);
    }

    public SupplierResponseDTO findById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
        return mapToDTO(supplier);
    }

    public SupplierResponseDTO update(SupplierCreateDTO dto, Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));

        if (dto.getFullName() != null) supplier.setFullName(dto.getFullName());
        if (dto.getAddress() != null) supplier.setAddress(dto.getAddress());
        if (dto.getCity() != null) supplier.setCity(dto.getCity());
        if (dto.getDni() != null) supplier.setDni(dto.getDni());
        if (dto.getObservations() != null) supplier.setObservations(dto.getObservations());

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return mapToDTO(updatedSupplier);
    }

    public List<SupplierResponseDTO> findAll() {
        return supplierRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proveedor no encontrado");
        }
        supplierRepository.deleteById(id);
    }

    public SupplierResponseDTO mapToDTO(Supplier supplier) {
        SupplierResponseDTO dto = new SupplierResponseDTO();
        dto.setId(supplier.getId());
        dto.setFullName(supplier.getFullName());
        dto.setAddress(supplier.getAddress());
        dto.setCity(supplier.getCity());
        dto.setDni(supplier.getDni());
        dto.setObservations(supplier.getObservations());
        return dto;
    }
}