package miniprojet_xml.model;

public class Client {
	private String nomClient;
	private String email;
	private String ville;
	
	public Client(String nomClient, String email, String ville) {
		this.setNomClient(nomClient);
		this.setEmail(email);
		this.setVille(ville);
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}
}
