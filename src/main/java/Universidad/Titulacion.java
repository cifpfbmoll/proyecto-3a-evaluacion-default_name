package Universidad;

import java.sql.*;

/**
 * @author Alberto
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

   // MÉTODOS
    
    public static void mostrarAsignaturas(){
        
        try{
            
            Connection miConexion = DriverManager.getConnection("jdbc:mysql://51.178.152.223:3306/dam4", "Dam4", "ProyectoGrupo4");
        
            PreparedStatement sentenciaPrep = miConexion.prepareStatement("SELECT * FROM ASIGNATURAS");
            
            ResultSet resultado = sentenciaPrep.executeQuery();
            
            while(resultado.next()){
                System.out.println(resultado.getString("ID_ASIGNATURA") +
                                   resultado.getString("NOMBRE_ASIGNATURA") +
                                   resultado.getString("ID_Titulacion") +
                                   resultado.getString("ID_PROFESOR") +
                                   resultado.getString("CURSO"));
            }
        }catch(Exception e){
            
            System.out.println("Lo siento, ha ocurrido un error y no se puede conectar a la Base de Datos.");
        }
    }
}
