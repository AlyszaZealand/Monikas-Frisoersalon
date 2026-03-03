package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
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
    public List<Administrator> FindAdministrators(){
        String sql = "select username,password from administrator";

        List<Administrator> result = new ArrayList<>();

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");

                Administrator admin = new Administrator(username, password);
                result.add(admin);
            }

        }catch (SQLException e){
            //måske custom exception
        }
        return result;
    }



}
