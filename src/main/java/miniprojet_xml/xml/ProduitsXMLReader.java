package miniprojet_xml.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import miniprojet_xml.model.Produit;


public class ProduitsXMLReader {
	/**
     * Lit le fichier XML et affiche les informations des produits dans la console.
     *
     * @param file fichier produits.xml
     * @throws JDOMException en cas d'erreur de parsing XML
     * @throws IOException en cas d'erreur de lecture du fichier
     */
	public void readAndDisplay(File file) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(file);

		Element racine = document.getRootElement();
		List<Element> produits = racine.getChildren("produit");

		for (Element p : produits) {
		    System.out.println("Nom: " + p.getChildText("nom"));
		    System.out.println("Prix: " + p.getChildText("prix"));
		    System.out.println("Quantité: " + p.getChildText("quantité"));
		}
	}
	/**
     * Lit le fichier XML et construit une liste d’objets Produit.
     *
     * @param file fichier produits.xml
     * @return liste d’objets Produit construits à partir du XML
     * @throws JDOMException en cas d’erreur XML
     * @throws IOException en cas d’erreur de lecture
     */
	public ArrayList<Produit> readAndMakeProduit(File file) throws JDOMException, IOException{
		ArrayList<Produit> listProduit = new ArrayList<Produit>();
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(file);

		Element racine = document.getRootElement();
		List<Element> produits = racine.getChildren("produit");

		for (Element p : produits) {
			listProduit.add(new Produit(p.getChildText("nom"), Double.valueOf(p.getChildText("prix")), Integer.parseInt(p.getChildText("quantité"))));
		}
		return listProduit;
	}
}
