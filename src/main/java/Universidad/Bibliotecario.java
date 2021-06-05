package Universidad;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.*;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Bibliotecario extends Persona{
    //ATRÍBUTOS
    private static Scanner lector = new Scanner(System.in);

    //constructor vacío
    public Bibliotecario() {
    }


    //constructor con parametros
    public Bibliotecario(String ID_Persona, String nombre, int edad, String telefono, String contrasena, String rol) {
        super(ID_Persona, nombre, edad, telefono, contrasena, rol);
    }

    //constructor copia
    public Bibliotecario(Bibliotecario copiaBibliotecario) {
        super((Persona) copiaBibliotecario);
    }

    /**
     * Metodo que pide al usuario los datos del libro y los anade a la BBDD
     *
     * @param miConexion
     * @param datos
     */
    public static void anadirLibro(Connection miConexion, String[] datos) {

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

        try {
            prepStat = miConexion.prepareStatement("INSERT INTO libro VALUES(?, ?, ?, 1, ?, ?, ?)");

            prepStat.setString(1, titulo);
            prepStat.setString(2, autor);
            prepStat.setString(3, editorial);
            prepStat.setInt(4, cantidad);
            prepStat.setInt(5, cantidad);
            prepStat.setString(6, tematica);

            int n = prepStat.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No he podido añadir el libro");
            e.printStackTrace();
        } finally {
            try {
                if (prepStat != null) {
                    prepStat.close();
                }
            } catch (SQLException e) {
                System.out.println("No he podido cerrar el preparedStatement");
            }
        }
    }

    /**
     * Metodo que muestra todos los libros guardados en la BBDD
     *
     * @param miConexion
     */
    public static void mostrarLibros(Connection miConexion) {

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
            try {
                if (sentenciaPrep != null) {
                    sentenciaPrep.close();
                }
                if (resultado != null) {
                    resultado.close();
                }
            } catch (SQLException e) {
                System.out.println("No he podido cerrar los recursos");
                e.printStackTrace();
            }

        }
        ;
    }

    /**
     * Metodo que muestra los libros y pregunta al usuario el titulo del libro que quiere eliminar, seguidamente lo elimina
     *
     * @param miConexion
     */
    public static void eliminarLibro(Connection miConexion) {

        System.out.println("Escribe el titulo del libro que quieres borrar: ");
        mostrarLibros(miConexion);
        System.out.println("---------------------------------");
        String titulo = lector.nextLine();
        PreparedStatement prepStat = null;

        try {
            prepStat = miConexion.prepareStatement("DELETE FROM LIBRO WHERE TITULO_LIBRO = ?");

            prepStat.setString(1, titulo);

            int n = prepStat.executeUpdate();

            System.out.println("Libro borrado con éxito");

        } catch (SQLException e) {
            System.out.println("No he podido borrar el libro " + titulo);
            System.out.println("Revisa que lo hayas escrito bien");
            e.printStackTrace();
        } finally {
            try {
                if (prepStat != null) {
                    prepStat.close();
                }
            } catch (SQLException e) {
                System.out.println("No he podido cerrar el PreparedStatement");
            }
        }
    }

    /**
     * Metodo que muestra las tematicas disponibles de los libros
     * @param connection
     */

    /**
     * Metodo que reserva libros a los alumnos. Se utiliza una transaccion
     *
     * @param miConexion
     */
    public static void reservarLibro(Connection miConexion) {
        PreparedStatement prepStat = null;
        System.out.print("Escribe tu ID de Alumno: ");
        String id = lector.nextLine();
        System.out.println("Hola, dime cuantos libros quieres reservar (maximo 3)");
        int reservas = lector.nextInt();
        lector.nextLine();
        if (reservas > 3 || reservas <= 0) {
            System.out.println("Solo puedes reservas de 1 a 3 libros. Vuelve a escribir la cantidad");
            reservas = lector.nextInt();
            lector.nextLine();
        } else {
            for (int i = 0; reservas > i; i++) {
                System.out.println("Escribe el titulo del libro que quieras reservar");
                mostrarLibros(miConexion);
                System.out.println("Escribe el titulo correctamente");
                System.out.print("Titulo: ");
                String titulo = lector.nextLine();

                try {
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
                } catch (SQLException e) {
                    try {
                        miConexion.rollback();
                        e.printStackTrace();
                    } catch (SQLException e2) {
                        System.out.println("No he podido hacer rollback");
                    }
                } finally {
                    try {
                        miConexion.setAutoCommit(true);
                        if (prepStat != null) {
                            prepStat.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("No he podido cerrar el prepared Statement");
                    }
                }
            }
        }
    }

    /**
     * Metodo que hace un filtrado por Autor para ver solo los libros que hay de dicho Autor en la biblioteca y
     * pide si lo quieren escribir en un fichero o PDF
     *
     * @param miConexion
     */
    public static void filtrarLibrosAutor(Connection miConexion) {
        PreparedStatement prepStat = null;
        ResultSet resultado = null;
        System.out.println("Escribe el autor de los libros que quieras ver");
        System.out.print("Autor:");
        String autor = lector.nextLine();
        String atajo;

        try {

            prepStat = miConexion.prepareStatement("SELECT * FROM LIBRO WHERE Autor = ?");

            prepStat.setString(1, autor);

            resultado = prepStat.executeQuery();

            if (resultado.next() == false) {
                System.out.println("\nNo hay libros de " + autor + ", lo siento");
            } else {
                System.out.println("\nEstos son los libros que hay de " + autor + "\n");
                do {

                    String tit = resultado.getString("TITULO_LIBRO");
                    String aut = resultado.getString("AUTOR");
                    String edi = resultado.getString("EDITORIAL");
                    int tot = resultado.getInt("CANTIDAD_TOTAL");
                    int rest = resultado.getInt("CANTIDAD_RESTANTE");
                    String tem = resultado.getString("TEMATICA");

                    atajo = "TITULO: " + tit + "\n" +
                            "AUTOR: " + aut + "\n" +
                            "EDITORIAL: " + edi + "\n" +
                            "CANTIDAD TOTAL: " + tot + "\n" +
                            "CANTIDAD RESTANTE: " + rest + "\n" +
                            "TEMATICA: " + tem + "\n";
                    System.out.println(atajo);
                } while (resultado.next());

                System.out.println("Quieres imprimirlo en un fichero ? \n");
                System.out.println(" 1) En un fichero .txt");
                System.out.println(" 2) En un PDF");
                System.out.println(" 3) No quiero imprimir nada");
                int opcion = lector.nextInt();
                lector.nextLine();

                if (opcion == 1) {
                    try (BufferedWriter bufferWriter = new BufferedWriter(new FileWriter("Ficheros/prueba.txt"))) {

                        bufferWriter.write(atajo);

                        System.out.println("Se ha escrito correctamente el fichero .txt en la ruta Fichero/prueba.txt");

                    } catch (IOException e) {
                        System.out.println("No he podido escribir el fichero .txt");
                        e.printStackTrace();
                    } catch (InputMismatchException e2) {
                        System.out.println("No he encontrado el fichero .txt");
                    }
                }
                if (opcion == 2) {
                    //PDF
                }
                if (opcion == 3) {
                    System.out.println(":(");
                }


            }
        } catch (SQLException e) {
            System.out.println("No he podido filtrar por autor");
            e.printStackTrace();
        } finally {
            try {
                if (prepStat != null) {
                    prepStat.close();
                }
                if (resultado != null) {
                    resultado.close();
                }
            } catch (SQLException e2) {

            }
        }
    }

    /**
     * Pregunta al usuario que editorial quiere para consultar sus libros e imprime listado en txt
     *
     * @param con objeto conexion para conectar con la bbdd
     */
    public static void filtrarLibrosEditorial(Connection con) {
        boolean editValida = false;
        String nombreEdit = null;
        while (editValida == false) {
            Bibliotecario.mostrarEditoriales(con);
            System.out.println("Escribe el nombre de la editorial que quieres ");

            nombreEdit = lector.nextLine();
            editValida = Bibliotecario.validarEditorial(nombreEdit, con);
        }
        PreparedStatement consulta = null;
        ResultSet resultados = null;


        try {
            File archivoSalida = new File("Ficheros/LibrosEditorial" + nombreEdit.toUpperCase() + ".txt");
            consulta = con.prepareStatement("select * from libro where editorial = ?");
            consulta.setString(1, nombreEdit);
            resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay libros.");
            } else {
                BufferedWriter escritorMejorado = new BufferedWriter(new FileWriter(archivoSalida));
                ;
                do {
                    String titulo = resultados.getString("Titulo_libro");
                    String autor = resultados.getString("autor");
                    String edit = resultados.getString("editorial");
                    String linea = "Titulo: " + titulo + "--- Autor: " + autor + "--- Editorial: " + edit;
                    System.out.println(linea);
                    escritorMejorado.write(linea);
                    escritorMejorado.newLine();

                } while (resultados.next());
                escritorMejorado.close();
            }

        } catch (SQLException error) {
            System.out.println("Error en la consulta.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultados != null) resultados.close();
                if (consulta != null) consulta.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }


    /**
     * Enseña las diferentes editoriales que tienen libros
     *
     * @param con objeto conexion para conectar con la bbdd
     */
    public static void mostrarEditoriales(Connection con) {
        try {
            PreparedStatement consulta = con.prepareStatement("select distinct editorial from libro");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay editoriales.");
            } else {
                System.out.println("----LISTADO DE EDITORIALES----");
                do {
                    String nombre = resultados.getString("Editorial");
                    System.out.println(nombre);
                } while (resultados.next());

            }
            if (resultados != null) {
                resultados.close();
            }
            if (consulta != null) consulta.close();

        } catch (SQLException error) {
            System.out.println("Error en la consulta.");

        }

    }

    /**
     * Comprueba si una editorial existe dada un nombre
     *
     * @param nombre es el nombre de la editorial para comprobar si existe
     * @param con    objeto conexion para conectar con la bbdd
     * @return true si la editorial existe en la bbdd, y false si no existe
     */
    public static boolean validarEditorial(String nombre, Connection con) {
        boolean encontrado = false;
        try (PreparedStatement consulta = con.prepareStatement("select editorial from libro where editorial = ?")) {
            consulta.setString(1, nombre);
            ResultSet resultado = consulta.executeQuery();

            if (resultado.next() == false) {
                encontrado = false;
            } else {
                encontrado = true;
            }
            if (consulta != null) {
                consulta.close();
            }//cierra
            if (resultado != null) {
                resultado.close();
            }//cierra
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            return encontrado;
        }
    }

    private static void verTematicasLibros(Connection connection) {
        //Declarar variables
        PreparedStatement preparedStatement = null;
        try {
            //Preparar statement
            preparedStatement = connection.prepareStatement("select tematica from Libro group by tematica");
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("----------------------");
            System.out.println("TEMATICAS: ");
            //Printear el resultado
            while (resultSet.next()) {
                System.out.println("- " + resultSet.getString("tematica"));
                ;
            }
            System.out.println("----------------------");

            //Recojer los errores
        } catch (SQLException e) {
            System.out.println("SQLSTATE: " + e.getSQLState());
            System.out.println("SQLMESSAGE: " + e.getMessage());
            //Cerrar la conexion a la bbdd
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                System.out.println("SQLSTATE: " + e.getSQLState());
                System.out.println("SQLMESSAGE: " + e.getMessage());
            }
        }

    }

    /**
     * Metodo que filtra los libros por una tematica y los enseña en consola
     *
     * @param connection
     */
    public static void filtrarLibrosTematica(Connection connection) {
        //Declarar variables
        Scanner scanner = new Scanner(System.in);
        PreparedStatement preparedStatement = null;
        String tematica;
        Boolean bool = false;
        Boolean informacion = false;
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
                while (resultSet.next()) {
                    System.out.println("- Titulo: " + resultSet.getString("Titulo_Libro"));
                    System.out.println("- Autor: " + resultSet.getString("Autor"));
                    System.out.println("- Editorial: " + resultSet.getString("ID_Biblioteca"));
                    System.out.println("- Cantidad Total: " + resultSet.getString("Cantidad_Total"));
                    System.out.println("- Cantidad Restante: " + resultSet.getString("Cantidad_Restante"));
                    System.out.println("----------------------");
                    informacion = true;

                }
                if (informacion) {
                    bool = true;
                    System.out.println("Quieres exportar los datos? (si/no)");
                    if (scanner.nextLine().equalsIgnoreCase("si")) {
                        librosTematicaExportar(connection, tematica);
                    }
                } else {
                    System.out.println("No se ha encontrado esa tematica, inserta otra.");
                }


            }
            //Recojer excepciones
        } catch (SQLException e) {
            System.out.println("SQLSTATE: " + e.getSQLState());
            System.out.println("SQLMESSAGE: " + e.getMessage());

            //Cerrar conexion
        } finally {
            try {
                //Cerrar PreparedStatement
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLSTATE: " + e.getSQLState());
                System.out.println("SQLMESSAGE: " + e.getMessage());
            }
        }
    }


    /**
     * Filtra los libros por una tematica y los exporta a un txt o pdf (usado por filtrarLibrosTematica(Connection connection))
     *
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
                    bufferedWriter = new BufferedWriter(new FileWriter("Ficheros/tematica_" + tematica.toUpperCase() + ".txt"));
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
                    pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("Ficheros/tematica_" + tematica.toUpperCase() + ".pdf"));
                    //Abrir el documento
                    document.open();
                    //Escribir en el documento la informacion
                    document.add(new Paragraph("LIBROS DE LA TEMATICA " + tematica.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD)));
                    document.add(new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                    while (resultSet.next()) {
                        document.add(new Paragraph("- Titulo: " + resultSet.getString("Titulo_Libro"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph("- Autor: " + resultSet.getString("Autor"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph("- Editorial: " + resultSet.getString("ID_Biblioteca"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph("- Cantidad Total: " + resultSet.getString("Cantidad_Total"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph("- Cantidad Restante: " + resultSet.getString("Cantidad_Restante"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        document.add(new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        if (resultSet.next()) {
                            document.add(new Paragraph("----------------------", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                            document.add(new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                        }

                    }
                    break;
                default:
                    System.out.println("Opcion erronea, no se va a exportar.");
            }
            //Recojer los errores que se hayan podido dar durante la ejecucion
        } catch (SQLException e) {
            System.out.println("SQLSTATE: " + e.getSQLState());
            System.out.println("SQLMESSAGE: " + e.getMessage());
        } catch (FileNotFoundException e) {
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
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (document != null) {
                    document.close();
                }
                if (pdfWriter != null) {
                    pdfWriter.close();
                }
            } catch (IOException e) {
                System.out.println("El archivo no se ha podido cerrar");
                System.out.println("MENSAJE: " + e.getMessage());
                ;
            } catch (SQLException e) {
                System.out.println("SQLSTATE: " + e.getSQLState());
                System.out.println("SQLMESSAGE: " + e.getMessage());
            }
        }
    }

    /**
     * Muestra todas las reservas de libros
     * @param con objeto conexión para conectar a la BBDD
     */
    public static void verReservas(Connection con) {
        try {
            PreparedStatement consulta = con.prepareStatement("select * from libros_reservados");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay titulaciones.");
            } else {
                System.out.println("----------LISTA DE RESERVAS ---------");
                do {
                    String alumno = resultados.getString("ID_Alumno");
                    String libro = resultados.getString("Titulo_libro");
                    String fechaReserva = resultados.getString("Fecha_reserva");
                    String fechaDevolucion = resultados.getString("Fecha_devolucion");
                    System.out.println("Alumno: " + alumno + " ---Titulo libro: " + libro );
                    System.out.println("Fecha Reserva: " + fechaReserva + " ---Fecha devolucion: " + fechaDevolucion);
                    System.out.println("----------------------------------------");
                } while (resultados.next());

            }
            if (resultados != null) {
                resultados.close();
            }
            if (consulta != null) consulta.close();
        } catch (SQLException throwables) {
            System.out.println(" Error en la consulta");
            throwables.printStackTrace();
        }
    }

    /**
     * Muestra las reservas filtrando por titulo de libro o por dni del alumno
     * @param con  objeto conexión para conectar a la BBDD
     */
    public static void verReservasFiltrado(Connection con){
        System.out.println("Escribe A) Filtrar por alumno");
        System.out.println("Escribe B) Filtrar por libro");
        verReservas(con);
        switch (lector.nextLine()){
            case "A":
                System.out.println("Escribe el DNI del alumno");
                String dni = lector.nextLine();
                verReservasDni(dni, con);
                break;
            case "B":
                System.out.println("Escribe el titulo del libro");
                String libro = lector.nextLine();
                verReservasLibro(libro, con);
                break;
            default:
                System.out.println("Opcion incorrecta");
                break;
        }
    }

    /**
     * Muestra todas las reservas de un alumno en concreto
     * @param dni el dni del alumno a buscar
     * @param con objeto conexión para conectar a la BBDD
     */
    private static void verReservasDni(String dni, Connection con){
        try {
            PreparedStatement consulta = con.prepareStatement("select * from libros_reservados where id_alumno = ?");
            consulta.setString(1, dni);
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay reservas.");
            } else {
                System.out.println("----------LISTA DE RESERVAS ---------");
                do {
                    String alumno = resultados.getString("ID_Alumno");
                    String libro = resultados.getString("Titulo_libro");
                    String fechaReserva = resultados.getString("Fecha_reserva");
                    String fechaDevolucion = resultados.getString("Fecha_devolucion");
                    System.out.println("Alumno: " + alumno + " ---Titulo libro: " + libro );
                    System.out.println("Fecha Reserva: " + fechaReserva + " ---Fecha devolucion: " + fechaDevolucion);
                    System.out.println("----------------------------------------");
                } while (resultados.next());

            }
            if (resultados != null) {
                resultados.close();
            }
            if (consulta != null) consulta.close();
        } catch (SQLException throwables) {
            System.out.println(" Error en la consulta");
            throwables.printStackTrace();
        }
    }

    /**
     * Muestra todas las reservas que tiene un título de libro
     * @param libro el titulo del libro a buscar en la lista de reservas
     * @param con objeto conexión para conectar a la BBDD
     */
    private static void verReservasLibro(String libro, Connection con){
        try {
            PreparedStatement consulta = con.prepareStatement("select * from libros_reservados where titulo_libro = ?");
            consulta.setString(1, libro);
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay reservas.");
            } else {
                System.out.println("----------LISTA DE RESERVAS ---------");
                do {
                    String alumno = resultados.getString("ID_Alumno");
                    String titulo = resultados.getString("Titulo_libro");
                    String fechaReserva = resultados.getString("Fecha_reserva");
                    String fechaDevolucion = resultados.getString("Fecha_devolucion");
                    System.out.println("Alumno: " + alumno + " ---Titulo libro: " + titulo );
                    System.out.println("Fecha Reserva: " + fechaReserva + " ---Fecha devolucion: " + fechaDevolucion);
                    System.out.println("----------------------------------------");
                } while (resultados.next());

            }
            if (resultados != null) {
                resultados.close();
            }
            if (consulta != null) consulta.close();
        } catch (SQLException throwables) {
            System.out.println(" Error en la consulta");
            throwables.printStackTrace();
        }
    }

    /**
     * pide el alumno que devuelve el libro i actualiza el atributo Cantidad_Restante aumentandolo por 1 en
     * libros i elimina de la tabla libros reservados el libro que se ha devuelto
     * @param miConexion
     */
    public static void devolverLibro(Connection miConexion){
        PreparedStatement prepStat = null;
        PreparedStatement psLibrosAlumnos = null;
        ResultSet rsLibrosAlumnos = null;
        String titulo;
        System.out.print("Escribe el ID del Alumno: ");
        String id = lector.nextLine();
        try{
            miConexion.setAutoCommit(false);
            psLibrosAlumnos = miConexion.prepareStatement("SELECT * FROM libros_reservados where id_alumno = ?");
            psLibrosAlumnos.setString(1, id);
            rsLibrosAlumnos = psLibrosAlumnos.executeQuery();
            System.out.println("A continuación se mostrarán los títulos de los libros que tiene reservados.");
            while (rsLibrosAlumnos.next()) {
                System.out.println("- TITULO: " + rsLibrosAlumnos.getString("titulo_libro"));
            }
            System.out.println("Escoge el titulo del libro que quieras devolver: ");
            titulo = lector.nextLine();
            prepStat = miConexion.prepareStatement("UPDATE LIBRO SET CANTIDAD_RESTANTE = CANTIDAD_RESTANTE+1 WHERE TITULO_LIBRO = ?");
            prepStat.setString(1, titulo);

            int g = prepStat.executeUpdate();

            miConexion.commit();
            prepStat = miConexion.prepareStatement("DELETE FROM LIBROS_RESERVADOS WHERE ID_Alumno = ? AND titulo_libro = ?");
            prepStat.setString(1, id);
            prepStat.setString(2, titulo);

            int g2 = prepStat.executeUpdate();

            System.out.println("Libro devuelto con éxito para el alumno con ID = " + id);
            miConexion.commit();
        }catch(SQLException e){
            try{
                miConexion.rollback();
                System.out.println("Ha habido un error en la devolucion, comprueba que los datos son correctos.");
            }catch(SQLException e2){
                System.out.println("No se ha podido hacer rollback");
            }
        }finally {
            try{
                miConexion.setAutoCommit(true);
                if(rsLibrosAlumnos != null){
                    rsLibrosAlumnos.close();
                }
                if(prepStat != null){
                    prepStat.close();
                }
                if(psLibrosAlumnos != null){
                    psLibrosAlumnos.close();
                }
            }catch(SQLException e){
                System.out.println("No se ha podido cerrar el prepared Statement");
            }
        }
    }

}
