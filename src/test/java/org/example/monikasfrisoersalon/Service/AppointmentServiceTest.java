package org.example.monikasfrisoersalon.Service;

import static org.junit.jupiter.api.Assertions.*;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.*;
import org.example.monikasfrisoersalon.Repository.AppointmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

class AppointmentServiceTest {

    private DB db;
    private AppointmentRepository appointmentRepo;
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        db = new DB();
        appointmentRepo = new AppointmentRepository(db);
        appointmentService = new AppointmentService(appointmentRepo); // Her sætter vi Servicen op!
    }

    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM appointment WHERE startdate >= '2030-01-01 00:00:00' OR YEAR(startdate) = 2018";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Kunne ikke rydde op i databasen: " + e.getMessage());
        }
    }

    @Test
    void testCreateAppointmentService() throws SQLException {
        Customer testCustomer = new Customer(1, "testkunde", "", 12345678);
        Employee testEmployee = new Employee(1, "testmedarbejder", "", 87654321);
        Treatment testTreatment = new Treatment(1, "testbehandling", 30, 150, true);
        LocalDateTime startDate = LocalDateTime.of(2030, 1, 1, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2030, 1, 1, 10, 30);

        Appointment testAppointment = new Appointment(0, testCustomer, testEmployee, testTreatment, true, startDate, endDate);

        appointmentService.createAppointment(testAppointment); // 2. Gem aftalen via Servicen

        // 3. Hent alle aftaler for medarbejderen og find den nye aftale
        List<Appointment> briansAppointments = appointmentRepo.findAppointmentsByEmployee(1);
        Appointment savedApp = null;
        for (Appointment aftale : briansAppointments) {
            if (aftale.getStartDate().getYear() == 2030) {
                savedApp = aftale;
                break;
            }
        }

        assertNotNull(savedApp, "Aftalen fra år 2030 burde kunne findes i databasen efter Servicen har håndteret den");
        assertEquals(testCustomer.getId(), savedApp.getCustomer().getId(), "Kundens ID stemmer ikke");
        assertEquals(testEmployee.getId(), savedApp.getEmployee().getId(), "Medarbejderens ID stemmer ikke");
    }


    @Test
    void testUpdateAppointmentThroughService() throws SQLException {
        List<Appointment> allAppointments = appointmentRepo.findAllAppointments();
        Appointment appointmentForUpdate = null;
        for (Appointment app : allAppointments) {
            if (app.getId() == 1) {
                appointmentForUpdate = app;
                break;
            }
        }
        // Vi forventer at der findes en aftale med ID 1 i databasen for at denne test kan køre
        assertNotNull(appointmentForUpdate, "Aftale 1 skal findes i databasen for at testen kan køre");

        // Gem de originale værdier for at kunne rydde op senere
        LocalDateTime originalStart = appointmentForUpdate.getStartDate();
        LocalDateTime originalEnd = appointmentForUpdate.getEndDate();
        Treatment originalTreatment = appointmentForUpdate.getTreatment();

        LocalDateTime newStart = LocalDateTime.of(2027, 5, 10, 14, 0);
        LocalDateTime newEnd = LocalDateTime.of(2027, 5, 10, 14, 45);
        Treatment newTreatment = new Treatment(2, "BuzzCut", 30, 150, true);

        // 3. Opdater aftalen med nye værdier
        appointmentForUpdate.setStartDate(newStart);
        appointmentForUpdate.setEndDate(newEnd);
        appointmentForUpdate.setTreatment(newTreatment);

        appointmentService.updateAppointment(appointmentForUpdate); // 4. Opdater aftalen via Servicen

        // 5. Hent aftalen igen og tjek at ændringerne er gemt
        List<Appointment> appointmentsAfterUpdate = appointmentRepo.findAllAppointments();
        Appointment updatedAppointment = null;
        for (Appointment aftale : appointmentsAfterUpdate) {
            if (aftale.getId() == 1) {
                updatedAppointment = aftale;
                break;
            }
        }

        assertNotNull(updatedAppointment, "Aftalen skal stadig findes efter opdateringen");
        assertEquals(newStart, updatedAppointment.getStartDate(), "Startdatoen blev ikke opdateret via Servicen");
        assertEquals(2, updatedAppointment.getTreatment().getId(), "Behandlingens ID burde nu være 2 (BuzzCut)");

        // 6. Ryd op - sæt aftalen tilbage til originalt tidspunkt og behandling
        updatedAppointment.setStartDate(originalStart);
        updatedAppointment.setEndDate(originalEnd);
        updatedAppointment.setTreatment(originalTreatment);
        appointmentRepo.updateAppointment(updatedAppointment);
    }
}