package Universidad;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
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

    /**
     * Metodo que hace un filtrado por Autor para ver solo los libros que hay de dicho Autor en la biblioteca y
     * pide si lo quieren escribir en un fichero o PDF
     * @param miConexion
     */
    public static void filtrarLibrosAutor(Connection miConexion){
        PreparedStatement prepStat = null;
        ResultSet resultado = null;
        System.out.println("Escribe el autor de los libros que quieras ver");
        System.out.print("Autor:");
        String autor = lector.nextLine();
        String atajo;

        try{

            prepStat = miConexion.prepareStatement("SELECT * FROM LIBRO WHERE Autor = ?");

            prepStat.setString(1, autor);

            resultado = prepStat.executeQuery();

            if(resultado.next() == false){
                System.out.println("\nNo hay libros de " + autor + ", lo siento");
            }else{
                System.out.println("\nEstos son los libros que hay de " + autor + "\n");
                do{

                    String tit = resultado.getString("TITULO_LIBRO");
                    String aut = resultado.getString("AUTOR");
                    String edi = resultado.getString("EDITORIAL");
                    int tot = resultado.getInt("CANTIDAD_TOTAL");
                    int rest  = resultado.getInt("CANTIDAD_RESTANTE");
                    String tem = resultado.getString("TEMATICA");

                    atajo = "TITULO: " + tit + "\n" +
                            "AUTOR: " + aut + "\n" +
                            "EDITORIAL: " + edi + "\n" +
                            "CANTIDAD TOTAL: " + tot + "\n" +
                            "CANTIDAD RESTANTE: " + rest + "\n" +
                            "TEMATICA: " + tem + "\n";
                    System.out.println(atajo);
                }while(resultado.next());

                System.out.println("Quieres imprimirlo en un fichero ? \n");
                System.out.println(" 1) En un fichero .txt");
                System.out.println(" 2) En un PDF");
                System.out.println(" 3) No quiero imprimir nada");
                int opcion = lector.nextInt();
                lector.nextLine();

                if(opcion == 1){
                    try(BufferedWriter bufferWriter = new BufferedWriter(new FileWriter("Ficheros/prueba.txt"))){

                          bufferWriter.write(atajo);

                        System.out.println("Se ha escrito correctamente el fichero .txt en la ruta Fichero/prueba.txt");

                    }catch(IOException e){
                        System.out.println("No he podido escribir el fichero .txt");
                        e.printStackTrace();
                    }catch(InputMismatchException e2){
                        System.out.println("No he encontrado el fichero .txt");
                    }
                }
                if(opcion == 2){
                    //PDF
                }
                if(opcion == 3){
                    System.out.println(":(");
                }


            }
        }catch(SQLException e){
            System.out.println("No he podido filtrar por autor");
            e.printStackTrace();
        }finally {
            try{
                if(prepStat != null){
                    prepStat.close();
                }
                if(resultado != null){
                    resultado.close();
                }
            }catch(SQLException e2){

            }
        }
    }

    /**
     * Pregunta al usuario que editorial quiere para consultar sus libros e imprime listado en txt
     * @param con objeto conexion para conectar con la bbdd
     */
    public static void filtrarLibrosEditorial(Connection con){
        boolean editValida = false;
        String nombreEdit = null;
        while(editValida ==false){
            Bibliotecario.mostrarEditoriales(con);
            System.out.println("Escribe el nombre de la editorial que quieres ");
            Scanner lector = new Scanner(System.in);
            nombreEdit = lector.nextLine();
            editValida = Bibliotecario.validarEditorial(nombreEdit, con);
        }
        PreparedStatement consulta = null;
        ResultSet resultados = null;


        try{
            File archivoSalida = new File("Ficheros/LibrosEditorial"+nombreEdit.toUpperCase()+".txt");
            consulta = con.prepareStatement("select * from libro where editorial = ?");
            consulta.setString(1, nombreEdit);
            resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay libros.");
            } else {
                BufferedWriter escritorMejorado = new BufferedWriter(new FileWriter(archivoSalida));;
                do {
                    String titulo = resultados.getString("Titulo_libro");
                    String autor = resultados.getString("autor");
                    String edit = resultados.getString("editorial");
                    String linea= "Titulo: " + titulo  + "--- Autor: "+autor + "--- Editorial: " +edit;
                    System.out.println(linea);
                    escritorMejorado.write(linea);
                    escritorMejorado.newLine();

                } while(resultados.next());
                escritorMejorado.close();
            }

        }catch(SQLException error) {
            System.out.println("Error en la consulta.");
        }catch (IOException e) {
                e.printStackTrace();
        }
        finally{
            try {
                if (resultados != null) resultados.close();
                if (consulta != null)consulta.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        }


    /**
     * Enseña las diferentes editoriales que tienen libros
     * @param con objeto conexion para conectar con la bbdd
     */
    public static void mostrarEditoriales( Connection con){
        try{
            PreparedStatement consulta = con.prepareStatement("select distinct editorial from libro");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay editoriales.");
            } else {
                System.out.println("----LISTADO DE EDITORIALES----");
                do {
                    String nombre = resultados.getString("Editorial");
                    System.out.println(nombre);
                } while(resultados.next());

            }
            if (resultados != null) {resultados.close (); }
            if (consulta != null) consulta.close ();

        }catch(SQLException error){
            System.out.println("Error en la consulta.");

        }

    }

    /**
     * Comprueba si una editorial existe dada un nombre
     * @param nombre es el nombre de la editorial para comprobar si existe
     * @param con objeto conexion para conectar con la bbdd
     * @return true si la editorial existe en la bbdd, y false si no existe
     */
    public static boolean validarEditorial( String nombre, Connection con){
        boolean encontrado = false;
        try (PreparedStatement consulta = con.prepareStatement("select editorial from libro where editorial = ?")) {
            consulta.setString(1, nombre);
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
    private static void verTematicasLibros(Connection connection){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select tematica from Libro");
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("----------------------");
            System.out.println("TEMATICAS");
            while (resultSet.next()){
                System.out.println("- " + resultSet.getString("tematica"));;
            }
            System.out.println("----------------------");


        }catch (SQLException e){
            System.out.println("SQLSTATE: " + e.getSQLState());
            System.out.println("SQLMESSAGE: " + e.getMessage());
        }finally {
            try {
                if(preparedStatement != null){preparedStatement.close();}

            }catch (SQLException e){
                System.out.println("SQLSTATE: " + e.getSQLState());
                System.out.println("SQLMESSAGE: " + e.getMessage());
            }
        }

    }

    public static void filtrarLibrosTematica(Connection connection) {
        PreparedStatement preparedStatement = null;
        String tematica;
        Boolean bool = false;
        try {
            while (!bool) {
                Bibliotecario.mostrarLibros(connection);
                System.out.println("----------------------");
                System.out.println("Introduce la ");
                preparedStatement = connection.prepareStatement("Select * from libro where Tematica = ?");

            }
        } catch (SQLException e) {

        }
    }

}