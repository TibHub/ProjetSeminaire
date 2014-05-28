package outilsBD;

import java.sql.*;

public class BDConnexion {
	private static String driver ;
	private static String url, login, passwd ;
	private static Connection connection ;
	
	static { 
		try {
			//url = "jdbc:oracle:thin:@//127.0.0.1:1521/xe";
			url = "jdbc:oracle:thin:@vs-oracle2:1521:ORCL";
			login = "GR1U3";
			passwd = "GR1U3";
			Class.forName("oracle.jdbc.OracleDriver");
			connection = DriverManager.getConnection(url, login, passwd);
		}
		catch (Exception e) {
			// problème, on arrête le serveur
			System.out.println(e);
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
}
