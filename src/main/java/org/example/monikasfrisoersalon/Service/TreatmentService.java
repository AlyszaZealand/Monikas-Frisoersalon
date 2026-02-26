package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Repoistory.TreatmentRepository;

import java.sql.SQLException;

public class TreatmentService {
    private final TreatmentRepository treatmentRepository;

    //
    public TreatmentService(TreatmentRepository treatmentRepo) {
        this.treatmentRepository = treatmentRepo;
    }

    // Create treatment
    public void handleCreateTreatment(String treatment, int duraiton) {
        try{
            treatmentRepository.createTreatment(treatment, duraiton);
        }catch (SQLException e){
            //custom exception needed maybe???
        }

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
