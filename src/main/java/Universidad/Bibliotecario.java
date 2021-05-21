package Universidad;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            System.out.println(e.getSQLState());
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
     * Metodo que muestra todos los libros guardados en la BBDD
     * @param miConexion
     */
    public static void mostrarLibros(Connection miConexion){

        PreparedStatement sentenciaPrep = null;
        ResultSet resultado = null;

        System.out.println("Estos son los libros que tenemos en la Biblioteca: \n");
        try {
            sentenciaPrep = miConexion.prepareStatement("SELECT * FROM LIBRO");

            resultado = sentenciaPrep.executeQuery();

            while (resultado.next()) {

                String titulo = resultado.getString("TITULO_LIBRO");
                String autor = resultado.getString("AUTOR");
                String editorial = resultado.getString("EDITORIAL");
                int id = resultado.getInt("ID_BIBLIOTECA");
                int cant_tot = resultado.getInt("CANTIDAD_TOTAL");
                int cant_rest = resultado.getInt("CANTIDAD_RESTANTE");
                String tematica = resultado.getString("TEMATICA");

                System.out.println("TITULO: " + titulo + " \n" +
                        "AUTOR: " + autor + " \n" +
                        "EDITORIAL: " + editorial + " \n" +
                        "ID_BIBLIOTECA: " + id + " \n" +
                        "CANTIDAD_TOTAL: " + cant_tot + " \n" +
                        "CANTIDAD_RESTANTE: " + cant_rest + " \n" +
                        "TEMATICA: " + tematica + " \n");
            }

        } catch (SQLException e) {
            System.out.println("Lo siento, ha ocurrido un error y no se puede conectar a la Base de Datos.");
            e.printStackTrace();
        } finally {
            try{
                if (sentenciaPrep != null) {
                    sentenciaPrep.close();
                }
                if (resultado != null) {
                    resultado.close();
                }
            }catch(SQLException e){
                System.out.println("No he podido cerrar los recursos");
                e.printStackTrace();
            }

        };
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
     * Metodo que muestra las tematicas disponibles de los libros
     * @param connection
     */
    private static void verTematicasLibros(Connection connection){
        //Declarar variables
        PreparedStatement preparedStatement = null;
        try {
            //Preparar statement
            preparedStatement = connection.prepareStatement("select tematica from Libro group by tematica");
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("----------------------");
            System.out.println("TEMATICAS: ");
            //Printear el resultado
            while (resultSet.next()){
                System.out.println("- " + resultSet.getString("tematica"));;
            }
            System.out.println("----------------------");

        //Recojer los errores
        }catch (SQLException e){
            System.out.println("SQLSTATE: " + e.getSQLState());
            System.out.println("SQLMESSAGE: " + e.getMessage());
        //Cerrar la conexion a la bbdd
        }finally {
            try {
                if(preparedStatement != null){preparedStatement.close();}

            }catch (SQLException e){
                System.out.println("SQLSTATE: " + e.getSQLState());
                System.out.println("SQLMESSAGE: " + e.getMessage());
            }
        }

    }

    /**
     * Metodo que filtra los libros por una tematica y los enseña en consola
     * @param connection
     */
    public static void filtrarLibrosTematica(Connection connection) {
        //Declarar variables
        Scanner scanner = new Scanner(System.in);
        PreparedStatement preparedStatement = null;
        String tematica;
        Boolean bool = false;
        try {
            while (!bool) {
                //Preparar sequencia SQL
                Bibliotecario.verTematicasLibros(connection);
                System.out.println("Introduce la tematica de la que quieres ver los libros: ");
                System.out.print("> ");
                tematica = scanner.nextLine();
                preparedStatement = connection.prepareStatement("Select * from libro where Tematica = ?");
                preparedStatement.setString(1, tematica);
                ResultSet resultSet = preparedStatement.executeQuery();//Ejecutar sequencia sql
                System.out.println("LIBROS DE TEMATICA " + tematica.toUpperCase());
                //Mostrar los resultados de la sequencia
                System.out.println("----------------------");
                while (resultSet.next()){
                    System.out.println("- Titulo: " + resultSet.getString("Titulo_Libro"));
                    System.out.println("- Autor: " + resultSet.getString("Autor"));
                    System.out.println("- Editorial: " + resultSet.getString("ID_Biblioteca"));
                    System.out.println("- Cantidad Total: " + resultSet.getString("Cantidad_Total"));
                    System.out.println("- Cantidad Restante: " + resultSet.getString("Cantidad_Restante"));
                    System.out.println("----------------------");
                    if(!resultSet.next()){
                        bool=true;
                        System.out.println("Quieres exportar los datos? (si/no)");
                        if(scanner.nextLine().equalsIgnoreCase("si")){
                            librosTematicaExportar(connection, tematica);
                        }

                    }
                }


            }
        //Recojer excepciones
        } catch (SQLException e) {
            System.out.println("SQLSTATE: " + e.getSQLState());
            System.out.println("SQLMESSAGE: " + e.getMessage());

        //Cerrar conexion
        }finally {
            try {
                //Cerrar PreparedStatement
                if(preparedStatement!= null){preparedStatement.close();}
            }catch (SQLException e){
                System.out.println("SQLSTATE: " + e.getSQLState());
                System.out.println("SQLMESSAGE: " + e.getMessage());
            }
        }
    }


    /**
     * Filtra los libros por una tematica y los exporta a un txt o pdf (usado por filtrarLibrosTematica(Connection connection))
     * @param connection
     * @param tematica
     */
    private static void librosTematicaExportar(Connection connection, String tematica) {
        //Declarar variables
        Scanner scanner = new Scanner(System.in);
        PreparedStatement preparedStatement = null;
        BufferedWriter bufferedWriter = null;
        Document document = null;
        PdfWriter pdfWriter = null;
        //pedir como desea exportar la informacion
        System.out.println("Donde quieres exportar los libros?");
        System.out.println("1. TXT");
        System.out.println("2. PDF");
        System.out.print("> ");
        try {
            //Preparar select con la tematica elegida
            preparedStatement = connection.prepareStatement("select * from libro where tematica = ?");
            preparedStatement.setString(1, tematica);
            ResultSet resultSet = preparedStatement.executeQuery();
            switch (Integer.parseInt(scanner.nextLine())) {
                case 1://TXT
                    //Crear el buffered writer i con el fichero
                    bufferedWriter = new BufferedWriter(new FileWriter("Ficheros/prueba.txt"));
                    //Escibir toda la informacion en el fichero TXT
                    while (resultSet.next()) {
                        bufferedWriter.write("- Titulo: " + resultSet.getString("Titulo_Libro"));
                        bufferedWriter.newLine();
                        bufferedWriter.write("- Autor: " + resultSet.getString("Autor"));
                        bufferedWriter.newLine();
                        bufferedWriter.write("- Editorial: " + resultSet.getString("ID_Biblioteca"));
                        bufferedWriter.newLine();
                        bufferedWriter.write("- Cantidad Total: " + resultSet.getString("Cantidad_Total"));
                        bufferedWriter.newLine();
                        bufferedWriter.write("- Cantidad Restante: " + resultSet.getString("Cantidad_Restante"));
                        bufferedWriter.newLine();
                        bufferedWriter.write("----------------------");
                        bufferedWriter.newLine();
                    }
                    break;
                case 2:
                    //Instancia de documento
                    document = new Document();
                    //Crear el pdf
                    pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("Ficheros/tematicas_" + tematica.toUpperCase() + ".pdf"));
                    //Abrir el documento
                    document.open();
                    //Escribir en el documento la informacion
                    document.add(new Paragraph("LIBROS DE LA TEMATICA " + tematica.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD)));
                    document.add(new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                    while (resultSet.next()){
                        document.add(new Paragraph("- Titulo: " + resultSet.getString("Titulo_Libro"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph("- Autor: " + resultSet.getString("Autor"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph("- Editorial: " + resultSet.getString("ID_Biblioteca"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph("- Cantidad Total: " + resultSet.getString("Cantidad_Total"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph("- Cantidad Restante: " + resultSet.getString("Cantidad_Restante"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        if(resultSet.next()) {
                            document.add(new Paragraph("----------------------", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                            document.add(new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        }

                    }
                    break;
                default:
                    System.out.println("Opcion erronea, no se va a exportar.");
            }
        //Recojer los errores que se hayan podido dar durante la ejecucion
        }catch (SQLException e){
            System.out.println("SQLSTATE: " + e.getSQLState());
            System.out.println("SQLMESSAGE: " + e.getMessage());
        }catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error durante el acceso al archivo");
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        //Cerrar todos las instancias y conexiones que se hayan podido abrir.
        } finally {
            try {
                if (bufferedWriter != null) { bufferedWriter.close();}
                if (preparedStatement != null){preparedStatement.close();}
                if (document != null){document.close();}
                if (pdfWriter != null){pdfWriter.close();}
            }catch (IOException e){
                System.out.println("El archivo no se ha podido cerrar");
                System.out.println("MENSAJE: " + e.getMessage());;
            }catch (SQLException e){
                System.out.println("SQLSTATE: " + e.getSQLState());
                System.out.println("SQLMESSAGE: " + e.getMessage());
            }
        }
    }
}
