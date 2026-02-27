package miniprojet_xml.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import miniprojet_xml.database.DatabaseConnection;
import miniprojet_xml.model.Produit;

public class ProduitDAO {
	public ProduitDAO() {
		
	}
	public Produit findByName(String name) throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM produit WHERE nom=?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return new Produit(rs.getInt("id"), rs.getString("nom"), rs.getDouble("prix"), rs.getInt("quantité"));
        }
		return null;
	}
	public int insert(Produit produit) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO produit(nom, prix, quantité) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, produit.getName());
        ps.setDouble(2, produit.getPrix());
        ps.setInt(3, produit.getQuantite());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("Impossible de récupérer l'id du nouveau produit");
    }
}
