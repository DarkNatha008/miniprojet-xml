package miniprojet_xml.miniprojet_xml;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		System.out.println("Bienvenue sur l'application scanneur de fichier XML");
		System.out.println("");
		System.out.println("---------------------------------------------------");
		try {
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery("SELECT * FROM client");
			while(res.next()) {
				System.out.println(res.getString(2));
			}
		}
		catch(SQLException e) {
			System.out.println("erreur connexion");
		}
		
	}

}
