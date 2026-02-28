package miniprojet_xml.main;

import java.sql.SQLException;
import org.jdom2.JDOMException;

import miniprojet_xml.xml.XMLParser;

public class Main {

	public static void main(String[] args) throws SQLException, JDOMException {
		String projectPath = System.getProperty("user.dir")+"/src/main/resources";
		
		System.out.println("Bienvenue sur l'application scanneur de fichier XML");
		System.out.println("");
		
		XMLParser parser = new XMLParser();
		parser.displayProduct(projectPath + "/xml/Produits.xml");
		parser.insertProductData(projectPath + "/xml/Produits.xml");
		
		parser.insertCommandeData(projectPath + "/xml/Commande.xml", projectPath + "/xml/Commande.dtd");
		
	    String exportPath = projectPath + "/xml/commandes_exportés.xml";

		parser.exportCommandeXML(exportPath);
		
	}

}
