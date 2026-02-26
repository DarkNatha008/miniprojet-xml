package miniprojet_xml.model;

public class Produit {

	private String name;
	private double prix;
	private double quantite;
	
	public Produit(String name, double prix, double quantite){
		this.setName(name);
		this.setPrix(prix);
		this.setQuantite(quantite);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public double getQuantite() {
		return quantite;
	}

	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}
	
	public String generateSQLInsertionRequest(double priceMultiplicator) {
		String request = "insert into produit(nom, prix, quantité) values('";
		request = request+this.name+"', "+this.prix*priceMultiplicator+", "+this.quantite+");";
		return request;
	}
}
