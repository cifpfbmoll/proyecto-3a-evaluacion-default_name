/**
 *
 * @author Alberto
 */

import java.util.Scanner;
import java.sql.*;

public class Libro {
    
    // ATRIBUTOS 
    
    private String titulo;
    private String autor;
    private String editorial;
    private int ID_biblioteca;
    private int copias_totales;
    private int copias_disponibles;
    
    // CONSTRUCTOR VACÍO, CONSTRUCTOR CON PARÁMETROS Y CONSTRUCTOR COPIA

    public Libro() {
    }

    public Libro(String titulo, String autor, String editorial, int ID_biblioteca, int copias_totales, int copias_disponibles) {
        this.setTitulo(titulo);
        this.setAutor(autor);
        this.setEditorial(editorial);
        this.setID_biblioteca(ID_biblioteca);
        this.setCopias_totales(copias_totales);
        this.setCopias_totales(copias_totales);
    }
    
    public Libro(Libro copiaLibro){
        this.setTitulo(copiaLibro.getTitulo());
        this.setAutor(copiaLibro.getAutor());
        this.setEditorial(copiaLibro.getEditorial());
        this.setID_biblioteca(copiaLibro.getID_biblioteca());
        this.setCopias_totales(copiaLibro.getCopias_totales());
        this.setCopias_disponibles(copiaLibro.getCopias_disponibles());
    }
    
    // GETTERS AND SETTERS

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getID_biblioteca() {
        return ID_biblioteca;
    }

    public void setID_biblioteca(int ID_biblioteca) {
        this.ID_biblioteca = ID_biblioteca;
    }

    public int getCopias_totales() {
        return copias_totales;
    }

    public void setCopias_totales(int copias_totales) {
        this.copias_totales = copias_totales;
    }

    public int getCopias_disponibles() {
        return copias_disponibles;
    }

    public void setCopias_disponibles(int copias_disponibles) {
        this.copias_disponibles = copias_disponibles;
    }
    
    // MÉTODOS
    /**
     * Método que muestra todos los libros de la tabla LIBROS
     */
    public static void mostrarLibros(){
        
        try{
            /**
             * HABRIA QUE CREAR UNA CONEXIÓN CON LA BBDD, VOY
             * A LLAMARLA miConexion 
             */
            Connection miConexion = DriverManager.getConnection("jdbc:mysql://51.178.152.223:3306/dam4", "Dam4", "ProyectoGrupo4");
            
            PreparedStatement sentenciaPrep = miConexion.prepareStatement("SELECT * FROM LIBROS");
            
            /**
             * Una vez tenemos la sentencia preparada vamos a ejecutar la sentancia.
             */
            ResultSet resultado = sentenciaPrep.executeQuery();
            
            while(resultado.next()){
                System.out.println(resultado.getString("TITULO") + 
                                   resultado.getString("AUTOR") +
                                   resultado.getString("EDITORIAL") +
                                   resultado.getString("ID_BIBLIOTECA"));
            }
            
        }catch(Exception e){
            System.out.println("Lo siento, ha ocurrido un error y no se puede conectar a la Base de Datos.");
        }
        
        
        
    }
   
}
