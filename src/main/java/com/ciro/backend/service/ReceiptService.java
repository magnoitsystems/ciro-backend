package com.ciro.backend.service;

import com.ciro.backend.dto.ReceiptCreateDTO;
import com.ciro.backend.dto.ReceiptResponseDTO;
import com.ciro.backend.entity.*;
import com.ciro.backend.enums.CurrentAccountType;
import com.ciro.backend.exception.BadRequestException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;

@Service
public class ReceiptService {

    @Autowired private ReceiptRepository receiptRepository;
    @Autowired private CurrentAccountRepository currentAccountRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private UserRepository userRepository;

    @Transactional
    public ReceiptResponseDTO createReceipt(ReceiptCreateDTO dto) {

        if (dto.getAmount() == null || dto.getAmount().doubleValue() <= 0) {
            throw new BadRequestException("El importe del recibo debe ser mayor a 0.");
        }

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Receipt receipt = new Receipt();
        receipt.setReceiptDate(dto.getReceiptDate() != null ? dto.getReceiptDate() : LocalDate.now());
        receipt.setAmount(dto.getAmount());
        receipt.setObservations(dto.getObservations());
        receipt.setCurrencyType(dto.getCurrencyType());
        receipt.setPatient(patient);
        receipt.setUser(user);

        Receipt savedReceipt = receiptRepository.save(receipt);

        CurrentAccount accountEntry = new CurrentAccount();
        accountEntry.setPatient(patient);
        accountEntry.setReceipt(savedReceipt);
        accountEntry.setType(CurrentAccountType.RECEIPT);
        accountEntry.setCanceled(false);

        currentAccountRepository.save(accountEntry);

        return new ReceiptResponseDTO(
                savedReceipt.getId(),
                savedReceipt.getReceiptDate(),
                savedReceipt.getAmount(),
                savedReceipt.getCurrencyType()
        );
    }
}