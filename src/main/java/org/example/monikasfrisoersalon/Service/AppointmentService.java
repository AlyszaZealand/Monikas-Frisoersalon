package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Repoistory.AppointmentRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {

    private final AppointmentRepository appointmentRepo;


    public AppointmentService(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    public void createAppointment(Appointment appointment) {
        if (appointment.getStartDate().isAfter(appointment.getEndDate()) ||
                appointment.getStartDate().isEqual(appointment.getEndDate())) {
            throw new IllegalArgumentException("Fejl: Sluttidspunktet skal være efter starttidspunktet.");
        }

        boolean hasConflict = appointmentRepo.checkForConflict(appointment);
        if (hasConflict) {
            throw new IllegalStateException("Fejl: Medarbejderen er allerede booket i dette tidsrum.");
        }

        try {
            appointmentRepo.createAppointment(appointment);
        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke gemme aftalen i databasen", e);
        }
    }


    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAllAppointments();
    }

    public List<Appointment> getAppointmentsForEmployee(int employeeId) {
        return appointmentRepo.findAppointmentsByEmployee(employeeId);
    }


    public void updateAppointment(Appointment appointment) {
        if (appointment.getStartDate().isAfter(appointment.getEndDate())) {
            throw new IllegalArgumentException("Fejl: Sluttidspunktet skal være efter starttidspunktet.");
        }
        if (appointmentRepo.checkForConflict(appointment)) {
            throw new IllegalStateException("Fejl: Den opdaterede tid skaber en konflikt for medarbejderen.");
        }
        appointmentRepo.updateAppointment(appointment);
    }


    public void cancelAppointment(int appointmentId) {
        appointmentRepo.cancelAppointment(appointmentId);
    }


    public void cleanupOldAppointments(LocalDateTime cutoffDate) {
        appointmentRepo.deleteAppointmentsOlderThan(cutoffDate);
    }


    public int calculateTotalRevenue() {
        return appointmentRepo.calculateTotalRevenue();
    }
}
