package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Model.Treatment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentRepositoryTest {

    private DB db;
    private AppointmentRepository appointmentRepo;

    @BeforeEach
    void setUp() {
        db = new DB();
        appointmentRepo = new AppointmentRepository(db);
    }

    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM appointment WHERE startdate >= '2030-01-01 00:00:00'";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (java.sql.SQLException e) {
            System.out.println("Kunne ikke rydde op i databasen: " + e.getMessage());
        }
    }


    @Test
    void testFindAppointmentsByEmployee() {
        int testEmployeeId = 1;

        List<Appointment> result = appointmentRepo.findAppointmentsByEmployee(testEmployeeId);

        assertNotNull(result, "Listen af aftaler må ikke være null");
        if (!result.isEmpty()) {
            int foundEmployeeId = result.get(0).getEmployee().getId();
            assertEquals(testEmployeeId, foundEmployeeId, "Aftalen SKAL tilhøre medarbejder 1");

            Appointment firstAssignment = result.get(0);
            assertEquals(1, firstAssignment.getId(), "Den første aftale burde have ID 1");

            assertNotNull(firstAssignment.getEmployee(), "Frisør-objektet må ikke være null");
            assertEquals(1, firstAssignment.getEmployee().getId(), "Frisørens ID skal være 1");
            assertEquals("brian", firstAssignment.getEmployee().getUsername(), "Frisørens navn skal være 'brian'");
        }


    }

    @Test
    void testFindAllAppointments() {
        List<Appointment> result = appointmentRepo.findAllAppointments();

        assertNotNull(result, "Listen må ikke være null");
        assertFalse(result.isEmpty(), "Listen burde indeholde mindst én aftale fra SchemaSeed");
        Appointment firstAssignment = result.get(0);
        assertEquals(1, firstAssignment.getId(), "Den første aftale burde have ID 1");

        assertNotNull(firstAssignment.getEmployee(), "Frisør-objektet må ikke være null");
        assertEquals(1, firstAssignment.getEmployee().getId(), "Frisørens ID skal være 1");
        assertEquals("brian", firstAssignment.getEmployee().getUsername(), "Frisørens navn skal være 'brian'");

        assertNotNull(firstAssignment.getCustomer(), "Kunde-objektet må ikke være null");
        assertEquals(1, firstAssignment.getCustomer().getId(), "Kundens ID skal være 1");
        assertEquals("klaus", firstAssignment.getCustomer().getUsername(), "Kundens navn skal være 'klaus'");
    }


    @Test
    void testFindUnassignedAppointments() {
        List<Appointment> result = appointmentRepo.findUnassignedAppointments();

        assertNotNull(result, "Listen må ikke være null");
        assertFalse(result.isEmpty(), "Listen burde indeholde mindst en unassigned aftale");
        Appointment unassignedApp = result.get(0);

        boolean missingEmployee = (unassignedApp.getEmployee() == null || unassignedApp.getEmployee().getId() == 0);
        assertTrue(missingEmployee, "Aftalen skal være unassigned (uden medarbejder)");

        Appointment firstAssignment = result.get(0);
        assertNotNull(firstAssignment.getCustomer(), "Kunde-objektet må ikke være null");
        assertEquals(2, firstAssignment.getCustomer().getId(), "Kundens ID skal være 2");
        assertEquals("morten", firstAssignment.getCustomer().getUsername(), "Kundens navn skal være 'morten'");

    }

    @Test
    void testCreateAppointment() throws SQLException {
        Customer testCustomer = new Customer(1, "testkunde", "",  12345678);
        Employee testEmployee = new Employee(1, "testmedarbejder", "", 87654321);
        Treatment testTreatment = new Treatment(1, "testbehandling", 30, true);
        LocalDateTime startDate = LocalDateTime.of(2030, 1, 1, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2030, 1, 1, 10, 30);

        Appointment testAppointment = new Appointment(0, testCustomer, testEmployee, testTreatment, true, startDate, endDate);

        int rowsAffected = appointmentRepo.createAppointment(testAppointment);
        assertEquals(1, rowsAffected, "Oprettelsen af aftalen skulle påvirke 1 række i databasen");


        List<Appointment> briansAppointments = appointmentRepo.findAppointmentsByEmployee(1);
        Appointment savedApp = null;
        for (Appointment aftale : briansAppointments) {
            if (aftale.getStartDate().getYear() == 2030) {
                savedApp = aftale;
                break;
            }
        }

        assertNotNull(savedApp, "Aftalen fra år 2030 burde kunne findes i databasen");

        assertEquals(testCustomer.getId(), savedApp.getCustomer().getId(), "Kundens ID stemmer ikke");
        assertEquals(testEmployee.getId(), savedApp.getTreatment().getId(), "Behandlingens ID stemmer ikke");
        assertEquals(true, savedApp.getAppStatus(), "Status burde være true");

    }

    @Test
    void testReaasignAppointment() {
        int testAppointmentId = 1;
        int originalEmployeeId = 1; // Brians ID
        int newEmployeeId = 2; // Mettes ID

        List<Appointment> allAppointmentsBefore = appointmentRepo.findAllAppointments();
        Appointment appBefore = null;
        for (Appointment aftale : allAppointmentsBefore) {
            if (aftale.getId() == testAppointmentId) {
                appBefore = aftale;
                break;
            }
        }

        assertNotNull(appBefore, "Aftalen burde findes i databasen");
        assertEquals(originalEmployeeId, appBefore.getEmployee().getId(), "Aftalen skal starte med at tilhøre ID 1");
        assertEquals("brian", appBefore.getEmployee().getUsername(), "Aftalen skal starte med at tilhøre 'brian'");

        appointmentRepo.reassignAppointment(testAppointmentId, newEmployeeId);
        List<Appointment> allAppointments = appointmentRepo.findAllAppointments();
        Appointment updatedAppointment = null;

        for (Appointment app : allAppointments) {
            if (app.getId() == testAppointmentId) {
                updatedAppointment = app;
                break;
            }
        }
        assertNotNull(updatedAppointment, "Aftalen burde stadig findes i databasen");
        assertEquals(newEmployeeId, updatedAppointment.getEmployee().getId(), "Medarbejder-ID burde nu være 2");
        assertEquals("mette", updatedAppointment.getEmployee().getUsername(), "Navnet på aftalen burde nu være 'mette'!");

        // Ryd op - sæt aftalen tilbage til Brian
        appointmentRepo.reassignAppointment(testAppointmentId, 1);
    }

    @Test
    void testAssignUnassignedAppointment() throws SQLException {
        List<Appointment> unassignedAppointments = appointmentRepo.findUnassignedAppointments();

        assertFalse(unassignedAppointments.isEmpty(), "Der skal være mindst én ufordelt aftale i databasen for at køre testen");

        Appointment unassignedApp = unassignedAppointments.get(0);
        int testAppointmentId = unassignedApp.getId();
        int nyEmployeeId = 2;

        boolean missingEmployee = (unassignedApp.getEmployee() == null || unassignedApp.getEmployee().getId() == 0);
        assertTrue(missingEmployee, "Aftalen skal starte med at være ufordelt (NULL)");

        appointmentRepo.reassignAppointment(testAppointmentId, nyEmployeeId);

        List<Appointment> allAppAfter = appointmentRepo.findAllAppointments();
        Appointment updatedApp = null;
        for (Appointment app : allAppAfter) {
            if (app.getId() == testAppointmentId) {
                updatedApp = app;
                break;
            }
        }

        assertNotNull(updatedApp, "Aftalen burde stadig findes i databasen");
        assertEquals(nyEmployeeId, updatedApp.getEmployee().getId(), "Aftalen skal nu have Mettes ID (2)");
        assertEquals("mette", updatedApp.getEmployee().getUsername(), "Navnet på aftalen burde nu være 'mette'");

        // Ryd op - sæt aftalen tilbage til ufordelt
        String sql = "UPDATE appointment SET employeeid = NULL WHERE id = ?";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, testAppointmentId);
            ps.executeUpdate();
        }
    }

    @Test
    void testUpdateAppointment() {

        List<Appointment> allAppointments = appointmentRepo.findAllAppointments();
        Appointment appointmentForUpdate = null;
        for (Appointment app : allAppointments) {
            if (app.getId() == 1) {
                appointmentForUpdate = app;
                break;
            }
        }
        assertNotNull(appointmentForUpdate, "Aftale 1 skal findes i databasen for at testen kan køre");

        LocalDateTime originalStart = appointmentForUpdate.getStartDate();
        LocalDateTime originalEnd = appointmentForUpdate.getEndDate();
        Treatment originalTreatment = appointmentForUpdate.getTreatment();


        LocalDateTime newStart = LocalDateTime.of(2027, 5, 10, 14, 0);
        LocalDateTime newEnd = LocalDateTime.of(2027, 5, 10, 14, 45);
        Treatment newTreatment = new Treatment(2, "BuzzCut", 30, true);

        appointmentForUpdate.setStartDate(newStart);
        appointmentForUpdate.setEndDate(newEnd);
        appointmentForUpdate.setTreatment(newTreatment);
        appointmentRepo.updateAppointment(appointmentForUpdate);

        List<Appointment> appointmentAfterUpdate = appointmentRepo.findAllAppointments();
        Appointment updatedAppointment = null;
        for (Appointment aftale : appointmentAfterUpdate) {
            if (aftale.getId() == 1) {
                updatedAppointment = aftale;
                break;
            }
        }

        assertNotNull(updatedAppointment, "Aftalen skal stadig findes efter opdateringen");

        assertEquals(newStart, updatedAppointment.getStartDate(), "Startdatoen er ikke blevet opdateret i databasen");
        assertEquals(2, updatedAppointment.getTreatment().getId(), "Behandlingens ID burde nu være 2 (BuzzCut)");

        // Ryd op - sæt aftalen tilbage til originalt tidspunkt og behandling
        updatedAppointment.setStartDate(originalStart);
        updatedAppointment.setEndDate(originalEnd);
        updatedAppointment.setTreatment(originalTreatment);
        appointmentRepo.updateAppointment(updatedAppointment);
    }

}