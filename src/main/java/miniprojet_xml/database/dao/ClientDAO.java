package miniprojet_xml.database.dao;


import java.sql.*;
import miniprojet_xml.database.DatabaseConnection;
import miniprojet_xml.model.Client;

public class ClientDAO {
	private Connection conn;

	public ClientDAO() {
		// connexion à la base de données
		conn = DatabaseConnection.getConnection();
	}
	/**
     * Recherche un client dans la base de données à partir de son email.
     * @param email Email du client à rechercher
     * @return L'objet Client correspondant si trouvé, sinon null
     * @throws SQLException Si une erreur SQL survient
     */
    public Client findByEmail(String email) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM client WHERE email=?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return new Client(rs.getInt("id"), rs.getString("nom_client"), rs.getString("email"), rs.getString("ville"));
        }
        return null;
    }
    /**
     * Recherche un client dans la base de données à partir de son id.
     * @param id Id du client à rechercher
     * @return L'objet Client correspondant si trouvé, sinon null
     * @throws SQLException Si une erreur SQL survient
     */
    public Client findById(int id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM client WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return new Client(rs.getInt("id"), rs.getString("nom_client"), rs.getString("email"), rs.getString("ville"));
        }
        return null;
    }
    /**
     * Insère un nouveau client dans la base de données.
     * @param client Objet Client à insérer
     * @return L'identifiant auto-généré du client inséré
     * @throws SQLException Si l'insertion échoue ou si l'id ne peut être récupéré
     */
    public int insert(Client client) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO client(nom_client, email, ville) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, client.getNomClient());
        ps.setString(2, client.getEmail());
        ps.setString(3, client.getVille());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("Impossible de récupérer l'id du nouveau client");
    }
}
