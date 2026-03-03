package org.example.monikasfrisoersalon.Repository;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Treatment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreatmentRepositoryTest {
    private DB db;
    private TreatmentRepository treatmentRepo;

    @BeforeEach
    void setUp() {
        db = new DB();
        treatmentRepo = new TreatmentRepository(db);
    }

    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM treatment WHERE typeoftreatment = 'TestBehandling'";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Kunne ikke rydde op i databasen: " + e.getMessage());
        }
    }

    @Test
    void testFindAllTreatments() {
        List<Treatment> result = treatmentRepo.findActiveTreatments();
        assertNotNull(result, "Listen over behandlinger må ikke være null");
        assertFalse(result.isEmpty(), "Listen må ikke være tom (kræver at der er mindst én aktiv behandling i dit SchemaSeed)");

        Treatment firstTreatment = result.get(0);
        assertNotNull(firstTreatment.getTypeOfTreatment(), "Behandlingen skal have et navn");
        assertEquals("Wash & brush", firstTreatment.getTypeOfTreatment(), "Første behandling skal være 'Wash & brush' ifølge SchemaSeed");
        assertEquals( 30, firstTreatment.getDuration(),"Behandlingens varighed skal være 30 minutter ifølge SchemaSeed");
        assertEquals( 150, firstTreatment.getPrice(), "Behandlingens pris skal være 150 ifølge SchemaSeed");
    }

    @Test
    void testFindActiveTreatments() {
        List<Treatment> result = treatmentRepo.findActiveTreatments();
        assertNotNull(result, "Listen over behandlinger må ikke være null");
        assertFalse(result.isEmpty(), "Listen må ikke være tom (kræver at der er mindst én aktiv behandling i dit SchemaSeed)");

        Treatment firstTreatment = result.get(0);
        assertNotNull(firstTreatment.getTypeOfTreatment(), "Behandlingen skal have et navn");
        assertEquals("Wash & brush", firstTreatment.getTypeOfTreatment(), "Første behandling skal være 'Wash & brush' ifølge SchemaSeed");
        assertEquals( 30, firstTreatment.getDuration(),"Behandlingens varighed skal være 30 minutter ifølge SchemaSeed");
        assertEquals( 150, firstTreatment.getPrice(), "Behandlingens pris skal være 150 ifølge SchemaSeed");
    }

    @Test
    void testCreateTreatment() {
        Treatment newTreatment = new Treatment(0, "TestBehandling", 45, 250, true);
        treatmentRepo.createTreatment(newTreatment);

        List<Treatment> activeTreatments = treatmentRepo.findActiveTreatments();
        Treatment savedTreatment = null;

        for (Treatment t : activeTreatments) {
            if ("TestBehandling".equals(t.getTypeOfTreatment())) {
                savedTreatment = t;
                break;
            }
        }

        assertNotNull(savedTreatment, "Den nyoprettede 'TestBehandling' burde kunne findes i databasen");
        assertEquals("TestBehandling", savedTreatment.getTypeOfTreatment(), "Behandlingens navn burde være 'TestBehandling'");
        assertEquals(45, savedTreatment.getDuration(), "Behandlingens varighed burde være 45 minutter");
        assertEquals(250, savedTreatment.getPrice(), "Behandlingens pris burde være 250 kr.");
    }

    @Test
    void testDeleteTreatmentSafelyAndFindAll() {
        int treatmentId = 1;

        try {
            treatmentRepo.deleteTreatmentSafely(treatmentId); // Sæt behandling med ID 1 til inaktiv

            List<Treatment> activeTreatments = treatmentRepo.findActiveTreatments();
            boolean foundInActive = false;

            for (Treatment t : activeTreatments) {
                if (t.getId() == treatmentId) {
                    foundInActive = true;
                    break;
                }
            }
            assertFalse(foundInActive, "Behandlingen med ID 1 burde IKKE være i listen over aktive");


            List<Treatment> allTreatments = treatmentRepo.findAllTreatments();
            boolean foundInAll = false;

            for (Treatment t : allTreatments) {
                if (t.getId() == treatmentId) {
                    foundInAll = true;
                    break;
                }
            }
            assertTrue(foundInAll, "Behandlingen med ID 1 SKAL stadig findes i findAllTreatments");

        } finally { // Sæt behandling med ID 1 tilbage til aktiv for at undgå sideeffekter på andre tests
            String sql = "UPDATE treatment SET isactive = true WHERE id = ?";
            try (java.sql.Connection con = db.getConnection();
                 java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, treatmentId);
                ps.executeUpdate();
            } catch (java.sql.SQLException e) {
                System.out.println("Oprydning fejlede: " + e.getMessage());
            }
        }
    }
}





