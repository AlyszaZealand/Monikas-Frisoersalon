package org.example.monikasfrisoersalon.Repository;

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
        String sql = "DELETE FROM appointment WHERE startdate >= '2030-01-01 00:00:00' OR YEAR(startdate) = 2018";

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
        Treatment testTreatment = new Treatment(1, "testbehandling", 30, 150,true);
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

        appointmentRepo.reassignAppointment(testAppointmentId, newEmployeeId); // Reassign aftale 1 til medarbejder 2 (mette)
        List<Appointment> allAppointments = appointmentRepo.findAllAppointments(); // Hent alle aftaler igen for at tjekke opdateringen
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

        boolean missingEmployee = (unassignedApp.getEmployee() == null || unassignedApp.getEmployee().getId() == 0); // Tjek at aftalen starter som ufordelt (uden medarbejder)
        assertTrue(missingEmployee, "Aftalen skal starte med at være ufordelt (NULL)");

        appointmentRepo.reassignAppointment(testAppointmentId, nyEmployeeId); // Tildel den ufordelte aftale til medarbejder 2 (mette)

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

        LocalDateTime originalStart = appointmentForUpdate.getStartDate(); // Gem original startdato for at kunne rydde op efter testen
        LocalDateTime originalEnd = appointmentForUpdate.getEndDate(); // Gem original slutdato for at kunne rydde op efter testen
        Treatment originalTreatment = appointmentForUpdate.getTreatment(); // Gem original behandling for at kunne rydde op efter testen


        LocalDateTime newStart = LocalDateTime.of(2027, 5, 10, 14, 0);
        LocalDateTime newEnd = LocalDateTime.of(2027, 5, 10, 14, 45);
        Treatment newTreatment = new Treatment(2, "BuzzCut", 30, 150,true);

        appointmentForUpdate.setStartDate(newStart); // Opdater startdatoen for aftalen
        appointmentForUpdate.setEndDate(newEnd); // Opdater slutdatoen for aftalen
        appointmentForUpdate.setTreatment(newTreatment); // Opdater behandlingen for aftalen
        try{
            appointmentRepo.updateAppointment(appointmentForUpdate); // Gem ændringerne i databasen
        } catch (SQLException e){
            e.printStackTrace();
        }

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
        try{
            appointmentRepo.updateAppointment(updatedAppointment);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Test
    void testCancelAppointment() {
        int testAppointmentId = 1;

        // Først tjekker vi at aftalen starter som aktiv (appstatus = true)
        List<Appointment> allAppointments = appointmentRepo.findAllAppointments();
        Appointment appointmentToCancel = null;
        for (Appointment app : allAppointments) {
            if (app.getId() == testAppointmentId) {
                appointmentToCancel = app;
                break;
            }
        }
        assertNotNull(appointmentToCancel, "Aftalen skal findes i databasen for at testen kan køre");
        assertTrue(appointmentToCancel.getAppStatus(), "Aftalen skal starte med at være aktiv (appstatus = true)");

        // Nu aflyser vi aftalen
        appointmentRepo.cancelAppointment(testAppointmentId);

        // Tjek at aftalen stadig findes i databasen og at appstatus nu er false
        List<Appointment> appointmentsAfter = appointmentRepo.findAllAppointments();
        Appointment cancelledAppointment = null;
        for (Appointment app : appointmentsAfter) {
            if (app.getId() == testAppointmentId) {
                cancelledAppointment = app;
                break;
            }
        }
        assertNotNull(cancelledAppointment, "Aftalen skal stadig findes i databasen efter aflysning");
        assertFalse(cancelledAppointment.getAppStatus(), "Aftalen skulle nu være aflyst (appstatus = false)");


        // Ryd op - sæt aftalen tilbage til aktiv
        String sql = "UPDATE appointment SET appstatus = true WHERE id = ?";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, testAppointmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Kunne ikke genaktivere aftalen: " + e.getMessage());
        }
    }


    @Test
    void testDeleteAppointmentsOlderThan() throws SQLException {
        Customer testCustomer = new Customer(1, "klaus", "", 12345678);
        Employee testEmployee = new Employee(1, "brian", "", 12345678);
        Treatment testTreatment = new Treatment(1, "Wash & brush", 30, 150,true);
        LocalDateTime oldStartDate = LocalDateTime.of(2020, 1, 1, 10, 0);
        LocalDateTime oldEndDate = LocalDateTime.of(2020, 1, 1, 10, 30);
        Appointment oldAppointment = new Appointment(0, testCustomer, testEmployee, testTreatment, true, oldStartDate, oldEndDate);

        appointmentRepo.createAppointment(oldAppointment); // Opret en gammel aftale fra 2020, som burde blive slettet af deleteAppointmentsOlderThan
        LocalDateTime cutoffDate = LocalDateTime.of(2022, 1, 1, 0, 0); // Definer cutoff-datoen til 1. januar 2022

        int deletedRows = appointmentRepo.deleteAppointmentsOlderThan(cutoffDate); // Slet alle aftaler der starter før 1. januar 2022
        assertTrue(deletedRows >= 1, "Der skulle have været mindst én aftale, der blev slettet (den fra 2020)");

        List<Appointment> allAppointments = appointmentRepo.findAllAppointments();
        boolean foundOld = false;
        boolean foundSchemaSeedAppointment = false;

        for (Appointment app : allAppointments) {
            if (app.getStartDate().getYear() == 2020) {
                foundOld = true;
            }
            if (app.getId() == 1) {
                foundSchemaSeedAppointment = true;
            }
        }

        assertFalse(foundOld, "Aftalen fra 2020 skulle have været slettet");

        assertTrue(foundSchemaSeedAppointment, "Aftalen fra SchemaSeed (ID 1) skulle stadig være i databasen, da den er nyere end cutoff-datoen");

        // Ryd op - slet den gamle aftale hvis den stadig findes
        String sql = "DELETE FROM appointment WHERE startdate = '2020-01-01 10:00:00'";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }


    @Test
    void testCheckForConflict() {
        List<Appointment> allAppointments = appointmentRepo.findAllAppointments();
        assertFalse(allAppointments.isEmpty(), "Der skal være mindst én aftale i databasen for at køre testen");

        // Vi tager den første aftale fra databasen (formentlig den med ID 1 fra SchemaSeed) og bruger dens data til at teste konfliktscenarier
        Appointment existingApp = allAppointments.get(0); // Tag den første aftale fra databasen (formentlig den med ID 1 fra SchemaSeed)
        Employee busyEmployee = existingApp.getEmployee(); // Den medarbejder der allerede har en aftale i databasen
        LocalDateTime existingStart = existingApp.getStartDate(); // Start- og slutdatoen for den eksisterende aftale, som vi vil teste konflikter imod
        LocalDateTime existingEnd = existingApp.getEndDate(); // Start- og slutdatoen for den eksisterende aftale, som vi vil teste konflikter imod

        // Opret testdata til de tre scenarier
        Customer testCustomer = new Customer(1, "klaus", "", 12345678);
        Treatment testTreatment = new Treatment(1, "Wash", 30, 150,true);

        // Vi skal også bruge en anden medarbejder for at teste scenarie C, hvor aftalen
        int otherEmployeeId = (busyEmployee.getId() == 1) ? 2 : 1;
        Employee freeEmployee = new Employee(otherEmployeeId, "other", "", 12345678);


        //test scenarie A: Opretter en aftale der overlapper med eksisterende aftale for samme medarbejder (skal give konflikt(return true))
        LocalDateTime conflictStart = existingStart.plusMinutes(5);
        LocalDateTime conflictEnd = existingEnd.plusMinutes(5);
        Appointment conflictingAppointment = new Appointment(0, testCustomer, busyEmployee, testTreatment, true, conflictStart, conflictEnd);

        //test scenarie B: Opretter en aftale der er helt adskilt i tid (skal ikke give konflikt(return false))
        LocalDateTime freeStart = existingStart.plusDays(7);
        LocalDateTime freeEnd = existingEnd.plusDays(7);
        Appointment freeAppointment = new Appointment(0, testCustomer, busyEmployee, testTreatment, true, freeStart, freeEnd);

        //test scenarie C: Opretter en aftale der overlapper i tid men for en anden medarbejder (skal ikke give konflikt(return false))
        Appointment otherEmployeeAppointment = new Appointment(0, testCustomer, freeEmployee, testTreatment, true, existingStart, existingEnd);

        // test scenarie A
        boolean hasConflict = appointmentRepo.checkForConflict(conflictingAppointment);
        assertTrue(hasConflict, "Scenarie A: Der burde være en konflikt når aftalen overlapper med en eksisterende aftale for samme medarbejder");

        // test scenarie B
        boolean isFree = appointmentRepo.checkForConflict(freeAppointment);
        assertFalse(isFree, "Scenarie B: Der burde ikke være en konflikt når aftalen er adskilt i tid fra eksisterende aftaler for samme medarbejder");

        // test scenarie C
        boolean otherIsFree = appointmentRepo.checkForConflict(otherEmployeeAppointment);
        assertFalse(otherIsFree, "Scenarie C: Der burde ikke være en konflikt når aftalen overlap");
    }

    @Test
    void testCalculateTotalRevenue() throws SQLException {
        int initialRevenue = appointmentRepo.calculateTotalRevenue();

        Customer testCustomer = new Customer(1, "DummyKunde", "pass", 12345678);
        Employee testEmployee = new Employee(1, "DummyFrisør", "pass", 12345678);
        Treatment testTreatment = new Treatment(1, "Wash & brush", 30, 150, true);

        // Opret en FULDFØRT tid i 2018 (appstatus = true).
        Appointment pastCompletedApp = new Appointment(0, testCustomer, testEmployee, testTreatment, true,
                LocalDateTime.of(2018, 5, 10, 10, 0),
                LocalDateTime.of(2018, 5, 10, 10, 30));
        appointmentRepo.createAppointment(pastCompletedApp);

        // Opret en AFLYST tid i 2018 (appstatus = false).
        Appointment pastCancelledApp = new Appointment(0, testCustomer, testEmployee, testTreatment, false,
                LocalDateTime.of(2018, 6, 11, 10, 0),
                LocalDateTime.of(2018, 6, 11, 10, 30));
        appointmentRepo.createAppointment(pastCancelledApp);

        int newTotalRevenue = appointmentRepo.calculateTotalRevenue();
        int expectedIncrease = 150;
        assertEquals(initialRevenue + expectedIncrease, newTotalRevenue,
                "Indtjeningen burde kun stige med prisen for den ene fuldførte tid fra 2018");
    }
}