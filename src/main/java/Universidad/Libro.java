package Universidad; /**
 *
 * @author Alberto
 */

import java.util.Scanner;
import java.sql.*;

public class Libro {

    // ATRIBUTOS 

    private String Titulo_Libro;
    private String Autor;
    private String Editorial;
    private int ID_Biblioteca;
    private int Cantidad_Total;
    private int Cantidad_Restante;
    private String Tematica;

    // CONSTRUCTOR VAC�O, CONSTRUCTOR CON PAR�METROS Y CONSTRUCTOR COPIA

    public Libro() {
    }

    public Libro(String Titulo_Libro, String Autor, String Editorial, int ID_Biblioteca, int Cantidad_Total, int Cantidad_Restante, String Tematica) {
        this.setTitulo_Libro(Titulo_Libro);
        this.setAutor(Autor);
        this.setEditorial(Editorial);
        this.setID_Biblioteca(ID_Biblioteca);
        this.setCantidad_Total(Cantidad_Total);
        this.setCantidad_Restante(Cantidad_Restante);
        this.setTematica(Tematica);
    }

    public Libro(Libro copiaLibro) {
        this.setTitulo_Libro(copiaLibro.getTitulo_Libro());
        this.setAutor(copiaLibro.getAutor());
        this.setEditorial(copiaLibro.getEditorial());
        this.setID_Biblioteca(copiaLibro.getCantidad_Total());
        this.setCantidad_Restante(copiaLibro.getCantidad_Restante());
        this.setTematica(copiaLibro.getTematica());
    }
    // GETTERS AND SETTERS

    public String getTitulo_Libro() {
        return Titulo_Libro;
    }

    public void setTitulo_Libro(String Titulo_Libro) {
        this.Titulo_Libro = Titulo_Libro;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String Autor) {
        this.Autor = Autor;
    }

    public String getEditorial() {
        return Editorial;
    }

    public void setEditorial(String Editorial) {
        this.Editorial = Editorial;
    }

    public int getID_Biblioteca() {
        return ID_Biblioteca;
    }

    public void setID_Biblioteca(int ID_Biblioteca) {
        this.ID_Biblioteca = ID_Biblioteca;
    }

    public int getCantidad_Total() {
        return Cantidad_Total;
    }

    public void setCantidad_Total(int Cantidad_Total) {
        this.Cantidad_Total = Cantidad_Total;
    }

    public int getCantidad_Restante() {
        return Cantidad_Restante;
    }

    public void setCantidad_Restante(int Cantidad_Restante) {
        this.Cantidad_Restante = Cantidad_Restante;
    }

    public String getTematica() {
        return Tematica;
    }

    public void setTematica(String tematica) {
        Tematica = tematica;
    }

    // M�TODOS

    /**
     * M�todo que muestra todos los libros de la tabla LIBROS
     */
    public static void mostrarLibros(Connection miConexion) throws SQLException {

        PreparedStatement sentenciaPrep = null;
        ResultSet resultado = null;

        System.out.println("Estos son los libros que tenemos en la Biblioteca: \n");
        try {
            /**
             * HABRIA QUE CREAR UNA CONEXI�N CON LA BBDD, VOY
             * A LLAMARLA miConexion 
             */

            sentenciaPrep = miConexion.prepareStatement("SELECT * FROM LIBRO");

            /**
             * Una vez tenemos la sentencia preparada vamos a ejecutar la sentancia.
             */
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
            if (sentenciaPrep != null) {
                sentenciaPrep.close();
            }
            if (resultado != null) {
                resultado.close();
            }
        }

    }
}
