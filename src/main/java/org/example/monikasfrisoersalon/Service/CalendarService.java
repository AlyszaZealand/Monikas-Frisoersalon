package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Model.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarService {

    private final AppointmentService appointmentService;

    public CalendarService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public List<Appointment> getAppointmentsForDate(LocalDate date) {
        // Hent alle aftaler og filtrer dem baseret på den angivne dato
        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        List<Appointment> dailyAppointments = new ArrayList<>();

        for (Appointment app : allAppointments) {
            if (app.getStartDate().toLocalDate().equals(date)) {
                dailyAppointments.add(app);
            }
        }
        return dailyAppointments;
    }


    public List<Appointment> getAppointmentsForEmployeeOnDate(Employee employee, LocalDate date) {
        // Hent alle aftaler for den specifikke medarbejder og filtrer dem baseret på den angivne dato
        List<Appointment> employeeAppointments = appointmentService.getAppointmentsForEmployee(employee.getId());
        List<Appointment> dailyEmployeeAppointments = new ArrayList<>();

        for (Appointment app : employeeAppointments) {
            if (app.getStartDate().toLocalDate().equals(date)) {
                dailyEmployeeAppointments.add(app);
            }
        }
        return dailyEmployeeAppointments;
    }
}
