package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Database.DataAccessException;
import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Model.Treatment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {
    private final DB db;

    // DB
    public AppointmentRepository(DB db) {
        this.db = db;
    }

    // Find all appointments
    public List<Appointment> findAll() {
        String sql = "Select employee, customer, treatment, From appointment order by employee";

        List<Appointment> results = new ArrayList<>();

        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                results.add(mapRow(rs));
            }
            return results;

        }catch (Exception e){
            throw new DataAccessException("Fejl ved find all");
        }
    }

    // Create appointment SQL
    public int createAppointment(int customerid, int employeeid, int treatmentid, boolean appstatus, String startdate, String enddate) throws SQLException {
        String sql = "Insert into appointment (customerid, employeeid,treatmentid,appstatus,startdate,enddate) values ?,?,?,?,?,? ";

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){

            ps.setInt(1, customerid);
            ps.setInt(2, employeeid);
            ps.setInt(3, treatmentid);
            ps.setBoolean(4, appstatus);
            ps.setString(5, startdate);
            ps.setString(6, enddate);
            int rows = ps.executeUpdate();

            return rows;
        } // lav catch
    }

    // Update appointment SQL
    public void updateAppointment(Appointment appointment){
        String sql = "Update appointment Set startdate = ?, endDate = ?, treatment = ? where id = ?";

        try(PreparedStatement ps = db.getConnection().prepareStatement(sql)){
            ps.setString(1, appointment.getStartdate().toString());
            ps.setString(2, appointment.getEndDate().toString());
            ps.setString(3, appointment.getTreatment().toString());
        }catch (SQLException e){
            //måske en custom exception
        }
    }

    // Cancel appointment SQL
    public void cancelAppointment(int appointment){
        String sql = "";
    }

    // Check appointment conflict SQL
    public void checkConflict(int appointmentID) {
        String sql = "";
    }


    // Retrieves the columns SQL
    private Appointment mapRow(ResultSet rs) throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



        Customer customer = new Customer(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("phone number")
        );
        Employee employee = new Employee(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("phone number")
        );
        Treatment treatment = new Treatment(
                rs.getInt("id"),
                rs.getString("type oft reatment"),
                rs.getInt("duration")
        );
        return new Appointment(
                rs.getInt("id"),
                customer,
                employee,
                treatment,
                rs.getBoolean("appstatus"),
                LocalDateTime.parse(rs.getString("start date"), formatter),
                LocalDateTime.parse(rs.getString("end date"))

        );
    }

}
