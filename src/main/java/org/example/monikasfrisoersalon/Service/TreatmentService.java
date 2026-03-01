package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Treatment;
import org.example.monikasfrisoersalon.Repoistory.TreatmentRepository;

import java.util.List;

public class TreatmentService {

    private final TreatmentRepository treatmentRepo;

    public TreatmentService(TreatmentRepository treatmentRepo) {
        this.treatmentRepo = treatmentRepo;
    }

    public void createTreatment(Treatment treatment) {
        // Business Logic: Validering før vi lader databasen røre ved det
        if (treatment.getDuration() <= 0) {
            throw new IllegalArgumentException("Fejl: En behandling skal tage mere end 0 minutter.");
        }
        if (treatment.getPrice() < 0) {
            throw new IllegalArgumentException("Fejl: Prisen kan ikke være negativ.");
        }
        if (treatment.getTypeOfTreatment() == null || treatment.getTypeOfTreatment().trim().isEmpty()) {
            throw new IllegalArgumentException("Fejl: Behandlingen skal have et navn.");
        }
        treatmentRepo.createTreatment(treatment);
    }

    public List<Treatment> getActiveTreatments() {
        return treatmentRepo.findActiveTreatments();
    }

    public void removeTreatment(int treatmentId) {
        treatmentRepo.deleteTreatmentSafely(treatmentId);
    }
}
