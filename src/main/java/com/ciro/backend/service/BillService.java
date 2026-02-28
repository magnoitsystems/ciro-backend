package com.ciro.backend.service;

import com.ciro.backend.dto.BillCreateDTO;
import com.ciro.backend.dto.BillResponseDTO;
import com.ciro.backend.entity.Bill;
import com.ciro.backend.entity.Supplier;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.BillStatus;
import com.ciro.backend.exception.BadRequestException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.BillRepository;
import com.ciro.backend.repository.SupplierRepository;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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

        String entityName = "Gasto General";

        if (dto.getEmployeeId() != null) {
            User employee = userRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
            bill.setEmployee(employee);
            entityName = employee.getName() + " " + employee.getLastname();
        }
        else if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            bill.setSupplier(supplier);
            entityName = supplier.getFullName();
        }

        Bill savedBill = billRepository.save(bill);

        return new BillResponseDTO(
                savedBill.getId(),
                entityName,
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
}