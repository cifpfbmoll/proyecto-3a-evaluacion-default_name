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

public   class Persona {
    private String ID_Persona;
    private String Nombre;
    private int Edad;
    private String Telefono;
    private String Contraseña;

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

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    // constructor completo
    public Persona(String ID_Persona, String nombre, int edad, String telefono, String contraseña) {
        this.ID_Persona = ID_Persona;
        this.Nombre = nombre;
        this.Edad = edad;
        this.Telefono = telefono;
        this.Contraseña = contraseña;
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
        this.setContraseña(p.getContraseña());
    }

    // metodo identificarse ESTA SIN PROBAR LO DE LA BASE DE DATOS
    public static String[]  identificarse(Connection con) throws SQLException {
        Scanner lector = new Scanner(System.in);
        System.out.println("Dame tu dni");
        String dni = lector.nextLine();
        System.out.println("Dame tu contraseña");
        String contraseña = lector.nextLine();

        PreparedStatement consulta = con.prepareStatement("select * from users where ID_Persona = ? and Constraseña = ?");
        consulta.setString(1, dni);
        consulta.setString(2, contraseña);
        ResultSet resultados = consulta.executeQuery();

        String[] datos = new String[2];
        if (resultados.next() == false) {
            System.out.println("ResultSet in empty in Java");
        } else {
            String dniCorrecto = resultados.getString ("ID_Persona");
            datos[0] = dniCorrecto;
            String rol = resultados.getString ("Rol");
            datos[1] = rol;
        }

        return datos;
    }

    //método buscar info personas (este método no se que recibe ni que devuelve, lo dejo asi de momento)
    public static void buscarInfoPersonas() {
        System.out.printf("TODO");
    }

}
