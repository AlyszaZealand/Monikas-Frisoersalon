package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Repoistory.TreatmentRepository;

public class TreatmentService {
    private final TreatmentRepository treatmentRepository;

    //
    public TreatmentService(TreatmentRepository treatmentRepo) {
        this.treatmentRepository = treatmentRepo;
    }

    // Create treatment
    public void handleCreateTreatment(int treatmentID) {
        treatmentRepository.createTreatment(treatmentID);
    }

    // Delete treatment
    public void handleDeleteTreatment(int treatmentID) {
        treatmentRepository.deleteTreatment(treatmentID);
    }

    // Display all treatments
    public void handleDisplayAllTreatments(int treatmentID) {
        treatmentRepository.displayAllTreatments(treatmentID);
    }

}
