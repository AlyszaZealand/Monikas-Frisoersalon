package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Treatment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TreatmentRepository {
    private final DB db;

    // DB
    public TreatmentRepository(DB db) {
        this.db = db;
    }

    // Create treatment SQL
    public void createTreatment(int treatmentID) {
        String sql = "";
    }

    // Delete treatment SQL
    public void deleteTreatment(int treatmentID) {
        String sql = "";
    }

    // Show all treatments SQL
    public void displayAllTreatments(int treatmentID) {
        String sql = "";
    }

    // Retrieves the columns SQL
    private Treatment mapRow(ResultSet rs) throws SQLException {

        return new Treatment(
            rs.getInt("id"),
                rs.getString("Treatment"),
                rs.getInt("Duration")
        );
    }

}
