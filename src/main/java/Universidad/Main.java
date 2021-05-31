package Universidad;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    //Scanner
    public static Scanner lector = new Scanner(System.in);
    //Main
    public static void main(String[] args){

        Connection miConexion = obtenerConexion();
        Persona persona = new Persona();
        String[] datos = Persona.identificarse(miConexion);
        for (int i = 0; i < datos.length; i++) {
            System.out.println(datos[i]);
        }

        Bibliotecario.filtrarLibrosTematica(miConexion);
    }


    /**
     * Metodo para obtener la conexion
     * @return Connection
     */
    public static Connection obtenerConexion () {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://51.178.152.223:3306/nueva_gestion_universidad";
            connection = DriverManager.getConnection (url, "Dam4", "ProyectoGrupo4");
        } catch (SQLException exception) {
            System.out.println("ERROR AL OBTENER LA CONEXION");
            exception.printStackTrace();
        }
        return connection;
    }
}

