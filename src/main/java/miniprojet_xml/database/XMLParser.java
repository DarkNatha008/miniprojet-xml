package miniprojet_xml.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

public class XMLParser {
	
	/**
	 * Affiche le contenu du fichier Produits.xml sous format xml
	 * @param path
	 * @throws JDOMException
	 */
	
	public void display(String path) throws JDOMException {
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
	
	public void insertData(String path) throws JDOMException, SQLException {
		
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
}
