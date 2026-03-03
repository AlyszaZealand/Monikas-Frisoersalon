package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Repository.AppointmentRepository;
import org.example.monikasfrisoersalon.Database.DB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarServiceTest {

    private DB db;
    private AppointmentRepository appointmentRepo;
    private AppointmentService appointmentService;
    private CalendarService calendarService;

    @BeforeEach
    void setUp() {
        db = new DB();
        appointmentRepo = new AppointmentRepository(db);
        appointmentService = new AppointmentService(appointmentRepo);
        calendarService = new CalendarService(appointmentService);
    }


    @Test
    void testGetAppointmentsForDate() {
        LocalDate testDate = LocalDate.of(2026, 2, 27);

        // Vi ved fra SchemaSeed at der er en aftale den 27. feb 2026, så vi tester på det
        List<Appointment> dailyAppointments = calendarService.getAppointmentsForDate(testDate);

        assertNotNull(dailyAppointments, "Listen over aftaler må ikke være null");
        assertFalse(dailyAppointments.isEmpty(), "Der burde være mindst én aftale den 27. feb 2026 fra din SchemaSeed");
        assertEquals(testDate, dailyAppointments.get(0).getStartDate().toLocalDate(), "Aftalens dato matcher ikke søgekriteriet");
    }


    @Test
    void testGetAppointmentsForEmployeeOnDate() {
        // Vi ved fra SchemaSeed at Brian har en aftale den 27. feb 2026, så vi tester på det
        Employee brian = new Employee(1, "brian", "brian123", 98652387);
        LocalDate testDate = LocalDate.of(2026, 2, 27);

        List<Appointment> briansAppointments = calendarService.getAppointmentsForEmployeeOnDate(brian, testDate);

        assertNotNull(briansAppointments, "Listen må ikke være null");
        assertFalse(briansAppointments.isEmpty(), "Brian burde have sin aftale den 27. feb 2026");
        assertEquals(1, briansAppointments.get(0).getEmployee().getId(), "Aftalen burde tilhøre medarbejder ID 1 (Brian)");
    }
}