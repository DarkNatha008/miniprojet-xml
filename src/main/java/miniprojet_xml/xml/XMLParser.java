package miniprojet_xml.xml;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import miniprojet_xml.database.dao.ClientDAO;
import miniprojet_xml.database.dao.CommandeDAO;
import miniprojet_xml.database.dao.ProduitDAO;
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
		
		// création du DAO
		
		ProduitDAO produitDAO = new ProduitDAO();
		
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
			
			try {
				// appel de l'insertion DAO pour chaque produit
				for(Element produit : produits) {
					String nom = produit.getChildText("nom");
					double prix = Double.parseDouble(produit.getChildText("prix")) * 2;
					int quantite = Integer.parseInt(produit.getChildText("quantité"));
				
					produitDAO.insert(new Produit(nom, prix, quantite));

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
		
		System.out.println("Insertion de "+path+" dans la base de données");
		
		
		
		// création des DAOs
		
		ClientDAO clientDAO = new ClientDAO();
		ProduitDAO produitDAO = new ProduitDAO();
		CommandeDAO commandeDAO = new CommandeDAO();

		
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
			String nameClient = elementClient.getChildText("nom-client");
			String email = elementClient.getChildText("email");
			String ville = elementClient.getChildText("ville");
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
			LocalDate date = LocalDate.parse(racine.getChildText("date"), formatter);
			
			
			List<Element> produits = racine.getChildren("produits").get(0).getChildren("produit");
			ArrayList<Produit> listProduit = new ArrayList<Produit>();
			for (Element p : produits) {
				listProduit.add(new Produit(p.getChildText("nom"), Double.valueOf(p.getChildText("prix")), Integer.parseInt(p.getChildText("quantité"))));
			}
			
			try {
				Client client = clientDAO.findByEmail(email);
				
				if(client==null)  {
					try {
						client = new Client(nameClient, email, ville);
						client.setId(clientDAO.insert(client));
						System.out.println("Client créé : " + nameClient);
					} catch(SQLException e) {
						e.printStackTrace();
						System.out.println("Erreur dans la création du client : " + nameClient);
						throw e;
					}
				}
				
				for(Produit p : listProduit) {
					if(p.getQuantite()<=0 ) {
						System.out.println("Le produit "+p.getName()+" à une quantité commandée négative.");
						return;
					}
					Produit produitInDatabase=produitDAO.findByName(p.getName());
					if(produitInDatabase==null) {
						System.out.println("Le produit "+p.getName()+" n'existe pas.");
						return;
					}
					if(produitInDatabase.getQuantite()<p.getQuantite()) {
						System.out.println("Le produit "+p.getName()+" à une quantité commandée inférieur au stock.");
						return;
					}
					p.setId(produitInDatabase.getId());
				}
				
				commandeDAO.insert(new Commande(client, date, listProduit));
				
			}
			catch(SQLException e) {
				e.printStackTrace();
				throw e;
			}
		System.out.println("Fichier " + path + " traité.");
		}
		catch(java.io.IOException e) {
			e.printStackTrace();
			
		}
	}
	public void exportCommandeXML(String path) {
		Element root = new Element("commandes");
	}
}
