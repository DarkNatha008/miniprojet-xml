package miniprojet_xml.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jdom2.Document;
import org.jdom2.JDOMException;

import miniprojet_xml.database.DatabaseQuery;
import miniprojet_xml.xml.XMLParser;

public class Main {

	public static void main(String[] args) throws SQLException, JDOMException {
		DatabaseQuery data = new DatabaseQuery();
		System.out.println("Bienvenue sur l'application scanneur de fichier XML");
		System.out.println("");
		
		XMLParser parser = new XMLParser();
		parser.displayProduct("/xml/Produits.xml");
		parser.insertProductData("/xml/Produits.xml");
		
		parser.insertCommandeData("/xml/Commande.xml", "/xml/Commande.dtd");
		
		String projectPath = System.getProperty("user.dir");

	    String exportPath = projectPath + "/src/main/resources/xml/commandes_exportés.xml";

		
		parser.exportCommandeXML(exportPath);
		
	}

}
