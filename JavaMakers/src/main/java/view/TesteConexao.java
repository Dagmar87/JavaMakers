package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteConexao {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection conexao = null;
		
		try {
			Class.forName("oracle.jdbc.OracleDriver");

			conexao = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "USUARIO", "dagmar87");
			
			System.out.println("Conexao: " + conexao.toString());

		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	

}
