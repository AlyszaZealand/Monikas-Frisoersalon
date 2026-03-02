package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
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

    // Create treatment SQL
    public int createTreatment(String treatment, int duration) throws SQLException{
        String sql = "insert into treatments (treatment, duration) values (?,?)";

        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, treatment);
            ps.setInt(2, duration);

            int rows = ps.executeUpdate();
            return rows;
        }
    }

    //choose treatment??maybe all treatment til menu'en??
    public List<Treatment> findAllTreatment(){
        String sql = "select treatment from treatment";

        List<Treatment> result = new ArrayList<>();
        try(Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                result.add(mapRow(rs));
            }

        }catch(SQLException e){}
        return result;
    }

    // Delete treatment SQL
    public void deleteTreatment(int id) {
        String sql = "Delete from treatment where treatment_id=?";

        try(Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e){
            //custom exception???
        }

    }

    // Show all treatments SQL
    public void displayAllTreatments(int treatmentID) {
        String sql = "";
    }

    // Retrieves the columns SQL
    private Treatment mapRow(ResultSet rs) throws SQLException {

        return new Treatment(
            rs.getInt("id"),
                rs.getString("Treatment"),
                rs.getInt("Duration"),
                rs.getInt("price"),
                rs.getBoolean("isactive")
        );
    }

}
