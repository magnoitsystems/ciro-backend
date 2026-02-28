package com.ciro.backend.service;

import com.ciro.backend.dto.BillCreateDTO;
import com.ciro.backend.dto.BillResponseDTO;
import com.ciro.backend.entity.Bill;
import com.ciro.backend.entity.Supplier;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.BillStatus;
import com.ciro.backend.enums.BillType;
import com.ciro.backend.enums.OriginType;
import com.ciro.backend.exception.BadRequestException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.BillRepository;
import com.ciro.backend.repository.SupplierRepository;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired private BillRepository billRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SupplierRepository supplierRepository;

    @Transactional
    public BillResponseDTO createBill(BillCreateDTO dto) {

        if (dto.getAmount() == null || dto.getAmount().doubleValue() <= 0) {
            throw new BadRequestException("El importe a registrar debe ser mayor a 0.");
        }

        if (dto.getStatus() == BillStatus.PAGADO) {
            if (dto.getFrom() == null || dto.getPaymentMethod() == null) {
                throw new BadRequestException("Si el gasto está PAGADO, debe indicar el Origen del dinero y el Método de pago.");
            }
        }

        Bill bill = new Bill();
        bill.setBillDate(dto.getBillDate() != null ? dto.getBillDate() : LocalDate.now());
        bill.setAmount(dto.getAmount());
        bill.setDescription(dto.getDescription());
        bill.setStatus(dto.getStatus());
        bill.setCurrencyType(dto.getCurrencyType());
        bill.setBillType(dto.getBillType());

        if (dto.getStatus() == BillStatus.PAGADO) {
            bill.setFrom(dto.getFrom());
            bill.setPaymentMethod(dto.getPaymentMethod());
        }

        String entityName = "Gasto de " + dto.getBillType().name();

        Long employeeId = null;
        String employeeFullName = null;
        Long supplierId = null;
        String supplierFullName = null;

        if (dto.getEmployeeId() != null) {
            User employee = userRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
            bill.setEmployee(employee);
            employeeId = employee.getId();
            employeeFullName = employee.getName() + " " + employee.getLastname();
        }
        else if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            bill.setSupplier(supplier);
            supplierId = supplier.getId();
            supplierFullName = supplier.getFullName();
        }

        Bill savedBill = billRepository.save(bill);

        return new BillResponseDTO(
                savedBill.getId(),
                entityName,
                employeeId,
                employeeFullName,
                supplierId,
                supplierFullName,
                savedBill.getBillDate(),
                savedBill.getAmount(),
                savedBill.getDescription(),
                savedBill.getStatus(),
                savedBill.getPaymentMethod(),
                savedBill.getCurrencyType(),
                savedBill.getFrom(),
                savedBill.getBillType()
        );
    }

    @Transactional
    public BillResponseDTO updateBill(Long id, BillCreateDTO dto) {

        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El gasto o sueldo no existe"));

        if (dto.getAmount() == null || dto.getAmount().doubleValue() <= 0) {
            throw new BadRequestException("El importe debe ser mayor a 0.");
        }

        if (dto.getStatus() == BillStatus.PAGADO) {
            if (dto.getFrom() == null || dto.getPaymentMethod() == null) {
                throw new BadRequestException("Para marcar como PAGADO, debe indicar el Origen del dinero y el Método de pago.");
            }
        }

        existingBill.setBillDate(dto.getBillDate() != null ? dto.getBillDate() : existingBill.getBillDate());
        existingBill.setAmount(dto.getAmount());
        existingBill.setDescription(dto.getDescription());
        existingBill.setStatus(dto.getStatus());
        existingBill.setCurrencyType(dto.getCurrencyType());
        existingBill.setBillType(dto.getBillType());

        if (dto.getStatus() == BillStatus.PAGADO) {
            existingBill.setFrom(dto.getFrom());
            existingBill.setPaymentMethod(dto.getPaymentMethod());
        } else {
            existingBill.setFrom(null);
            existingBill.setPaymentMethod(null);
        }

        existingBill.setEmployee(null);
        existingBill.setSupplier(null);

        String entityName = "Gasto de " + dto.getBillType().name();

        Long employeeId = null;
        String employeeFullName = null;
        Long supplierId = null;
        String supplierFullName = null;

        if (dto.getEmployeeId() != null) {
            User employee = userRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
            existingBill.setEmployee(employee);
            employeeId = employee.getId();
            employeeFullName = employee.getName() + " " + employee.getLastname();
        }
        else if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            existingBill.setSupplier(supplier);
            supplierId = supplier.getId();
            supplierFullName = supplier.getFullName();
        }

        Bill updatedBill = billRepository.save(existingBill);


        return new BillResponseDTO(
                updatedBill.getId(),
                entityName,
                employeeId,
                employeeFullName,
                supplierId,
                supplierFullName,
                updatedBill.getBillDate(),
                updatedBill.getAmount(),
                updatedBill.getDescription(),
                updatedBill.getStatus(),
                updatedBill.getPaymentMethod(),
                updatedBill.getCurrencyType(),
                updatedBill.getFrom(),
                updatedBill.getBillType()
        );
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getBills(BillType type, OriginType origin) {
        List<Bill> bills;

        if (type != null && origin != null) {
            bills = billRepository.findByBillTypeAndFromOrderByBillDateAsc(type, origin);
        } else if (type != null) {
            bills = billRepository.findByBillTypeOrderByBillDateAsc(type);
        } else if (origin != null) {
            bills = billRepository.findByFromOrderByBillDateAsc(origin);
        } else {
            bills = billRepository.findAllByOrderByBillDateAsc();
        }

        return bills.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private BillResponseDTO mapToDTO(Bill bill) {
        String entityName = "Gasto de " + bill.getBillType().name();

        Long employeeId = null;
        String employeeFullName = null;
        Long supplierId = null;
        String supplierFullName = null;

        if (bill.getEmployee() != null) {
            employeeId = bill.getEmployee().getId();
            employeeFullName = bill.getEmployee().getName() + " " + bill.getEmployee().getLastname();
        }
        else if (bill.getSupplier() != null) {
            supplierId = bill.getSupplier().getId();
            supplierFullName = bill.getSupplier().getFullName();
        }

        return new BillResponseDTO(
                bill.getId(),
                entityName,
                employeeId,
                employeeFullName,
                supplierId,
                supplierFullName,
                bill.getBillDate(),
                bill.getAmount(),
                bill.getDescription(),
                bill.getStatus(),
                bill.getPaymentMethod(),
                bill.getCurrencyType(),
                bill.getFrom(),
                bill.getBillType()
        );
    }
}