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

    public List<Appointment> findAppointmentsByEmployee(int employeeId) {
        String sql = "SELECT a.id AS app_id, a.appstatus, a.startdate, a.enddate, " +
                "c.id AS c_id, c.username AS c_username, c.phonenumber AS c_phone, " +
                "e.id AS e_id, e.username AS e_username, e.phonenumber AS e_phone, " +
                "t.id AS t_id, t.typeoftreatment, t.duration, t.isactive " +
                "FROM appointment a " +
                "JOIN customer c ON a.customerid = c.id " +
                "JOIN employee e ON a.employeeid = e.id " +
                "JOIN treatment t ON a.treatmentid = t.id " +
                "WHERE a.employeeid = ?";

        List<Appointment> results = new ArrayList<>();
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke hente aftaler for medarbejder med ID: " + employeeId, e);
        }
        return results;
    }

    public List<Appointment> findAllAppointments() {
        String sql = "SELECT a.id AS app_id, a.appstatus, a.startdate, a.enddate, " +
                "c.id AS c_id, c.username AS c_username, c.phonenumber AS c_phone, " +
                "e.id AS e_id, e.username AS e_username, e.phonenumber AS e_phone, " +
                "t.id AS t_id, t.typeoftreatment, t.duration, t.isactive " +
                "FROM appointment a " +
                "JOIN customer c ON a.customerid = c.id " +
                "LEFT JOIN employee e ON a.employeeid = e.id " +
                "JOIN treatment t ON a.treatmentid = t.id";

        List<Appointment> results = new ArrayList<>();
        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                results.add(mapRow(rs));
            }
        } catch (Exception e){
            throw new DataAccessException("Fejl ved find all appointments", e);
        }
        return results;
    }

    public List<Appointment> findUnassignedAppointments() {
        String sql = "SELECT a.id AS app_id, a.appstatus, a.startdate, a.enddate, " +
                "c.id AS c_id, c.username AS c_username, c.phonenumber AS c_phone, " +
                "e.id AS e_id, e.username AS e_username, e.phonenumber AS e_phone, " +
                "t.id AS t_id, t.typeoftreatment, t.duration, t.isactive " +
                "FROM appointment a " +
                "JOIN customer c ON a.customerid = c.id " +
                "LEFT JOIN employee e ON a.employeeid = e.id " +
                "JOIN treatment t ON a.treatmentid = t.id " +
                "WHERE a.employeeid IS NULL";

        List<Appointment> results = new ArrayList<>();
        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                results.add(mapRow(rs));
            }
        } catch (Exception e){
            throw new DataAccessException("Fejl ved find unassigned", e);
        }
        return results;
    }


    // Create appointment SQL
    public int createAppointment(Appointment appointment) throws SQLException {
        String sql = "Insert into appointment (customerid, employeeid,treatmentid,startdate,enddate) values (?,?,?,?,?)";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, appointment.getCustomer().getId());
            ps.setInt(2, appointment.getEmployee().getId());
            ps.setInt(3, appointment.getTreatment().getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(4, appointment.getStartDate().format(formatter));
            ps.setString(5, appointment.getEndDate().format(formatter));

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved oprettelse af aftale", e);
        }
    }


    public void reassignAppointment(int appointmentId, int newEmployeeId) {
        String sql = "UPDATE appointment SET employeeid = ? WHERE id = ?";
        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, newEmployeeId);
            ps.setInt(2, appointmentId);
            ps.executeUpdate();
        } catch (Exception e){
            throw new DataAccessException("Kunne ikke overdrage aftalen", e);
        }
    }


    public void updateAppointment(Appointment appointment){
        String sql = "UPDATE appointment SET startdate = ?, enddate = ?, treatmentid = ? WHERE id = ?";

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(1, appointment.getStartDate().format(formatter));
            ps.setString(2, appointment.getEndDate().format(formatter));

            ps.setInt(3, appointment.getTreatment().getId());
            ps.setInt(4, appointment.getId());

            ps.executeUpdate();

        }catch (SQLException e){
            throw new DataAccessException("Kunne ikke opdatere aftalens tidspunkt eller behandling", e);
        }
    }


    public void cancelAppointment(int appointmentId){
        String sql = "UPDATE appointment SET appstatus = false WHERE id = ?";

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, appointmentId);
            ps.executeUpdate();

        }catch (SQLException e){
            throw new DataAccessException("Kunne ikke aflyse aftalen", e);
        }
    }


    public boolean checkForConflict(Appointment newAppointment) {
        String sql = "SELECT COUNT(*) FROM appointment " +
                "WHERE employeeid = ? " +
                "AND appstatus = true " +
                "AND startdate < ? AND enddate > ? " +
                "AND id != ?";

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, newAppointment.getEmployee().getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(2, newAppointment.getEndDate().format(formatter));
            ps.setString(3, newAppointment.getStartDate().format(formatter));
            ps.setInt(4, newAppointment.getId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int numberOfConflicts = rs.getInt(1);
                return numberOfConflicts > 0;
            }

        }catch (SQLException e){
            throw new DataAccessException("Kunne ikke tjekke for tids-konflikter", e);
        }

        return false;
    }

    private Appointment mapRow(ResultSet rs) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Customer customer = new Customer(
                rs.getInt("c_id"),
                rs.getString("c_username"),
                "",
                rs.getInt("c_phone")
        );
        Treatment treatment = new Treatment(
                rs.getInt("t_id"),
                rs.getString("typeoftreatment"),
                rs.getInt("duration"),
                rs.getBoolean("isactive")
        );
        Employee employee = null;
        int empId = rs.getInt("e_id");
        if (!rs.wasNull()) {
            employee = new Employee(
                    empId,
                    rs.getString("e_username"),
                    "",
                    rs.getInt("e_phone"));
        }
        String startDateStr = rs.getString("startdate").replace("T", " ");
        String endDateStr = rs.getString("enddate").replace("T", " ");

        return new Appointment(
                rs.getInt("app_id"),
                customer,
                employee,
                treatment,
                rs.getBoolean("appstatus"),
                LocalDateTime.parse(startDateStr, formatter),
                LocalDateTime.parse(endDateStr, formatter)
        );
    }
}


