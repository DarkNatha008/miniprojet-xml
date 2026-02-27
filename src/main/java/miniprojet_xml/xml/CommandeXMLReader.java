package miniprojet_xml.xml;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import miniprojet_xml.model.Client;
import miniprojet_xml.model.Commande;
import miniprojet_xml.model.Produit;


public class CommandeXMLReader {
	/**
     * Lit le fichier XML et affiche ses données formatées dans la console.
     * 
     * @param file fichier XML représentant une commande
     * @throws JDOMException en cas d’erreur de parsing XML
     * @throws IOException en cas d’erreur de lecture du fichier
     */
	public void readAndDisplay(File file) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(file);

		Element racine = document.getRootElement();
		System.out.println("Nom client: "+racine.getChildren("client").get(0).getChildText("nom-client"));
		System.out.println("Email client: "+racine.getChildren("client").get(0).getChildText("email"));
		System.out.println("Ville client: "+racine.getChildren("client").get(0).getChildText("ville"));
		
		System.out.println("Date: "+racine.getChildText("date"));
		
		List<Element> produits = racine.getChildren("produit");

		System.out.println("Produits:");
		for (Element p : produits) {
		    System.out.println("Nom: " + p.getChildText("nom"));
		    System.out.println("Prix: " + p.getChildText("prix"));
		    System.out.println("Quantité: " + p.getChildText("quantité"));
		    System.out.println();
		}
	}
	/**
     * Lit le fichier XML et construit un objet métier Commande contenant un Client, une date et une liste de Produits.
     * 
     * @param file fichier XML de commande
     * @return objet Commande construit à partir des données XML
     * @throws JDOMException en cas d’erreur XML
     * @throws IOException en cas d’erreur de lecture
     */
	public Commande readAndMakeCommande(File file) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(file);

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
		
		Commande commande = new Commande(client, date, listProduit);
		
		return commande;
	}
}
