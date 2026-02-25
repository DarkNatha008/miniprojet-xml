package miniprojet_xml.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	private static final String driver = "jdbc:mysql://localhost:3306/mini-projet_db";
	private static final String user = "root";
	private static final String password = "";
	
	/**
	 * Permet de connecter l'application à la base de données
	 * @return la connexion à la base de données
	 */
	
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(driver, user, password);
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
