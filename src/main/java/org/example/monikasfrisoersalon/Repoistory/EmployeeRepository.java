package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Database.DataAccessException;
import org.example.monikasfrisoersalon.Model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {
    private final DB db;

    // DB
    public EmployeeRepository(DB db) {
        this.db = db;
    }

    // Create employee SQL
    public void createEmployee(int employeeID) {
        String sql = "";
    }

    // Delete employee SQL
    public void deleteEmployee(int employeeID) {
        String sql = "";
    }

    // Find employees
    public List<Employee> findEmployees(){
        String sql = "select id, username, password, phonenumber from administrator";

        List<Employee> result = new ArrayList<>();

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()) {
                result.add(mapRow(rs));
            }

        }catch (SQLException e){
            throw new DataAccessException("Kunne ikke hente medarbejdere", e);
        }
        return result;
    }

    public void createEmployee(Employee employee) {
        String sql = "INSERT INTO employee (username, password, phonenumber) VALUES (?, ?, ?)";

        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, employee.getUsername());
            ps.setString(2, employee.getPassword());
            ps.setInt(3, employee.getPhoneNumber());

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DataAccessException("Kunne ikke oprette den nye medarbejder", e);
        }
    }

    public void deleteEmployeeSafely(int employeeId) {
        String removeAssignedSql = "UPDATE appointment SET employeeid = NULL WHERE employeeid = ?";
        String deleteEmployeeSql = "DELETE FROM employee WHERE id = ?";

        try(Connection con = db.getConnection()){
            PreparedStatement ps1 = con.prepareStatement(removeAssignedSql);
            ps1.setInt(1, employeeId);
            ps1.executeUpdate();


            PreparedStatement ps2 = con.prepareStatement(deleteEmployeeSql);
            ps2.setInt(1, employeeId);
            ps2.executeUpdate();
        } catch (Exception e){
            throw new DataAccessException("Fejl under sikker sletning af medarbejder", e);
        }
    }

    public void updatePassword(int employeeId, String newPassword) {
        String sql = "UPDATE employee SET password = ? WHERE id = ?";
        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, newPassword);
            ps.setInt(2, employeeId);
            ps.executeUpdate();
        } catch (Exception e){
            throw new DataAccessException("Kunne ikke opdatere adgangskode", e);
        }
    }



    // Retrieves the columns SQL
    private Employee mapRow(ResultSet rs) throws SQLException {

        return new Employee(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("phonenumber")
        );
    }

}
