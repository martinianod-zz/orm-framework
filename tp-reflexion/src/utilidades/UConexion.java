package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class UConexion {
	
	private static UConexion instanciaConexion;
	
	private static Connection conexion;
	
	private UConexion() {
		
		/* recuperar datos de conexion del archivo properties*/
		
		ResourceBundle rb = ResourceBundle.getBundle("resources.framework");
		
		try {
			
			Class.forName(rb.getString("driver"));
			
			conexion = DriverManager.getConnection(rb.getString("db_path"), rb.getString("user"), rb.getString("pass"));
			
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
		
		
		
	}
	
	public static Connection getInstanciaConexion() {
		
		if(instanciaConexion == null) {
			instanciaConexion = new UConexion();
		}
		
		return conexion;
		
	}

	public Connection getConexion() {
		return conexion;
	}

	

}
