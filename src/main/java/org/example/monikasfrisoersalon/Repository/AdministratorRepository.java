package org.example.monikasfrisoersalon.Repository;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Database.DataAccessException;
import org.example.monikasfrisoersalon.Model.Administrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministratorRepository {
    private final DB db;

    // DB
    public AdministratorRepository(DB db) {
        this.db = db;
    }

    // Liste af Administratorer
    public List<Administrator> findAdministrators(){
        String sql = "select id, username, password, phonenumber from administrator";

        List<Administrator> result = new ArrayList<>();

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                result.add(mapRow(rs));
            }

        }catch (SQLException e){
            throw new DataAccessException("Kunne ikke hente administratorer", e);
        }
        return result;
    }

    private Administrator mapRow(ResultSet rs) throws SQLException {

        return new Administrator(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("phonenumber")
        );
    }




}
