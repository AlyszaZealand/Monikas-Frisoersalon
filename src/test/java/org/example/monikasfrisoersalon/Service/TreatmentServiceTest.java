package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Treatment;
import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Repository.TreatmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreatmentServiceTest {

    private DB db;
    private TreatmentRepository treatmentRepo;
    private TreatmentService treatmentService;

    @BeforeEach
    void setUp() {
        db = new DB();
        treatmentRepo = new TreatmentRepository(db);
        treatmentService = new TreatmentService(treatmentRepo);
    }

    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM treatment WHERE typeoftreatment = 'ServiceTestBehandling'";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Oprydning fejlede: " + e.getMessage());
        }
    }


    @Test
    void testCreateTreatmentSuccess() throws SQLException {
        Treatment testTreatment = new Treatment(0, "ServiceTestBehandling", 45, 250, true);

        treatmentService.createTreatment(testTreatment);

        // Hent alle aktive behandlinger og find den nye behandling
        List<Treatment> activeTreatments = treatmentRepo.findActiveTreatments();
        Treatment saved = null;
        for (Treatment t : activeTreatments) {
            if ("ServiceTestBehandling".equals(t.getTypeOfTreatment())) {
                saved = t;
                break;
            }
        }

        assertNotNull(saved, "Behandlingen burde findes i databasen efter oprettelse");
        assertEquals(45, saved.getDuration(), "Varigheden skal være 45 minutter");
        assertEquals(250, saved.getPrice(), "Prisen skal være 250 kr.");
    }


    @Test
    void testCreateTreatmentFailNegativePrice() {
        // Lav en behandling med en negativ pris
        Treatment badTreatment = new Treatment(0, "DyrBehandling", 30, -100, true);

        try {
            treatmentService.createTreatment(badTreatment);
            fail("Servicen burde have afvist en negativ pris.");
        } catch (IllegalArgumentException e) {
            assertEquals("Fejl: Prisen kan ikke være negativ.", e.getMessage());
        }
    }
}