package Universidad;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Bibliotecario extends Persona{
    //ATRÍBUTOS -- la clase no tiene atríbutos porque los hereda directamenete todos de persona

    //constructor vacío
    public Bibliotecario(){
    }


    //constructor con parametros
    public Bibliotecario(String ID_Persona, String nombre, int edad, String telefono, String contrasena, String rol) {
        super(ID_Persona, nombre, edad, telefono, contrasena, rol);
    }

    //constructor copia
    public Bibliotecario(Bibliotecario copiaBibliotecario){
        super((Persona)copiaBibliotecario);
    }

    // METODOS
    public static Scanner lector = new Scanner(System.in);
    /**
     * Metodo que pide al usuario los datos del libro y los anade a la BBDD
     * @param miConexion
     * @param datos
     */
    public static void anadirLibro(Connection miConexion, String[] datos){

        PreparedStatement prepStat = null;
        System.out.println("Vamos a anadir un libro:");

        System.out.println("Titulo del libro:");
        String titulo = lector.nextLine();
        System.out.println("Autor:");
        String autor = lector.nextLine();
        System.out.println("Editorial");
        String editorial = lector.nextLine();
        System.out.println("Cantidad de libros:");
        int cantidad = lector.nextInt();
        lector.nextLine();
        System.out.println("Tematica del libro:");
        String tematica = lector.nextLine();

        try{
            prepStat= miConexion.prepareStatement("INSERT INTO libro VALUES(?, ?, ?, 1, ?, ?, ?)");

            prepStat.setString(1, titulo);
            prepStat.setString(2, autor);
            prepStat.setString(3, editorial);
            prepStat.setInt(4, cantidad);
            prepStat.setInt(5, cantidad);
            prepStat.setString(6, tematica);

            int n = prepStat.executeUpdate();

        }catch(SQLException e){
            System.out.println("No he podido añadir el libro");
            e.printStackTrace();
        }finally{
            try{
                if(prepStat != null){
                    prepStat.close();
                }
            }catch (SQLException e){
                System.out.println("No he podido cerrar el preparedStatement");
            }
        }
    }

    /**
     * Metodo que muestra todos los libros guardados en la BBDD, llama a otro metodo de la clase Libro
     * @param miConexion
     */
    public static void mostrarLibros(Connection miConexion){

        Libro.mostrarLibros(miConexion);
    }

    /**
     * Metodo que muestra los libros y pregunta al usuario el titulo del libro que quiere eliminar, seguidamente lo elimina
     * @param miConexion
     */
    public static void eliminarLibro(Connection miConexion){

        System.out.println("Escribe el titulo del libro que quieres borrar: ");
        mostrarLibros(miConexion);
        System.out.println("---------------------------------");
        String titulo = lector.nextLine();
        PreparedStatement prepStat = null;

        try{
            prepStat = miConexion.prepareStatement("DELETE FROM LIBRO WHERE TITULO_LIBRO = ?");

            prepStat.setString(1, titulo);

            int n = prepStat.executeUpdate();

            System.out.println("Libro borrado con éxito");

        }catch(SQLException e){
            System.out.println("No he podido borrar el libro " + titulo);
            System.out.println("Revisa que lo hayas escrito bien");
            e.printStackTrace();
        }finally{
            try{
                if (prepStat != null){
                    prepStat.close();
                }
            }catch (SQLException e){
                System.out.println("No he podido cerrar el PreparedStatement");
            }
        }
    }

    /**
     * Metodo que reserva libros a los alumnos. Se utiliza una transaccion
     * @param miConexion
     */
    public static void reservarLibro(Connection miConexion){
        PreparedStatement prepStat = null;
        System.out.print("Escribe tu ID de Alumno: ");
        String id = lector.nextLine();
        System.out.println("Hola, dime cuantos libros quieres reservar (maximo 3)");
        int reservas = lector.nextInt();
        lector.nextLine();
        if (reservas > 3 || reservas <= 0){
            System.out.println("Solo puedes reservas de 1 a 3 libros. Vuelve a escribir la cantidad");
            reservas = lector.nextInt();
            lector.nextLine();
        }else{
            for(int i = 0; reservas > i; i++){
                System.out.println("Escribe el titulo del libro que quieras reservar");
                mostrarLibros(miConexion);
                System.out.println("Escribe el titulo correctamente");
                System.out.print("Titulo: ");
                String titulo = lector.nextLine();

                try{
                    // Inicio transaccion
                    miConexion.setAutoCommit(false);

                    prepStat = miConexion.prepareStatement("UPDATE LIBRO SET CANTIDAD_RESTANTE = CANTIDAD_RESTANTE-1 WHERE TITULO_LIBRO = ?");

                    prepStat.setString(1, titulo);

                    int n = prepStat.executeUpdate();

                    miConexion.commit();

                    prepStat = miConexion.prepareStatement("INSERT INTO LIBROS_RESERVADOS VALUES (?,?, default, default)");

                    prepStat.setString(1, id);
                    prepStat.setString(2, titulo);

                    int n2 = prepStat.executeUpdate();

                    System.out.println("Libro reservado con éxito para el alumno con ID = " + id);
                    miConexion.commit();
                }catch(SQLException e){
                    try{
                        miConexion.rollback();
                        e.printStackTrace();
                    }catch(SQLException e2){
                        System.out.println("No he podido hacer rollback");
                    }
                }finally {
                    try{
                        miConexion.setAutoCommit(true);
                        if(prepStat != null){
                            prepStat.close();
                        }
                    }catch(SQLException e){
                        System.out.println("No he podido cerrar el prepared Statement");
                    }
                }
            }
        }



    }
}