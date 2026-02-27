package com.ciro.backend.service;

import com.ciro.backend.repository.PracticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PracticeService {
    @Autowired
    private PracticeRepository practiceRepository;

}
