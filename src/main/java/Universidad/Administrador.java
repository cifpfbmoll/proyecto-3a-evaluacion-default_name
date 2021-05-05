package Universidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase Administrador
 * @author grupo3
 */
public class Administrador extends Persona{

    // CONSTRUCTOR VACÍO, CONSTRUCTOR CON PARÁMETROS Y CONSTRUCTOR COPIA

    public Administrador() {
    }

    public Administrador(String ID_Persona, String nombre, int edad, String telefono, String contrasena, int ID_Persona1) {
        super(ID_Persona, nombre, edad, telefono, contrasena);
    }

    public Administrador(Administrador copiaAdministrador){
        super((Persona)copiaAdministrador);
    }

    /**
     * Imprime las personas de la tabla persona.
     * @param con es un objeto de tipo Connection, que es la conexión a la BBDD
     */
    public static void verPersonas(Connection con) {
        try{
            PreparedStatement consulta = con.prepareStatement("select * from persona");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay usuarios.");
            } else {
                do {
                    String dni = resultados.getString("ID_Persona");
                    String nombre = resultados.getString("Nombre");
                    int edad = resultados.getInt("Edad");
                    String telefono = resultados.getString("Telefono");
                    String rol = resultados.getString("Rol");
                    String contrasena = resultados.getString("Contrasena");
                    System.out.println("DNI " +dni + " ROL: "+rol +" Contrasena: "
                            +contrasena + "Nombre: " + nombre + "Telefono: " + telefono + "Edad: " + edad);
                } while(resultados.next());
            }
            if (resultados != null) {resultados.close (); }//cierra
            if (consulta != null) consulta.close ();//cierra

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }
    }

    /**
     * Pide el rol y el resto de datos que le quieres dar a la nueva persona
     * @return devuelve una personas con los datos introducidos por el administrador
     */
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

    /**
     * Añade una persona a la BBDD
     * @param miConexion recibe un objeto conexión para conectar con la BBDD
     */
    public static void anadirPersona(Connection miConexion){
        Persona p = Administrador.pedirDatosPersona();

        String datosPersona = "insert into persona values( ?, ?,?,?,?,?) ";
        try {
            PreparedStatement estatementpreparada = miConexion.prepareStatement (datosPersona);
            estatementpreparada.setString(1, p.getID_Persona());
            estatementpreparada.setString(2, p.getNombre());
            estatementpreparada.setInt(3, p.getEdad());
            estatementpreparada.setString(4, p.getTelefono());
            estatementpreparada.setString(5, p.getContrasena());
            estatementpreparada.setString(6, p.getRol());
            int filasMetidas = estatementpreparada.executeUpdate();

            if(filasMetidas>0){
                System.out.println("Se Ha añadido el registro");
            }
            if (estatementpreparada != null) {estatementpreparada.close (); }//cierra

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Busca por un DNI
     * @param dni un dni para buscar
     * @param con un obbjeto Connection para hacer la búsqueda en la BBDD
     * @return true si ha encontrado el DNI, false si no lo ha encontrado
     */
    public static boolean buscarDni( String dni, Connection con){

        boolean encontrado = false;
        try (PreparedStatement consulta = con.prepareStatement("select * from persona where ID_Persona = ?")) {
            consulta.setString(1, dni);
            ResultSet resultado = consulta.executeQuery();

            if (resultado.next() == false) {
                encontrado = false;
            }else{
                encontrado= true;
            }
            if (consulta != null) {consulta.close (); }//cierra
            if (resultado != null) {resultado.close (); }//cierra
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            return encontrado;
        }


    }

    /**
     * Borra una persona de la BBD
     * @param con es un objeto Connection para contactar con la BBDD
     */
    public static void borrarPersona(Connection con){
        Administrador.verPersonas(con);
        boolean encontrado = false;
        try {
            String dni = null;
            while (!encontrado) {
                Scanner lector = new Scanner(System.in);
                System.out.println("Escribe el DNI de la persona a eliminar");
                dni = lector.nextLine();
                encontrado = buscarDni(dni, con);
                System.out.println(encontrado);
            }
            String datosPersona = "delete from persona where ID_Persona = ?  ";
            PreparedStatement estatementpreparada = con.prepareStatement(datosPersona);
            estatementpreparada = con.prepareStatement(datosPersona);
            estatementpreparada.setString(1, dni);
            int filasBorradas = estatementpreparada.executeUpdate();

            if(filasBorradas > 0){
                System.out.println("Se ha eliminado el registro");
            }
            if (estatementpreparada != null) {estatementpreparada.close (); }//cierra
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}

