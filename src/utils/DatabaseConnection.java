package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DatabaseConnection {

	private static final String DATABASE_NAME = "MyCourseIs";
	private static final String URL = "jdbc:mysql://localhost:3306/"+DATABASE_NAME+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "slaptazodis";

	
	public static Connection connect() throws Exception {
		
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(connection == null) {
			throw new Exception();
		}
		
		return connection;
	}
	
	
	
	public static void disconect(Connection connection, Statement statement) {
		try {
			if(connection != null && statement != null) {
				connection.close();
				statement.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}







	
	
}
