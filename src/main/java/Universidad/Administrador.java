package Universidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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

    public static Persona pedirDatosPersona(){
        Scanner lector = new  Scanner(System.in);
        String tipoPersona;
        do{
            System.out.println("¿Qué tipo de persona quieres introducir?");
            System.out.println("1. Administrador");
            System.out.println("2. Profesor");
            System.out.println("3. Alumno");
            System.out.println("4. Bibliotecario");
            tipoPersona = lector.nextLine();
            System.out.println(tipoPersona);
        }while (!"1".equals(tipoPersona) && !"2".equals(tipoPersona)  && !"3".equals(tipoPersona)  && !"4".equals(tipoPersona) );

        String rol ="";
        switch (tipoPersona){
            case "1": rol = "administrador"; break;
            case "2": rol = "profesor"; break;
            case "3": rol = "alumno"; break;
            case "4": rol = "bibliotecario"; break;
        }

        System.out.println("Dame el DNI");
        String DNI = lector.nextLine();
        System.out.println("Dame la contrasena");
        String constrasena = lector.nextLine();
        System.out.println("Dame la nombre");
        String nombre = lector.nextLine();
        System.out.println("Dime el telefono");
        String telefono = lector.nextLine();
        System.out.println("Dime la edad");
        int edad = lector.nextInt();
        lector.nextLine();
        Persona persona = new Persona();
        persona.setID_Persona(DNI);
        persona.setContrasena(constrasena);
        persona.setNombre(nombre);
        persona.setTelefono(telefono);
        persona.setEdad(edad);
        persona.setRol(rol);

        return persona;
    }

    public static void anadirPersona(Connection miConexion){
        Scanner lector = new  Scanner(System.in);
        System.out.println("¿Qué persona quieres introducir?");
        System.out.println();

    }



}

