package miniprojet_xml.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jdom2.JDOMException;

import miniprojet_xml.model.Commande;
import miniprojet_xml.model.Produit;
import miniprojet_xml.xml.CommandeXMLReader;
import miniprojet_xml.xml.ProduitsXMLReader;

public class Main {

	public static void main(String[] args) throws SQLException {
		DatabaseQuery data = new DatabaseQuery();
		System.out.println("Bienvenue sur l'application scanneur de fichier XML");
		System.out.println("");
		ResultSet res= data.loadData("SELECT * FROM produit");
		data.displayAllCols(res);
		
		ProduitsXMLReader produitReader = new ProduitsXMLReader();
		ArrayList<Produit> produits = new ArrayList<Produit>();
		try {
			produits = produitReader.readAndMakeProduit(new File("src/main/resources/xml/Produits.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(Produit produit : produits) {
			data.updateQuery(produit.generateSQLInsertionRequest(2.0));
		}
		
		CommandeXMLReader commandeReader = new CommandeXMLReader();
		Commande commande = null;
		try {
			commande = commandeReader.readAndMakeCommande(new File("src/main/resources/xml/Commande.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(commande.getDate().toString());
		
		
	}

}
