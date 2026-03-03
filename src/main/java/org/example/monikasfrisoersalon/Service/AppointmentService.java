package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Repository.AppointmentRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {

    private final AppointmentRepository appointmentRepo;


    public AppointmentService(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    public void validateAppointment(Appointment appointment) {
        // Validering: Starttidspunkt skal være før sluttidspunkt
        if (appointment.getStartDate().isAfter(appointment.getEndDate()) ||
                appointment.getStartDate().isEqual(appointment.getEndDate())) {
            throw new IllegalArgumentException("Fejl: Sluttidspunktet skal være efter starttidspunktet.");
        }

        // Validering: Tjek for overlap med eksisterende aftaler for samme medarbejder
        boolean hasConflict = appointmentRepo.checkForConflict(appointment);
        if (hasConflict) {
            throw new IllegalStateException("Fejl: Medarbejderen er allerede booket i dette tidsrum.");
        }
    }

    public void createAppointment(Appointment appointment) {
        validateAppointment(appointment);
        try {
            appointmentRepo.createAppointment(appointment);
        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke gemme aftalen i databasen", e);
        }
    }

    public void updateAppointment(Appointment appointment) {
        validateAppointment(appointment);
        try {
            appointmentRepo.updateAppointment(appointment);
        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke gemme aftalen i databasen", e);
        }
    }

    public void handleReassignEmployeeToAppointment(int appointmentId, int employeeId) {
        // Fetch the existing appointment
        Appointment appointment = appointmentRepo.findById(appointmentId);
        if (appointment == null) {
            throw new IllegalArgumentException("Aftalen findes ikke");
        }

        // Set the new employee so checkForConflict uses the right employee ID
        appointment.getEmployee().setId(employeeId);

        boolean hasConflict = appointmentRepo.checkForConflict(appointment);
        if (hasConflict) {
            throw new IllegalStateException("Medarbejderen er allerede booket i dette tidsrum.");
        }

        appointmentRepo.reassignAppointment(appointmentId, employeeId);
    }


    public void handleFindUnassignedAppointment(){
        appointmentRepo.findUnassignedAppointments();
    }

    public void cancelAppointment(int appointmentId) {
        appointmentRepo.cancelAppointment(appointmentId);
    }


    public void cleanupOldAppointments(LocalDateTime cutoffDate) {
        appointmentRepo.deleteAppointmentsOlderThan(cutoffDate);
    }


    public int getTotalRevenue() {
        return appointmentRepo.calculateTotalRevenue();
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAllAppointments();
    }

    public List<Appointment> getAppointmentsForEmployee(int employeeId) {
        return appointmentRepo.findAppointmentsByEmployee(employeeId);
    }

}
