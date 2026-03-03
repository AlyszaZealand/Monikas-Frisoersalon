package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Appointment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class AppointmentRepositoryTest {

    private DB db;
    private AppointmentRepository repo;

    @BeforeEach
    void setUp() {
        db = new DB();
        repo = new AppointmentRepository(db);
    }

    @Test
    void testFindAllAppointments() {
        List<Appointment> appointments = repo.findAll();

        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());

        Appointment  appointment = appointments.get(0);

        assertEquals("brian", appointment.getEmployee().getUsername(), "frisør på første appointment skal være brian");
        assertEquals("klaus",appointment.getCustomer().getUsername(),"navn på første kunde");
        assertEquals(34212343, appointment.getCustomer().getPhoneNumber(),"klaus' nummer skal være 34212343");
        assertEquals("Wash & brush", appointment.getTreatment().getTypeOfTreatment(),"første appointment type skal være wash & brush");
        assertEquals(150, appointment.getTreatment().getPrice(),"regner med at få 150");

    }

    // appointment(customerid, employeeid, treatmentid, appstatus, startdate, enddate)
    // VALUES (1, 1, 1, true, '2026-02-27 12:00:00', '2026-02-27 12:30:00');

  
}