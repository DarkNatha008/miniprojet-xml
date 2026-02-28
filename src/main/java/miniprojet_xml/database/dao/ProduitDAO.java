package miniprojet_xml.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import miniprojet_xml.database.DatabaseConnection;
import miniprojet_xml.model.Produit;

public class ProduitDAO {
	private Connection conn;

	public ProduitDAO() {
		// connexion à la base de données
		conn = DatabaseConnection.getConnection();
	}
	/**
	 * Recherche un produit dans la base de données à partir de son nom.
	 * @param name Nom du produit
	 * @return L'objet Produit correspondant si trouvé, sinon null
	 * @throws SQLException Si une erreur SQL survient
	 */
	public Produit findByName(String name) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM produit WHERE nom=?");
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			return new Produit(rs.getInt("id"), rs.getString("nom"), rs.getDouble("prix"), rs.getInt("quantité"));
		}
		return null;
	}
	/**
	 * Insère un nouveau produit dans la base de données s'il n'existe pas ou augmente sa quantité s'il existe.
	 * @param produit Objet Produit à insérer
	 * @return L'identifiant du produit inséré ou mis à jour.
	 * @throws SQLException Si l'insertion échoue ou si l'id ne peut être récupéré
	 */
	public int insert(Produit produit) throws SQLException {
		Produit produitInDatabase=this.findByName(produit.getName());
		if(produitInDatabase!=null) {
			PreparedStatement ps = conn.prepareStatement("UPDATE produit SET quantité=quantité+? WHERE nom=?");
			ps.setInt(1, produit.getQuantite());
			ps.setString(2, produit.getName());
			ps.executeUpdate();
			return produitInDatabase.getId();
		} else {
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
}
