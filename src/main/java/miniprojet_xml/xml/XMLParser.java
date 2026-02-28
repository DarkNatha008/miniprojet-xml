package miniprojet_xml.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import miniprojet_xml.database.dao.ClientDAO;
import miniprojet_xml.database.dao.CommandeDAO;
import miniprojet_xml.database.dao.ProduitDAO;
import miniprojet_xml.model.Client;
import miniprojet_xml.model.Commande;
import miniprojet_xml.model.Produit;
import java.io.StringReader;
import java.net.URL;


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
	 * @param pathXML
	 * @throws JDOMException
	 * @throws SQLException
	 */
	
	public void insertCommandeData(String pathXML, String pathDTD) throws JDOMException, SQLException {
		
		System.out.println("Insertion de "+pathXML+" dans la base de données");
		
		// création des DAOs
		
		ClientDAO clientDAO = new ClientDAO();
		ProduitDAO produitDAO = new ProduitDAO();
		CommandeDAO commandeDAO = new CommandeDAO();

		// Chargement du fichier xml
		
		try {
			InputStream isXML = getClass().getResourceAsStream(pathXML);
		    if (isXML == null) {
		        System.out.println("Fichier " + pathXML + " introuvable !");
		        return;
		    }

		    String xmlContent = new String(isXML.readAllBytes());

		    URL dtdURL = getClass().getResource(pathDTD);
		    
		    if (dtdURL == null) {
		        System.out.println("Fichier " + pathDTD + " introuvable !");
		        return;
		    }
		    
		    // injecte DOCTYPE avant le parsing
		    String xmlWithDTD = "<!DOCTYPE commande SYSTEM \"" + dtdURL + "\">\n" + xmlContent;
		    
		    // vérifie la validité du .dtd
		    SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
		    Document document = builder.build(new StringReader(xmlWithDTD));

		    System.out.println("XML validé avec succès avec le DTD : " + pathDTD);

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
			 
		System.out.println("Fichier " + pathXML + " traité.");
		}
		catch(java.io.IOException e) {
			e.printStackTrace();
		}
		catch (JDOMParseException e) {
		    System.out.println("Erreur de validation DTD :" + e.getMessage());
		    e.printStackTrace();
		}
	}
	
	/**
	 * Exporte toutes les commandes présentes dans la base de données vers un fichier XML respectant la structure demandée.
	 *
	 * @param path Chemin absolu du fichier XML à générer
	 * @throws SQLException Si une erreur survient lors de l'accès aux données
	 */
	public void exportCommandeXML(String path) throws SQLException {
		// création des DAOs
		CommandeDAO commandeDAO = new CommandeDAO();
		
		Element commandes = new Element("commandes");
		
		for(Commande c : commandeDAO.allCommandes()) {
			Element commande = new Element("commande");
			commande.setAttribute("id", c.getId());
			commande.setAttribute("nb-produit", ""+c.getListProduit().size());
			
			Element nomClient = new Element("nom-client");
			nomClient.setText(c.getClient().getNomClient());
			commande.addContent(nomClient);
			
			Element emailClient = new Element("email");
			emailClient.setText(c.getClient().getEmail());
			commande.addContent(emailClient);
			
			Element villeClient = new Element("ville");
			villeClient.setText(c.getClient().getVille());
			commande.addContent(villeClient);
			
			Element date = new Element("date");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
			date.setText(c.getDate().format(formatter));
			commande.addContent(date);
			
			double total = 0;
			for(Produit p : c.getListProduit()) {
				total += (p.getPrix()*p.getQuantite());
			}
			Element totalElement = new Element("total");
			totalElement.setText(""+total);
			commande.addContent(totalElement);
			
			Element produits = new Element("produits");
			for(Produit p : c.getListProduit()) {
				Element produit = new Element("produit");
				
				Element nomProduit = new Element("nom");
				nomProduit.setText(p.getName());
				produit.addContent(nomProduit);
				
				Element prix = new Element("prix");
				prix.setText(""+p.getPrix());
				produit.addContent(prix);
				
				Element quantité = new Element("quantité");
				quantité.setText(""+p.getQuantite());
				produit.addContent(quantité);
				
				produits.addContent(produit);
			}
			commande.addContent(produits);
			
			commandes.addContent(commande);
		}
		
		Document document = new Document(commandes);
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try {
		    xmlOutput.output(document, new FileOutputStream(path));
		    System.out.println("Fichier exporté avec succès vers : " + path);
		} catch (IOException e) {
		    System.out.println("Échec de l'exportation du fichier xml vers : " + path);
		    e.printStackTrace();
		}
	}
}
