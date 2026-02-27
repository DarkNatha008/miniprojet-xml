package miniprojet_xml.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import miniprojet_xml.database.DatabaseConnection;
import miniprojet_xml.model.Produit;

public class LigneCommandeDAO {
	private Connection conn;
	
	public LigneCommandeDAO() {
		// connexion à la base de données
		conn = DatabaseConnection.getConnection();
	}
	/**
     * Insère une ligne de commande dans la table "lignes_commande"
     * et met à jour la quantité disponible du produit.
     * @param produit Produit commandé
     * @param newCommandeId Id de la commande (ex: "C10")
     * @return true si l'insertion et la mise à jour sont réussies
     * @throws SQLException Si une erreur SQL survient
     */
	public boolean insert(Produit produit, String newCommandeId) throws SQLException {
        PreparedStatement ps;
        try {
			ps = conn.prepareStatement("INSERT INTO lignes_commande(idCommande, idProduit, prixAchat, quantité) VALUES(?,?,?,?)");
			ps.setString(1, newCommandeId);
			ps.setInt(2, produit.getId());
			ps.setDouble(3, produit.getPrix());
			ps.setInt(4, produit.getQuantite());
			ps.executeUpdate();
			System.out.println("Ligne de commande insérée pour : " + produit.getName());
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur pour l'insertion ligne commande pour : " + produit.getName());
			throw e;
		}
		try {
			ps = conn.prepareStatement("UPDATE produit SET quantité=quantité-? WHERE id=?");
			ps.setInt(1, produit.getQuantite());
			ps.setInt(2, produit.getId());
			ps.executeUpdate();
			System.out.println("Quantité mise à jour pour : " + produit.getName());

		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur pour la mise à jour de la quantité du produit : " + produit.getName());
			throw e;
		}
		return true;
    }
}
