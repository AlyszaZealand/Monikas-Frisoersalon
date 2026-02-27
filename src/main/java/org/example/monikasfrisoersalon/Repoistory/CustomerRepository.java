package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Database.DataAccessException;
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

    public List<Customer> findCustomers(){
        String sql = "select id, username, password, phonenumber from customer";

        List<Customer> result = new ArrayList<>();

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);

            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                result.add(mapRow(rs));
            }
        }catch (SQLException e){
            throw new DataAccessException("Kunne ikke hente kunder", e);
        }
        return result;
    }

    public void createCustomer(Customer customer) {
        String sql = "INSERT INTO customer (username, password, phonenumber) VALUES (?, ?, ?)";

        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, customer.getUsername());
            ps.setString(2, customer.getPassword());
            ps.setInt(3, customer.getPhoneNumber());
            ps.executeUpdate();

        } catch (SQLException e){
            throw new DataAccessException("Kunne ikke oprette den nye kunde", e);
        }
    }

    public void anonymizeCustomer(int customerId) {
        String sql = "UPDATE customer SET username = 'Anonym', password = 'GDPR_SLETTET', phonenumber = 0 WHERE id = ?";

        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, customerId);
            ps.executeUpdate();

        } catch (SQLException e){
            throw new DataAccessException("Kunne ikke anonymisere kunden", e);
        }
    }

    // Retrieves the columns SQL
    private Customer mapRow(ResultSet rs) throws SQLException{

        return  new Customer(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("phonenumber")
        );

    }

}
