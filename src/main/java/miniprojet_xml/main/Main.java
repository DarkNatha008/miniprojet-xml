package miniprojet_xml.main;

import java.sql.SQLException;
import java.util.Scanner;

import org.jdom2.JDOMException;

import miniprojet_xml.xml.XMLParser;

public class Main {

	public static void main(String[] args) throws SQLException, JDOMException {
		String projectPath = System.getProperty("user.dir")+"/src/main/resources";
		
		System.out.println("Bienvenue sur l'application scanneur de fichier XML");
		System.out.println("");
		
		XMLParser parser = new XMLParser();
		Scanner scanner = new Scanner(System.in);

		
		System.out.println("Bienvenue sur l'application scanneur de fichier XML\n1 - Lancer le script de démo\n2 - Menu interactif pour XMLParser");
		System.out.print("Votre choix : ");
		String choice = scanner.nextLine();
		
		String exportPath;
		
		switch (choice) {
			case "1":
				parser.displayProduct(projectPath + "/xml/Produits.xml");
				parser.insertProductData(projectPath + "/xml/Produits.xml");
				
				parser.insertCommandeData(projectPath + "/xml/Commande.xml", projectPath + "/xml/Commande.dtd");
				
				exportPath = projectPath + "/xml/commandes_exportés.xml";

				parser.exportCommandeXML(exportPath);
				break;
			case "2":
				boolean active = true;
				while (active) {
					System.out.println("--- Menu XMLParser ---\n1 - displayProduct\n2 - insertProductData\n3 - insertCommandeData\n4 - exportCommandeXML\n5 - Quitter");
					System.out.print("Votre choix : ");
					String choice2 = scanner.nextLine();

					switch (choice2) {
						case "1":
							System.out.print("Chemin absolu du fichier Produits.xml : ");
							String displayPath = scanner.nextLine();
							parser.displayProduct(displayPath);
							break;
						case "2":
							System.out.print("Chemin absolu du fichier Produits.xml : ");
							String insertProdPath = scanner.nextLine();
							parser.insertProductData(insertProdPath);
							break;
						case "3":
							System.out.print("Chemin absolu du fichier Commande.xml : ");
							String xmlPath = scanner.nextLine();
							System.out.print("Chemin absolu du fichier Commande.dtd : ");
							String dtdPath = scanner.nextLine();
							parser.insertCommandeData(xmlPath, dtdPath);
							break;
						case "4":
							System.out.print("Chemin absolu du fichier XML à générer : ");
							exportPath = scanner.nextLine();
							parser.exportCommandeXML(exportPath);
							break;
						case "5":
							System.out.println("Au revoir !");
							active=false;
							break;
						default:
							System.out.println("Choix invalide !");
					}
				}
				break;
			default:
				System.out.println("Choix invalide !");
		}
		scanner.close();
	}

}
