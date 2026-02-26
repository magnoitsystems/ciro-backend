package com.ciro.backend.service;

import com.ciro.backend.dto.SupplierDTO;
import com.ciro.backend.entity.Supplier;
import com.ciro.backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier save(SupplierDTO supplier) {
        Supplier newSupplier = new Supplier();

        newSupplier.setAddress(supplier.getAddress());
        newSupplier.setCity(supplier.getCity());
        newSupplier.setDni(supplier.getDni());
        newSupplier.setObservations(supplier.getObservations());

        return supplierRepository.save(newSupplier);
    }

    public SupplierDTO findById(Long id) {
        if(id >= 0){
            Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier no encontrado"));

            return mapToDTO(supplier);
        }
        return null;
    }

    public Supplier update(SupplierDTO supplier, Long id) {
        if(id >= 0){
            Supplier newSupplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier no encontrado"));
            newSupplier.setAddress(supplier.getAddress());
            newSupplier.setCity(supplier.getCity());
            newSupplier.setDni(supplier.getDni());
            newSupplier.setObservations(supplier.getObservations());

            return supplierRepository.save(newSupplier);
        }
        return null;
    }

    public List<SupplierDTO> findAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierDTO> supplierDTOs = new ArrayList<>();

        for (Supplier supplier : suppliers) {
            supplierDTOs.add(mapToDTO(supplier));
        }

        return supplierDTOs;
    }

    public void deleteById(Long id) {
        if(id >= 0){
            supplierRepository.deleteById(id);
        }
    }

    public SupplierDTO mapToDTO(Supplier supplier) {
        SupplierDTO supplierDTO = new SupplierDTO();

        supplierDTO.setAddress(supplier.getAddress());
        supplierDTO.setCity(supplier.getCity());
        supplierDTO.setDni(supplier.getDni());
        supplierDTO.setObservations(supplier.getObservations());

        return supplierDTO;
    }
}
