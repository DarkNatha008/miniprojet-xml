package miniprojet_xml.miniprojet_xml;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQuery {
	
	private Connection conn = DatabaseConnection.getConnection();
	
	public DatabaseQuery() {
	}
	
	/**
	 * Charge les données de la base de la base de données dans l'application
	 * @param query
	 * @return le résultat de la requête
	 */
	public ResultSet loadData(String query) {
		ResultSet res = null;
		try {
			Statement stmt = conn.createStatement();
			res = stmt.executeQuery(query);
			return res;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * Permet d'obtenir le nombre de colonne retourné par la requête
	 * @param data
	 * @return le nombre colonne dans la table
	 * @throws SQLException
	 */
	
	public int getNbCols(ResultSet data) throws SQLException {
		try {
			ResultSetMetaData rsmd = data.getMetaData();
			int nbCols = rsmd.getColumnCount();
			return nbCols;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * Affiche les données retournées dans l'application
	 * @param data
	 * @throws SQLException
	 */
	public void displayAllCols(ResultSet data) throws SQLException {
	
		try {
			ResultSetMetaData rsmd = data.getMetaData();
			int nbCols = getNbCols(data);
			for(int i=1; i<nbCols; i++) {
				System.out.print("|"+rsmd.getColumnName(i) + "|");
			}
			System.out.println();
			System.out.println("---------------------------");
			System.out.println();
			while(data.next()) {
				
				for(int i=1; i<nbCols; i++) {
					System.out.print("|"+data.getString(i) + "|");
					
				}
				System.out.println();
				System.out.println();
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("ipossible d'afficher les résultats");
		}
	}

}
