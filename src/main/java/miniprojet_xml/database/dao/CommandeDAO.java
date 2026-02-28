package miniprojet_xml.database.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import miniprojet_xml.database.DatabaseConnection;
import miniprojet_xml.model.Client;
import miniprojet_xml.model.Commande;
import miniprojet_xml.model.Produit;

public class CommandeDAO {
	private Connection conn;

	public CommandeDAO() {
		// connexion à la base de données
		conn = DatabaseConnection.getConnection();
	}
	/**
     * Insère une commande dans la base de données.
     * @param commande Objet Commande contenant le client, la date et la liste des produits
     * @return L'identifiant de la commande généré (exemple: "C10")
     * @throws SQLException Si une erreur SQL survient
     */
	public String insert(Commande commande) throws SQLException {
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
	/**
     * Renvoie la liste des commandes enregistrées de la base.
     * @return La liste des commandes enregistrées dans la base
     * @throws SQLException Si une erreur SQL survient
     */
	public ArrayList<Commande> allCommandes() throws SQLException{
		
		// déclaration des DAOs
		ClientDAO clientDAO = new ClientDAO();
		LigneCommandeDAO ligneCommandeDAO = new LigneCommandeDAO();
		
		ArrayList<Commande> listCommandes = new ArrayList<Commande>();
		PreparedStatement psCommande = conn.prepareStatement("SELECT * FROM commande");
		ResultSet rsCommande = psCommande.executeQuery();
		while(rsCommande.next()) {
			Client client = clientDAO.findById(rsCommande.getInt("idClient"));
			LocalDate commandeDate = rsCommande.getDate("date").toLocalDate();
			ArrayList<Produit> listProduit = ligneCommandeDAO.findByCommandeId(rsCommande.getString("id"));
			Commande commande = new Commande(rsCommande.getString("id"), client, commandeDate, listProduit);
			listCommandes.add(commande);
		}
		return listCommandes;
		
	}
}
