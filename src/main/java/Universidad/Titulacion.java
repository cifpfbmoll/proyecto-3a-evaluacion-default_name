import java.sql.*;

/**
 * @author Alberto
 */
public class Titulacion {
    
    // ATRIBUTOS 
    private int ID_titulacion;
    private String nombreTitulacion;
    
    // CONSTRUCTORES VACÍO, CON PARÁMETROS Y COPIA

    public Titulacion() {
    }
    
    public Titulacion(int ID_titulacion, String nombreTitulacion) {
        this.setID_titulacion(ID_titulacion);
        this.setNombreTitulacion(nombreTitulacion);
    }
    
    public Titulacion(Titulacion copiaTitulacion){
        this.setID_titulacion(copiaTitulacion.getID_titulacion());
        this.setNombreTitulacion(copiaTitulacion.getNombreTitulacion());
    }
    // GETTERS AND SETTERS

    public int getID_titulacion() {
        return ID_titulacion;
    }

    public void setID_titulacion(int ID_titulacion) {
        this.ID_titulacion = ID_titulacion;
    }

    public String getNombreTitulacion() {
        return nombreTitulacion;
    }

    public void setNombreTitulacion(String nombreTitulacion) {
        this.nombreTitulacion = nombreTitulacion;
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
                                   resultado.getString("ID_TITULACION") +
                                   resultado.getString("ID_PROFESOR") +
                                   resultado.getString("CURSO"));
            }
        }catch(Exception e){
            
            System.out.println("Lo siento, ha ocurrido un error y no se puede conectar a la Base de Datos.");
        }
    }
}
