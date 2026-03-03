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
//    String sql = "SELECT a.id AS app_id, a.appstatus, a.startdate, a.enddate, " + // Tilføj startdate og enddate i SELECT for at kunne mappe dem korrekt
//            "c.id AS c_id, c.username AS c_username, c.phonenumber AS c_phone, " + // Tilføj customer-felter
//            "e.id AS e_id, e.username AS e_username, e.phonenumber AS e_phone, " + // Tilføj employee-felter (selvom de kan være NULL, skal de med i SELECT for at kunne mappe korrekt)
//            "t.id AS t_id, t.typeoftreatment, t.duration, t.price, t.isactive " + // Tilføj treatment-felter
//            "FROM appointment a " + // Start fra appointment-tabellen
//            "JOIN customer c ON a.customerid = c.id " + // Join med customer for at få kundedata
//            "JOIN employee e ON a.employeeid = e.id " + // Join med employee for at få medarbejderdata (vil være NULL
//            "JOIN treatment t ON a.treatmentid = t.id " + // Join med treatment for at få behandlingsdata
//            "WHERE a.employeeid = ?"
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
            ps.executeUpdate();
        }catch (SQLException e){
            //måske en custom exception
        }
    }

    // Cancel appointment SQL
    public void cancelAppointment(int id) throws SQLException {
        String sql = "Update appointment set appstatus = false where id = ?";

        try(Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);
            ps.executeUpdate();
        }
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
