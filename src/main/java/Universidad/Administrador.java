package Universidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Administrador extends Persona{
    
    // ATRIBUTOS

    // CONSTRUCTOR VACÍO, CONSTRUCTOR CON PARÁMETROS Y CONSTRUCTOR COPIA

    public Administrador() {
    }

    public Administrador(String ID_Persona, String nombre, int edad, String telefono, String contrasena, int ID_Persona1) {
        super(ID_Persona, nombre, edad, telefono, contrasena);
    }

    public Administrador(Administrador copiaAdministrador){
        super((Persona)copiaAdministrador);
    }
    
    public static void verPersonas(Connection con) {
        try{
            PreparedStatement consulta = con.prepareStatement("select * from persona");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay usuarios.");
            } else {
                do {
                    String dni = resultados.getString("ID_Persona");
                    String rol = resultados.getString("Rol");
                    String contrasena = resultados.getString("Contrasena");
                    System.out.println("DNI " +dni + " ROL: "+rol +" Contrasena: " +contrasena);
                } while(resultados.next());

            }
            if (resultados != null) {resultados.close (); }//cierra
            if (consulta != null) consulta.close ();//cierra

        }catch(SQLException error){
            System.out.println("Error en la consulta.");

        }


    }


}

