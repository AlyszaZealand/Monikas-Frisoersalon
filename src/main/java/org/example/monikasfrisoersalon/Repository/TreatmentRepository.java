package org.example.monikasfrisoersalon.Repository;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Database.DataAccessException;
import org.example.monikasfrisoersalon.Model.Treatment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TreatmentRepository {
    private final DB db;

    // DB
    public TreatmentRepository(DB db) {
        this.db = db;
    }

    public List<Treatment> findAllTreatments() {
        String sql = "SELECT id, typeoftreatment, duration, price, isactive FROM treatment";
        List<Treatment> results = new ArrayList<>();

        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                results.add(mapRow(rs));
            }
        } catch (SQLException e){
            throw new DataAccessException("Kunne ikke hente aktive behandlinger", e);
        }
        return results;
    }


    public List<Treatment> findActiveTreatments() {
        String sql = "SELECT id, typeoftreatment, duration, price, isactive FROM treatment WHERE isactive = true";
        List<Treatment> results = new ArrayList<>();

        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                results.add(mapRow(rs));
            }
        } catch (SQLException e){
            throw new DataAccessException("Kunne ikke hente aktive behandlinger", e);
        }
        return results;
    }

    public void createTreatment(Treatment treatment) {
        String sql = "INSERT INTO treatment (typeoftreatment, duration, price) VALUES (?, ?, ?)";

        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, treatment.getTypeOfTreatment());
            ps.setInt(2, treatment.getDuration());
            ps.setInt(3, treatment.getPrice());
            ps.executeUpdate();

        } catch (SQLException e){
            throw new DataAccessException("Kunne ikke oprette ny behandling", e);
        }
    }

    public void deleteTreatmentSafely(int treatmentId) {
        String sql = "UPDATE treatment SET isactive = false WHERE id = ?";

        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, treatmentId);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new DataAccessException("Kunne ikke fjerne behandlingen", e);
        }
    }

    // Retrieves the columns SQL
    private Treatment mapRow(ResultSet rs) throws SQLException {
        return new Treatment(
            rs.getInt("id"),
                rs.getString("typeofTreatment"),
                rs.getInt("Duration"),
                rs.getInt("price"),
                rs.getBoolean("isactive")
        );
    }

}
