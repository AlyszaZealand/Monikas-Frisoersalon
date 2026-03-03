package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private final DB db;

    // DB
    public CustomerRepository(DB db) {
        this.db = db;
    }

    public List<Customer> FindCustomers(){
        String sql = "select username,password from customer";

        List<Customer> result = new ArrayList<>();

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");

                Customer customer = new Customer(username, password);
                result.add(customer);
            }

        }catch (SQLException e){
            //måske custom exception
        }
        return result;
    }

    // Retrieves the columns SQL
    private Customer mapRow(ResultSet rs) throws SQLException{

        return  new Customer(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("phoneNumber")
        );

    }

}
