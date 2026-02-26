package miniprojet_xml.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jdom2.JDOMException;

import miniprojet_xml.model.Produit;
import miniprojet_xml.xml.ProduitsXMLReader;

public class Main {

	public static void main(String[] args) throws SQLException {
		DatabaseQuery data = new DatabaseQuery();
		System.out.println("Bienvenue sur l'application scanneur de fichier XML");
		System.out.println("");
		ResultSet res= data.loadData("SELECT * FROM produit");
		data.displayAllCols(res);
		
		ProduitsXMLReader reader = new ProduitsXMLReader();
		ArrayList<Produit> produits = new ArrayList<Produit>();
		try {
			produits = reader.readAndMakeProduit(new File("src/main/resources/xml/Produits.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(Produit produit : produits) {
			data.updateQuery(produit.generateSQLInsertionRequest(2.0));
		}
		
		
	}

}
