package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Repoistory.AppointmentRepository;

import java.sql.SQLException;

public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    //
    public AppointmentService(AppointmentRepository appointmentRepo) {
        this.appointmentRepository = appointmentRepo;
    }

    // Create appointment
    public void handleCreateAppointment(int customerid, int employeeid, int treatmentid, boolean appstatus, String startdate, String enddate) {
        try{
            appointmentRepository.createAppointment(customerid, employeeid,treatmentid,appstatus,startdate,enddate);
        }catch (SQLException e){
            //Custom exception?
        }
    }

    // Cancel appointment
    public void handleCancelAppointment(int appointmentID) {
        appointmentRepository.cancelAppointment(appointmentID);
    }

    // Update appointment
    public void handleUpdateAppointment(Appointment appointment) {
        appointmentRepository.updateAppointment(appointment);
    }

    // Check appointment conflicts
    public void handleCheckConflict(int id, String startdate, String enddate) {
       try{
           appointmentRepository.checkConflict(id, startdate, enddate);
       } catch (SQLException e){
           //Custom exception
       }

    }

}
