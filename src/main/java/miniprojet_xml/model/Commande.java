package miniprojet_xml.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Commande {

	private String id;
	private Client client;
	private ArrayList<Produit> listProduit;
	private LocalDate date;
	
	public Commande(String id, Client client, LocalDate date, ArrayList<Produit> listProduit){
		this.setId(id);
		this.setClient(client);
		this.setListProduit(listProduit);
		this.setDate(date);
	}
	
	public Commande(Client client, LocalDate date, ArrayList<Produit> listProduit){
		this.setClient(client);
		this.setListProduit(listProduit);
		this.setDate(date);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ArrayList<Produit> getListProduit() {
		return listProduit;
	}

	public void setListProduit(ArrayList<Produit> listProduit) {
		this.listProduit = listProduit;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
