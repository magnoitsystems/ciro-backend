package com.ciro.backend.controller;

import com.ciro.backend.dto.StatisticsResponseDTO;
import com.ciro.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<StatisticsResponseDTO> getDashboardStats() {
        StatisticsResponseDTO stats = statisticsService.getDashboardStatistics();
        return ResponseEntity.ok(stats);
    }
}