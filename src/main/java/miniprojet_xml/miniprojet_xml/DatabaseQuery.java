package miniprojet_xml.miniprojet_xml;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQuery {
	
	private Connection conn;
	
	public DatabaseQuery() {
		Connection conn = DatabaseConnection.getConnection();
	}
	
	public ResultSet loadData() {
		ResultSet res = null;
		try {
			Statement stmt = conn.createStatement();
			res = stmt.executeQuery("SELECT * FROM client");
		}
		catch(SQLException e) {
			System.out.println("erreur connexion");
		}
		return res;
	}
	
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

}
