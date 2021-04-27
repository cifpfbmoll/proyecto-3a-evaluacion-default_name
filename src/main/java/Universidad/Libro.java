/**
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
    
    // CONSTRUCTOR VACÍO, CONSTRUCTOR CON PARÁMETROS Y CONSTRUCTOR COPIA

    public Libro() {
    }

    public Libro(String Titulo_Libro, String Autor, String Editorial, int ID_Biblioteca, int Cantidad_Total, int Cantidad_Restante) {
        this.setTitulo_Libro(Titulo_Libro);
        this.setAutor(Autor);
        this.setEditorial(Editorial);
        this.setID_Biblioteca(ID_Biblioteca);
        this.setCantidad_Total(Cantidad_Total);
        this.setCantidad_Restante(Cantidad_Restante);
    }

    public Libro(Libro copiaLibro){
        this.setTitulo_Libro(copiaLibro.getTitulo_Libro());
        this.setAutor(copiaLibro.getAutor());
        this.setEditorial(copiaLibro.getEditorial());
        this.setID_Biblioteca(copiaLibro.getCantidad_Total());
        this.setCantidad_Restante(copiaLibro.getCantidad_Restante());
    }
    // GETTERS AND SETTERS
    
    public String getTitulo_Libro(){
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

    // MÉTODOS
    /**
     * Método que muestra todos los libros de la tabla LIBROS
     */
    public static void mostrarLibros() {
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
                                   resultado.getString("ID_BIBLIOTECA") +
                                   resultado.getString("CANTIDAD TOTAL") +
                                   resultado.getString("CANTIDAD RESTANTE"));
            }
            
        }catch(Exception e){
            System.out.println("Lo siento, ha ocurrido un error y no se puede conectar a la Base de Datos.");
        }
    }
   
}
