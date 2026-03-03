package org.example.monikasfrisoersalon.Repository;

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

    public Appointment findById(int appointmentId) {
        String sql = "SELECT a.id AS app_id, a.appstatus, a.startdate, a.enddate, " +
                "c.id AS c_id, c.username AS c_username, c.phonenumber AS c_phone, " +
                "e.id AS e_id, e.username AS e_username, e.phonenumber AS e_phone, " +
                "t.id AS t_id, t.typeoftreatment, t.duration, t.price, t.isactive " +
                "FROM appointment a " +
                "JOIN customer c ON a.customerid = c.id " +
                "LEFT JOIN employee e ON a.employeeid = e.id " +
                "JOIN treatment t ON a.treatmentid = t.id " +
                "WHERE a.id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke finde aftale med ID: " + appointmentId, e);
        }
        return null;
    }

    public List<Appointment> findAppointmentsByEmployee(int employeeId) {
        String sql = "SELECT a.id AS app_id, a.appstatus, a.startdate, a.enddate, " + // Tilføj startdate og enddate i SELECT for at kunne mappe dem korrekt
                "c.id AS c_id, c.username AS c_username, c.phonenumber AS c_phone, " + // Tilføj customer-felter
                "e.id AS e_id, e.username AS e_username, e.phonenumber AS e_phone, " + // Tilføj employee-felter (selvom de kan være NULL, skal de med i SELECT for at kunne mappe korrekt)
                "t.id AS t_id, t.typeoftreatment, t.duration, t.price, t.isactive " + // Tilføj treatment-felter
                "FROM appointment a " + // Start fra appointment-tabellen
                "JOIN customer c ON a.customerid = c.id " + // Join med customer for at få kundedata
                "JOIN employee e ON a.employeeid = e.id " + // Join med employee for at få medarbejderdata (vil være NULL
                "JOIN treatment t ON a.treatmentid = t.id " + // Join med treatment for at få behandlingsdata
                "WHERE a.employeeid = ?"; // Filtrer for kun at få aftaler for den specifikke medarbejder

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
        } catch (Exception e){
            throw new DataAccessException("Fejl ved find all appointments", e);
        }
        return results;
    }

    public List<Appointment> findUnassignedAppointments() {
        String sql = "SELECT a.id AS app_id, a.appstatus, a.startdate, a.enddate, " + // Tilføj startdate og enddate i SELECT for at kunne mappe dem korrekt
                "c.id AS c_id, c.username AS c_username, c.phonenumber AS c_phone, " + // Tilføj customer-felter
                "e.id AS e_id, e.username AS e_username, e.phonenumber AS e_phone, " + // Tilføj employee-felter (selvom de vil være NULL for unassigned, skal de med i SELECT for at kunne mappe korrekt)
                "t.id AS t_id, t.typeoftreatment, t.duration, t.price, t.isactive " + // Tilføj treatment-felter
                "FROM appointment a " + // Start fra appointment-tabellen
                "JOIN customer c ON a.customerid = c.id " + // Join med customer for at få kundedata
                "LEFT JOIN employee e ON a.employeeid = e.id " + // Left join med employee for at få medarbejderdata (vil være NULL for unassigned)
                "JOIN treatment t ON a.treatmentid = t.id " + // Join med treatment for at få behandlingsdata
                "WHERE a.employeeid IS NULL"; // Filtrer for kun at få unassigned aftaler

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
        String sql = "Insert into appointment (customerid, employeeid,treatmentid, appstatus, startdate,enddate) values (?,?,?,?,?,?)";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, appointment.getCustomer().getId());
            ps.setInt(2, appointment.getEmployee().getId());
            ps.setInt(3, appointment.getTreatment().getId());
            ps.setBoolean(4, appointment.getAppStatus());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(5, appointment.getStartDate().format(formatter));
            ps.setString(6, appointment.getEndDate().format(formatter));

            return ps.executeUpdate();
        }
    }


    public void reassignAppointment(int appointmentId, int newEmployeeId) {
        String sql = "UPDATE appointment SET employeeid = ? WHERE id = ?"; // Opdater SQL for at ændre employeeid for en given appointment
        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, newEmployeeId);
            ps.setInt(2, appointmentId);
            ps.executeUpdate();
        } catch (Exception e){
            throw new DataAccessException("Kunne ikke overdrage aftalen", e);
        }
    }


    public void updateAppointment(Appointment appointment) throws SQLException{
        String sql = "UPDATE appointment SET startdate = ?, enddate = ?, treatmentid = ? WHERE id = ?";

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(1, appointment.getStartDate().format(formatter));
            ps.setString(2, appointment.getEndDate().format(formatter));
            ps.setInt(3, appointment.getTreatment().getId());
            ps.setInt(4, appointment.getId());

            ps.executeUpdate();

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


    public int deleteAppointmentsOlderThan(LocalDateTime cutoffDate) {
        String sql = "DELETE FROM appointment WHERE enddate < ?"; // Slet alle aftaler, hvor enddate er før cutoffDate

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {


            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(1, cutoffDate.format(formatter));

            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Kunne ikke slette gamle aftaler", e);
        }
    }


    public boolean checkForConflict(Appointment newAppointment) {
        String sql = "SELECT COUNT(*) FROM appointment " + // Tjek for overlapende aftaler for samme medarbejder
                "WHERE employeeid = ? " + // Tjek for samme medarbejder
                "AND appstatus = true " + // Tjek kun aktive aftaler
                "AND startdate < ? AND enddate > ? " + // Tjek for overlap (startdate før ny aftales enddate OG enddate efter ny aftales startdate)
                "AND id != ?"; // Udeluk den aftale, der opdateres (hvis det er en opdatering og ikke en ny aftale)

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


    public int calculateTotalRevenue() {
        String sql = "SELECT SUM(t.price) AS total_revenue " + // Beregn den samlede indtjening ved at summere prisen for alle behandlinger i afsluttede aftaler
                "FROM appointment a " + // Start fra appointment-tabellen
                "JOIN treatment t ON a.treatmentid = t.id " + // Join med treatment for at få prisen på hver behandling
                "WHERE a.appstatus = true AND a.enddate < NOW()"; // Filtrer for kun at inkludere afsluttede aftaler (appstatus = true og enddate før nu)

        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_revenue");
            }
        } catch (java.sql.SQLException e) {
            throw new DataAccessException("Kunne ikke beregne den samlede indtjening", e);
        }
        return 0;
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


