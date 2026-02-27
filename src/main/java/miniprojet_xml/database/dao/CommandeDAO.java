package miniprojet_xml.database.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import miniprojet_xml.database.DatabaseConnection;
import miniprojet_xml.model.Client;
import miniprojet_xml.model.Commande;
import miniprojet_xml.model.Produit;

public class CommandeDAO {
	public CommandeDAO() {
		
	}
	/**
     * Insère une commande dans la base de données.
     * @param commande Objet Commande contenant le client, la date et la liste des produits
     * @return L'identifiant de la commande généré (exemple: "C10")
     * @throws SQLException Si une erreur SQL survient
     */
	public String insert(Commande commande) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT MAX(CAST(SUBSTRING(id,2) AS UNSIGNED)) AS max_id FROM commande");
		ResultSet rs = ps.executeQuery();

		int nextId = 1;

		if (rs.next()) {
		    nextId = rs.getInt("max_id")+1;
		}
		
		String newCommandeId = "C"+nextId;
		try {
			ps = conn.prepareStatement("INSERT INTO commande(id, idClient, date) VALUES(?,?,?)");
			ps.setString(1, newCommandeId);
			ps.setInt(2, commande.getClient().getId());
			ps.setDate(3, Date.valueOf(commande.getDate()));
			ps.executeUpdate();
			System.out.println("Commande créé : " + newCommandeId);
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur dans la création de la commande : " + newCommandeId);
			throw e;
		}
		
		LigneCommandeDAO ligneCommandeDAO = new LigneCommandeDAO();
		
		for(Produit p : commande.getListProduit()) {
			ligneCommandeDAO.insert(p, newCommandeId);
		}
		
		return newCommandeId;
		
	}
}
