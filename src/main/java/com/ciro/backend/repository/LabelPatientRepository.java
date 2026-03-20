package com.ciro.backend.repository;

import com.ciro.backend.entity.Label;
import com.ciro.backend.entity.LabelPatient;
import com.ciro.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

@Repository
public interface LabelPatientRepository extends JpaRepository<LabelPatient, Long> {
    @Query("SELECT  lp FROM LabelPatient lp WHERE lp.patient.id = :idPatient AND lp.label.id = :idLabel")
    LabelPatient existsByPatientIdAndLabelId(Long idPatient, Long idLabel);

    @Query("SELECT lp FROM LabelPatient lp WHERE lp.label.id = :idLabel")
    List<LabelPatient> findLabelPatientByLabel(Long idLabel);

    @Query("SELECT lp FROM LabelPatient lp WHERE lp.patient.id = :idPatient")
    List<LabelPatient> findLabelPatientById(Long idPatient);

    @Modifying
    @Transactional
    void deleteByPatientAndLabel(Patient patient, Label label);

}
