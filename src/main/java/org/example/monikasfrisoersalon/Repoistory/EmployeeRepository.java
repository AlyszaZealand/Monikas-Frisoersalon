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
    public void createEmployee(int employeeID) {
        String sql = "";
    }

    // Delete employee SQL
    public void deleteEmployee(int employeeID) {
        String sql = "";
    }

    // Find employees
    public List<Employee> FindEmployees(){
        String sql = "select username,password from administrator";

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
