package Universidad;

import java.sql.*;

/**
 * Clase titulacion. No tiene metodos
 * @author grupo3
 */
public class Titulacion {
    
    // ATRIBUTOS 
    private int ID_Titulacion;
    private String Nombre_Titulacion;
    
    // CONSTRUCTORES VACÍO, CON PARÁMETROS Y COPIA

    public Titulacion() {
    }
    
    public Titulacion(int ID_Titulacion, String Nombre_Titulacion) {
        this.setID_Titulacion(ID_Titulacion);
        this.setNombre_Titulacion(Nombre_Titulacion);
    }
    
    public Titulacion(Titulacion copiaTitulacion){
        this.setID_Titulacion(copiaTitulacion.getID_Titulacion());
        this.setNombre_Titulacion(copiaTitulacion.getNombre_Titulacion());
    }
    // GETTERS AND SETTERS

    public int getID_Titulacion() {
        return ID_Titulacion;
    }

    public void setID_Titulacion(int ID_Titulacion) {
        this.ID_Titulacion = ID_Titulacion;
    }

    public String getNombre_Titulacion() {
        return Nombre_Titulacion;
    }

    public void setNombre_Titulacion(String Nombre_Titulacion) {
        this.Nombre_Titulacion = Nombre_Titulacion;
    }

}
