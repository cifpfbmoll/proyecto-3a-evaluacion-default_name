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


        mostrarMenus(datos, miConexion);
        //Bibliotecario.filtrarLibrosTematica(miConexion);
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

    public static void mostrarMenus(String[] datos, Connection miConexion){
        switch(datos[1]){

            case "bibliotecario": menuBibliotecario(datos, miConexion); break;
            case "alumno": ; break;
            case "profesor": ; break;
            case "administrador": ; break;
            default: System.out.println("No has iniciado con tu usuario");
        }
    }
    public static void menuBibliotecario(String[] datos, Connection miConexion){

        boolean menu = false;

        while(!menu){
            switch (pedirOpcionBibliotecario()){
                case 1:
                    Bibliotecario.anadirLibro(miConexion, datos);
                    break;
                case 2:
                    Bibliotecario.eliminarLibro(miConexion);
                    break;
                case 3:
                    Bibliotecario.mostrarLibros(miConexion);
                    break;
                case 4:
                    while(!menu){
                        switch (pedirOpcionReserva()){
                            case 1:
                                Bibliotecario.reservarLibro(miConexion);
                                break;
                            case 2:
                                Bibliotecario.verReservas(miConexion);
                                break;
                            case 3:
                                Bibliotecario.verReservasFiltrado(miConexion);
                                break;
                            case 4:
                                menuBibliotecario(datos, miConexion);
                        }
                    }
                    break;
                case 5:
                    // devolverLibro()
                    //Bibliotecario
                    break;
                case 6:
                    Bibliotecario.mostrarEditoriales(miConexion);
                    break;
                case 7:
                    Bibliotecario.filtrarLibrosAutor(miConexion);
                    break;
                case 8:
                    Bibliotecario.filtrarLibrosEditorial(miConexion);
                    break;
                case 9:
                    Bibliotecario.filtrarLibrosTematica(miConexion);
                    break;
                case 10:
                    menu = true;
                    break;
                default:
                    System.out.println("Escribe una opcion correcta:");
                    break;
            }
        }

    }

    public static int pedirOpcionBibliotecario(){
        System.out.println("-----------------------------------------");
        System.out.println("Escribe la opcion que quieras realizar:  ");
        System.out.println("-----------------------------------------");
        System.out.println("        1. Anadir libro                  ");
        System.out.println("        2. Eliminar libro                ");
        System.out.println("        3. Ver libros                    ");
        System.out.println("        4. Menu Reservas                 ");
        System.out.println("        5. Devolver Libro                ");
        System.out.println("        6. Ver editoriales               ");
        System.out.println("        7. Filtrar libros por autor      ");
        System.out.println("        8. Filtrar libros por editorial  ");
        System.out.println("        9. Filtrar libros por tematica   ");
        System.out.println("       10. Salir                         ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");

        return lector.nextInt();
    }

    private static int pedirOpcionReserva(){
        System.out.println("-----------------------------------------");
        System.out.println("Escribe la opcion que quieras realizar:  ");
        System.out.println("-----------------------------------------");
        System.out.println("        1. Reservar libro                ");
        System.out.println("        2. Ver reservas                  ");
        System.out.println("        3. Filtrar reserva               ");
        System.out.println("        4. Volver al menu anterior      ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");
        return lector.nextInt();
    }
}
