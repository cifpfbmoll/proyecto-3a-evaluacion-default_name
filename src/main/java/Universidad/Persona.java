package Universidad;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
/**
 * Clase abstracta persona que permite la identificacion del usuario en la aplicacion
 * @author grupo3
 */
public  class Persona {
    //Atributos
    private String ID_Persona;
    private String Nombre;
    private int Edad;
    private String Telefono;
    private String Contrasena;
    private String Rol;
    private static Scanner lector = new Scanner(System.in);
    // getters y setters
    public String getID_Persona() {
        return ID_Persona;
    }

    public void setID_Persona(String ID_Persona) {
        this.ID_Persona = ID_Persona;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getEdad() {
        return Edad;
    }

    public void setEdad(int edad) {
        Edad = edad;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        Contrasena = contrasena;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String rol) {
        this.Rol = rol;
    }

    public Persona(String ID_Persona, String nombre, int edad, String telefono, String contrasena, String rol) {
        this.ID_Persona = ID_Persona;
        this.Nombre = nombre;
        this.Edad = edad;
        this.Telefono = telefono;
        this.Contrasena = contrasena;
        this.Rol = rol;
    }

    // constructor vacio
    public Persona() {
    }

    // constructor copia
    public Persona(Persona p) {
        this.setID_Persona(p.getID_Persona());
        this.setNombre(p.getNombre());
        this.setEdad(p.getEdad());
        this.setTelefono(p.getTelefono());
        this.setContrasena(p.getContrasena());
        this.setRol(p.getRol());
    }

    /**
     * Metiodo para identificar al usuario, este devolvera el el dni posicion 0 y el rol posicion 1
     * @param con
     * @return String[]
     */
    public static String[]  identificarse(Connection con)  {

        System.out.println("Dame tu dni");
        String dni = lector.nextLine();
        System.out.println("Dame tu contrasena");
        String contrasena = lector.nextLine();
        String[] datos = new String[2];
        try {
            PreparedStatement consulta = con.prepareStatement("select * from persona where ID_Persona = ? and Contrasena = ?");
            consulta.setString(1, dni);
            consulta.setString(2, contrasena);
            ResultSet resultados = consulta.executeQuery();


            if (resultados.next() == false) {
                System.out.println("No se ha encontrado al usuario.");
            } else {
                String dniCorrecto = resultados.getString("ID_Persona");
                datos[0] = dniCorrecto;
                String rol = resultados.getString("Rol");
                datos[1] = rol;
            }


        } catch (SQLException exception) {
            System.out.println("ERROR EN LA CONNEXION EN LA BBDD");
            exception.printStackTrace();
        }
        return datos;
    }

}
