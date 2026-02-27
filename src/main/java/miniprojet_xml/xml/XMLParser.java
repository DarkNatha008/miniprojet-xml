package miniprojet_xml.xml;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import miniprojet_xml.database.DatabaseConnection;
import miniprojet_xml.model.Client;
import miniprojet_xml.model.Commande;
import miniprojet_xml.model.Produit;

public class XMLParser {
	
	/**
	 * Affiche le contenu du fichier Produits.xml sous format xml
	 * @param path
	 * @throws JDOMException
	 */
	
	public void displayProduct(String path) throws JDOMException {
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream is = getClass().getResourceAsStream(path);
			if(is == null) {
				System.out.println("Fichier introuvable !");
				return;
			}
			Document document = builder.build(is);
			XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
			output.output(document, System.out);
		}
		catch(java.io.IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Insert les données du fichier produits.xml dans la base de données
	 * @param path
	 * @throws JDOMException
	 * @throws SQLException
	 */
	
	public void insertProductData(String path) throws JDOMException, SQLException {
		
		// connexion à la base de données
		
		Connection conn = DatabaseConnection.getConnection();
		
		// Chargement du fichier xml
		
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream is = getClass().getResourceAsStream(path);
			if(is == null) {
				System.out.println("Fichier introuvable !");
				return;
			}
			Document document = builder.build(is);
			Element root = document.getRootElement();
			List<Element> produits = root.getChildren();
			
			//preparation de la requête
			
			try {
				PreparedStatement ps = conn.prepareStatement("INSERT INTO produit (nom, prix, quantité) VALUES (?,?,?)");
				for(Element produit : produits) {
					String nom = produit.getChildText("nom");
					double prix = Double.parseDouble(produit.getChildText("prix")) * 2;
					int quantite = Integer.parseInt(produit.getChildText("quantité"));
				
					ps.setString(1, nom);
					ps.setDouble(2, prix);
					ps.setInt(3, quantite);
					ps.executeUpdate();
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
	}
		catch(java.io.IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Insert les données du fichier commande.xml dans la base de données
	 * @param path
	 * @throws JDOMException
	 * @throws SQLException
	 */
	
	public void insertCommandeData(String path) throws JDOMException, SQLException {
		
		// connexion à la base de données
		
		Connection conn = DatabaseConnection.getConnection();
		
		// Chargement du fichier xml
		
		try {
			InputStream is = getClass().getResourceAsStream(path);
			if(is == null) {
				System.out.println("Fichier introuvable !");
				return;
			}
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(is);

			Element racine = document.getRootElement();
			
			Element elementClient = racine.getChildren("client").get(0);
			String nomClient = elementClient.getChildText("nom-client");
			String email = elementClient.getChildText("email");
			String ville = elementClient.getChildText("ville");
			Client client = new Client(nomClient, email, ville);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
			LocalDate date = LocalDate.parse(racine.getChildText("date"), formatter);
			
			
			List<Element> produits = racine.getChildren("produit");
			ArrayList<Produit> listProduit = new ArrayList<Produit>();
			for (Element p : produits) {
				listProduit.add(new Produit(p.getChildText("nom"), Double.valueOf(p.getChildText("prix")), Integer.parseInt(p.getChildText("quantité"))));
			}
			
			
			try {
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM client WHERE email=?");
				
				
				ps.setString(1, email);
				ResultSet rs = ps.executeQuery();
				
				int idClient = -1;
				
				if(rs.next()) {
					idClient = rs.getInt("id");
				} else {
					try {
						ps = conn.prepareStatement("INSERT INTO client(nom_client, email, ville) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, client.getNomClient());
						ps.setString(2, email);
						ps.setString(3, client.getVille());
						ps.executeUpdate();
						
						ResultSet generatedKeys = ps.getGeneratedKeys();
						if (generatedKeys.next()) {
							idClient = generatedKeys.getInt(1);
					    }
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
				
				HashMap<String, Integer> productNameIdConversion = new HashMap<String, Integer>();
				
				for(Produit p : listProduit) {
					if(p.getQuantite()<=0 ) {
						System.out.println("Le produit "+p.getName()+" à une quantité commandée négative.");
						return;
					}
					ps = conn.prepareStatement("SELECT id, quantité FROM produit WHERE nom=?");
					ps.setString(1, p.getName());
					rs = ps.executeQuery();
					if(!rs.next()) {
						System.out.println("Le produit "+p.getName()+" n'existe pas.");
						return;
					}
					if(rs.getInt(2)<p.getQuantite()) {
						System.out.println("Le produit "+p.getName()+" à une quantité commandée inférieur au stock.");
						return;
					}
					productNameIdConversion.put(p.getName(), rs.getInt(1));
				}
				
				
				ps = conn.prepareStatement("SELECT id FROM commande ORDER BY id DESC LIMIT 1");
				rs = ps.executeQuery();

				int nextId = 1;

				if (rs.next()) {
				    String lastId = rs.getString("id");
				    nextId = Integer.parseInt(lastId.substring(1)) + 1;
				}
				String newCommandeId = "C"+nextId;
				ps = conn.prepareStatement("INSERT INTO commande(id, idClient, date) VALUES(?,?,?)");
				ps.setString(1, newCommandeId);
				ps.setInt(2, idClient);
				ps.setDate(3, Date.valueOf(date));
				ps.executeUpdate();
				
				for(Produit p : listProduit) {
					ps = conn.prepareStatement("INSERT INTO ligne_commande(idCommande, idProduit, prixAchat, quantité) VALUES(?,?,?,?)");
					ps.setString(1, newCommandeId);
					ps.setInt(2, productNameIdConversion.get(p.getName()));
					ps.setDouble(3, p.getPrix());
					ps.setInt(4, p.getQuantite());
					ps.executeUpdate();
					
					ps = conn.prepareStatement("UPDATE produit SET quantité=quantité-? WHERE id=?");
					ps.setInt(1, p.getQuantite());
					ps.setInt(2, productNameIdConversion.get(p.getName()));
					ps.executeUpdate();
				}
				
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
	}
		catch(java.io.IOException e) {
			e.printStackTrace();
		}
	}
}
