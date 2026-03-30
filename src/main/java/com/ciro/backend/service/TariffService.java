package com.ciro.backend.service;

import com.ciro.backend.dto.*;
import com.ciro.backend.entity.Tariff;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    @Transactional
    public TariffResponseDTO createTariff(TariffCreateDTO dto) {
        Tariff tariff = new Tariff();
        tariff.setName(dto.getName());

        tariff.setTariffDate(dto.getTariffDate() != null ? dto.getTariffDate() : LocalDate.now());

        calculateAmounts(tariff, dto.getAmountPesos(), dto.getAmountDollars(), dto.getTc());

        Tariff savedTariff = tariffRepository.save(tariff);
        return mapToResponseDTO(savedTariff);
    }

    @Transactional
    public TariffResponseDTO updateTariff(Long id, TariffUpdateDTO dto) {
        Tariff existingTariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arancel no encontrado con ID: " + id));

        if (dto.getName() != null) existingTariff.setName(dto.getName());
        if (dto.getTariffDate() != null) existingTariff.setTariffDate(dto.getTariffDate());

        // CASO 1: Si nos mandan explícitamente ambos montos, pisamos lo viejo y no recalculamos nada.
        if (dto.getAmountPesos() != null && dto.getAmountDollars() != null) {
            existingTariff.setAmountPesos(dto.getAmountPesos());
            existingTariff.setAmountDollars(dto.getAmountDollars());
            if (dto.getTc() != null) existingTariff.setTc(dto.getTc());
        }
        // CASO 2: Mandaron datos parciales, hay que recalcular.
        else {
            // Obtenemos el TC
            BigDecimal currentTc = dto.getTc() != null ? dto.getTc() : existingTariff.getTc();

            if (currentTc != null && currentTc.compareTo(BigDecimal.ZERO) > 0) {
                existingTariff.setTc(currentTc);

                // Si mandaron dólares nuevos, recalculamos los pesos
                if (dto.getAmountDollars() != null) {
                    existingTariff.setAmountDollars(dto.getAmountDollars());
                    existingTariff.setAmountPesos(dto.getAmountDollars().multiply(currentTc));
                }
                // Si mandaron pesos nuevos, recalculamos los dólares
                else if (dto.getAmountPesos() != null) {
                    existingTariff.setAmountPesos(dto.getAmountPesos());
                    existingTariff.setAmountDollars(dto.getAmountPesos().divide(currentTc, 2, java.math.RoundingMode.HALF_UP));
                }
                // Si SOLO mandaron un TC nuevo (sin montos), actualizamos los pesos manteniendo los dólares que ya existían
                else if (dto.getTc() != null && existingTariff.getAmountDollars() != null) {
                    existingTariff.setAmountPesos(existingTariff.getAmountDollars().multiply(currentTc));
                }
            }
        }

        Tariff updatedTariff = tariffRepository.save(existingTariff);
        return mapToResponseDTO(updatedTariff);
    }

    public List<TariffResponseDTO> getTariffs(String keyword, BigDecimal minPesos, BigDecimal minDollars) {
        List<Tariff> tariffs;

        if (keyword != null || minPesos != null || minDollars != null) {
            tariffs = tariffRepository.searchTariffs(keyword, minPesos, minDollars);
        } else {
            tariffs = tariffRepository.findAll();
        }

        return tariffs.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public void deleteTariff(Long id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arancel no encontrado con ID: " + id));
        tariffRepository.delete(tariff);
    }

    private void calculateAmounts(Tariff tariff, BigDecimal pesos, BigDecimal dollars, BigDecimal tc) {
        if (pesos != null && dollars != null) {
            tariff.setAmountPesos(pesos);
            tariff.setAmountDollars(dollars);
            tariff.setTc(tc);
        }
        else if (tc != null && tc.compareTo(BigDecimal.ZERO) > 0) {
            tariff.setTc(tc);
            if (dollars != null) {
                tariff.setAmountDollars(dollars);
                tariff.setAmountPesos(dollars.multiply(tc));
            } else if (pesos != null) {
                tariff.setAmountPesos(pesos);
                tariff.setAmountDollars(pesos.divide(tc, 2, RoundingMode.HALF_UP));
            }
        }
        else {
            if (pesos != null) tariff.setAmountPesos(pesos);
            if (dollars != null) tariff.setAmountDollars(dollars);
            if (tc != null) tariff.setTc(tc);
        }
    }

    private TariffResponseDTO mapToResponseDTO(Tariff tariff) {
        TariffResponseDTO dto = new TariffResponseDTO();
        dto.setId(tariff.getId());
        dto.setName(tariff.getName());
        dto.setTariffDate(tariff.getTariffDate());
        dto.setAmountDollars(tariff.getAmountDollars());
        dto.setAmountPesos(tariff.getAmountPesos());
        dto.setTc(tariff.getTc());
        return dto;
    }
}