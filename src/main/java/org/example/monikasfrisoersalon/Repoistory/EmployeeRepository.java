package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Administrator;
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
    public void createEmployee(String username, String password, int phonenumber) {
        String sql = "Insert into employee (username,password,phonenumber) values (?,?,?)";

        try(Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,username);
            ps.setString(2,password);
            ps.setInt(3,phonenumber);

            ps.executeUpdate();

        }catch (SQLException e){
            //custom exception??+ maybe??+...
        }


    }

    // Delete employee SQL
    public void deleteEmployee(int id) {
        String sql = "Delete  from employee where employeeID=?";

        try(Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e){
            //Custom exception???
        }

    }

    // Find employees, select skal updateres der er nok måske en specifik til login oplysninger???
    public List<Employee> FindEmployees(){
        String sql = "select username,password from employee";

        List<Employee> result = new ArrayList<>();

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");

                Employee employee = new Employee(username, password);
                result.add(employee);
            }

        }catch (SQLException e){
            //måske custom exception
        }
        return result;
    }
    // Retrieves the columns SQL
    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("phoneNumber")
        );
    }

}
