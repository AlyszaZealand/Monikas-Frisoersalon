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
        String sql = "SELECT a.id AS app_id, a.appstatus, a.startdate, a.enddate, " + // Tilføj startdate og enddate i SELECT for at kunne mappe dem korrekt
                "c.id AS c_id, c.username AS c_username, c.phonenumber AS c_phone, " + // Tilføj customer-felter
                "e.id AS e_id, e.username AS e_username, e.phonenumber AS e_phone, " + // Tilføj employee-felter (selvom de kan være NULL, skal de med i SELECT for at kunne mappe korrekt)
                "t.id AS t_id, t.typeoftreatment, t.duration, t.price, t.isactive " + // Tilføj treatment-felter
                "FROM appointment a " + // Start fra appointment-tabellen
                "JOIN customer c ON a.customerid = c.id " + // Join med customer for at få kundedata
                "LEFT JOIN employee e ON a.employeeid = e.id " + // Left join med employee for at få medarbejderdata (vil være NULL
                "JOIN treatment t ON a.treatmentid = t.id"; // Join med treatment for at få behandlingsdata

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
        String sql = "Insert into appointment (customerid, employeeid,treatmentid,appstatus,startdate,enddate) values (?,?,?,?,?,?) ";

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
    public void cancelAppointment(int id) throws SQLException {
        String sql = "";
    }

    // Check appointment conflict SQL skal nok rykkes til SERVICE??
    public void checkConflict(int id, String startdate, String enddate) throws SQLException {
        String sql = "Select count (*) from appointment where appointmentID = ? and status = 1 and startdate < ? and enddate > ? ";

        try(Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,id);
            ps.setString(2,startdate);
            ps.setString(3,enddate);
            ps.executeUpdate();
        }

    }


    // Retrieves the columns SQL
    private Appointment mapRow(ResultSet rs) throws SQLException{
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
                rs.getInt("price"),
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
