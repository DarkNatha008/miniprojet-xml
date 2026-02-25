package miniprojet_xml.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) throws SQLException {
		DatabaseQuery data = new DatabaseQuery();
		System.out.println("Bienvenue sur l'application scanneur de fichier XML");
		System.out.println("");
		ResultSet res= data.loadData("SELECT * FROM produit");
		data.displayAllCols(res);
		
		
	}

}
