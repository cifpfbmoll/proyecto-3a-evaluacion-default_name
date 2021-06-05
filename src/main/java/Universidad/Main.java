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

        System.out.println("Has iniciado sesion como " + datos[1]);
        mostrarMenus(datos, miConexion);
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
            case "alumno": menuAlumno(datos, miConexion); break;
            case "profesor": menuProfesor(datos, miConexion); break;
            case "administrador": menuAdministrador(datos, miConexion); break;
            default: System.out.println("No has iniciado con tu usuario");
        }
    }

    private static void menuBibliotecario(String[] datos, Connection miConexion){

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
                    boolean menu2 = false;
                    while (!menu2){
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
                                menu2 = true;
                                break;
                            default:
                                System.out.println("Escribe una opcion correcta:");
                                break;
                        }
                    }
                    break;
                case 5:
                    Bibliotecario.devolverLibro(miConexion);
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

    private static int pedirOpcionBibliotecario(){
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
        System.out.println("        4. Volver al menu anterior       ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");
        return lector.nextInt();
    }

    private static void menuAlumno(String[] datos, Connection miConexion){

        boolean menu = false;

        while (!menu){
            switch (pedirOpcionAlumno()){
                case 1:
                    // Dar de alta matricula
                    break;
                case 2:
                    Alumno.bajaMatricula(miConexion, datos[0]);
                    break;
                case 3:
                    Alumno.verEstadoAsignaturas(miConexion, datos[0]);
                    break;
                case 4:
                    Alumno.verMatriculaciones(miConexion, datos[0]);
                    break;
                case 5:
                    menu = true;
                    break;
                default:
                    System.out.println("Escribe una opcion correcta:");
                    break;
            }
        }
    }

    private static int pedirOpcionAlumno(){
        System.out.println("-----------------------------------------");
        System.out.println("Escribe la opcion que quieras realizar:  ");
        System.out.println("-----------------------------------------");
        System.out.println("     1. Dar de alta en una matricula     ");
        System.out.println("     2. Dar de baja en una matricula     ");
        System.out.println("     3. Ver el estado de una asignatura  ");
        System.out.println("     4. Ver matriculaciones              ");
        System.out.println("     5. Salir                            ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");
        return lector.nextInt();
    }

    private static void menuProfesor(String[] datos, Connection miConexion){

        boolean menu = false;

        while (!menu){
            switch (pedirOpcionProfesor()){
                case 1:
                    Profesor.mostrarAlumnos(miConexion, datos);
                    break;
                case 2:
                    Profesor.ponerNota(miConexion, datos);
                    break;
                case 3:
                    Profesor.eliminarNotaAAlumno(miConexion, datos);
                    break;
                case 4:
                    menu = true;
                    break;
                default:
                    System.out.println("Escribe una opcion correcta:");
                    break;
            }
        }
    }

    private static int pedirOpcionProfesor(){
        System.out.println("-----------------------------------------");
        System.out.println("Escribe la opcion que quieras realizar:  ");
        System.out.println("-----------------------------------------");
        System.out.println("    1. Mostrar alumnos                   ");
        System.out.println("    2. Poner nota                        ");
        System.out.println("    3. Eliminar nota a un alumno         ");
        System.out.println("    4. Salir                             ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");
        return lector.nextInt();
    }

    private static void menuAdministrador(String[] datos, Connection miConexion){

        boolean menu = false;

        while (!menu){
            switch (pedirOpcionAdministrador()){
                case 1:
                    boolean menuPersonas = false;
                    while (!menuPersonas){
                        switch (pedirOpcionAdminPersonas()){
                            case 1:
                                Administrador.anadirPersona(miConexion);
                                break;
                            case 2:
                                Administrador.borrarPersona(miConexion);
                                break;
                            case 3:
                                Administrador.verPersonas(miConexion);
                                break;
                            case 4:
                                Administrador.mostrarPersonasAtributo(miConexion);
                                break;
                            case 5:
                                Administrador.verProfesores(miConexion);
                                break;
                            case 6:
                                Administrador.verAlumnos(miConexion);
                                break;
                            case 7:
                                Administrador.verBibliotecarios(miConexion);
                                break;
                            case 8:
                                Administrador.verAdministradores(miConexion);
                                break;
                            case 9:
                                menuPersonas = true;
                                break;
                            default:
                                System.out.println("Escribe una opcion correcta:");
                                break;
                        }
                    }
                    break;
                case 2:
                    boolean menuTitulaciones = false;
                    while (!menuTitulaciones){
                        switch (pedirOpcionAdminTitulaciones()){
                            case 1:
                                Administrador.anadirTitulacion(miConexion);
                                break;
                            case 2:
                                Administrador.eliminarTitulacion(miConexion);
                                break;
                            case 3:
                                Administrador.verTitulaciones(miConexion);
                                break;
                            case 4:
                                menuTitulaciones = true;
                                break;
                            default:
                                System.out.println("Escribe una opcion correcta:");
                                break;
                        }
                    }
                    break;
                case 3:
                    boolean menuDepartamentos = false;
                    while (!menuDepartamentos){
                        switch (pedirOpcionAdminDepartamentos()){
                            case 1:
                                Administrador.anadirDepartamento(miConexion);
                                break;
                            case 2:
                                Administrador.eliminarDepartamento(miConexion);
                                break;
                            case 3:
                                Administrador.verDepartamento(miConexion);
                                break;
                            case 4:
                                menuDepartamentos = true;
                                break;
                            default:
                                System.out.println("Escribe una opcion correcta:");
                                break;
                        }
                    }
                    break;
                case 4:
                    boolean menuAsignaturas = false;
                    while (!menuAsignaturas){
                        switch (pedirOpcionAdminAsignaturas()){
                            case 1:
                                Administrador.anadirAsignatura(miConexion);
                                break;
                            case 2:
                                Administrador.borrarAsignatura(miConexion);
                                break;
                            case 3:
                                Administrador.verAsignaturas(miConexion);
                                break;
                            case 4:
                                menuAsignaturas = true;
                                break;
                            default:
                                System.out.println("Escribe una opcion correcta:");
                                break;
                        }
                    }
                    break;
                case 5:
                    menu = true;
                    break;
                default:
                    System.out.println("Escribe una opcion correcta:");
                    break;

            }
        }
    }

    private static int pedirOpcionAdministrador(){
        System.out.println("-----------------------------------------");
        System.out.println("Escribe la opcion que quieras realizar:  ");
        System.out.println("-----------------------------------------");
        System.out.println("    1. Administracion personas           ");
        System.out.println("    2. Administracion titulaciones       ");
        System.out.println("    3. Administracion departamentos      ");
        System.out.println("    4. Administracion asignaturas        ");
        System.out.println("    5. Salir                             ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");
        return lector.nextInt();
    }

    private static int pedirOpcionAdminPersonas(){
        System.out.println("-----------------------------------------");
        System.out.println("Escribe la opcion que quieras realizar:  ");
        System.out.println("-----------------------------------------");
        System.out.println("       1. Anadir persona                 ");
        System.out.println("       2. Borrar persona                 ");
        System.out.println("       3. Mostrar personas               ");
        System.out.println("       4. Mostrar personas por atributo  ");
        System.out.println("       5. Mostrar profesores             ");
        System.out.println("       6. Mostrar alumnos                ");
        System.out.println("       7. Mostrar bibliotecarios         ");
        System.out.println("       8. Mostrar administradores        ");
        System.out.println("       9. Volver al menu anterior        ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");
        return lector.nextInt();
    }

    private static int pedirOpcionAdminTitulaciones(){
        System.out.println("-----------------------------------------");
        System.out.println("Escribe la opcion que quieras realizar:  ");
        System.out.println("-----------------------------------------");
        System.out.println("       1. Anadir titulacion              ");
        System.out.println("       2. Eliminar titulacion            ");
        System.out.println("       3. Mostrar titulaciones           ");
        System.out.println("       4. Volver al menu anterior        ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");
        return lector.nextInt();
    }

    private static int pedirOpcionAdminDepartamentos(){
        System.out.println("-----------------------------------------");
        System.out.println("Escribe la opcion que quieras realizar:  ");
        System.out.println("-----------------------------------------");
        System.out.println("       1. Anadir departamento            ");
        System.out.println("       2. Eliminar departamento          ");
        System.out.println("       3. Mostrar departamentos          ");
        System.out.println("       4. Volver al menu anterior        ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");
        return lector.nextInt();
    }

    private static int pedirOpcionAdminAsignaturas(){
        System.out.println("-----------------------------------------");
        System.out.println("Escribe la opcion que quieras realizar:  ");
        System.out.println("-----------------------------------------");
        System.out.println("       1. Anadir asignatura              ");
        System.out.println("       2. Eliminar asignatura            ");
        System.out.println("       3. Mostrar asignaturas            ");
        System.out.println("       4. Volver al menu anterior        ");
        System.out.println("-----------------------------------------");
        System.out.print("Opcion: ");
        return lector.nextInt();
    }
}

