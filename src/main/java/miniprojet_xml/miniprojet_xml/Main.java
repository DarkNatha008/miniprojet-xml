package miniprojet_xml.miniprojet_xml;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jdom2.Document;
import org.jdom2.JDOMException;

public class Main {

	public static void main(String[] args) throws SQLException, JDOMException {
		DatabaseQuery data = new DatabaseQuery();
		System.out.println("Bienvenue sur l'application scanneur de fichier XML");
		System.out.println("");
		
		XMLParser parser = new XMLParser();
		parser.display("/xml/Produits.xml");
		
		
	}

}
