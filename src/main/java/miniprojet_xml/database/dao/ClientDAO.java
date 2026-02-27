package miniprojet_xml.database.dao;


import java.sql.*;
import miniprojet_xml.database.DatabaseConnection;
import miniprojet_xml.model.Client;

public class ClientDAO {
	public ClientDAO() {
		
	}
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
