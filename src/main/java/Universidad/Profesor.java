package Universidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Profesor extends Persona{


    // ATRIBUTOS
    private String ID_Departamento;

    // CONSTRUCTOR VACÍO, CONSTRUCTOR CON PARÁMETROS, CONSTRUCTOR COPIA

    public Profesor() {
    }

    public Profesor(String ID_Persona, String nombre, int edad, String telefono, String contrasena) {
        super(ID_Persona, nombre, edad, telefono, contrasena);
        this.setID_Departamento(ID_Departamento);
    }

    public Profesor(Profesor copiaProfesor){
        super((Persona)copiaProfesor);
        this.setID_Departamento(copiaProfesor.getID_Departamento());
    }

    // GETTERS & SETTERS

    public String getID_Departamento() {
        return ID_Departamento;
    }

    public void setID_Departamento(String ID_Departamento) {
        this.ID_Departamento = ID_Departamento;
    }

    // METODOS

    /**
     *
     * @param miConexion
     */
    public static void mostrarAlumnos(Connection miConexion) throws SQLException {

        PreparedStatement prepStat =
                miConexion.prepareStatement("SELECT p.ID_Persona" +
                                             "FROM PERSONA p, Profesor pr, Asignatura a, Matriculacion m, Alumno al " +
                                             "WHERE ID_PERSONA = ");

        ResultSet resultado = prepStat.executeQuery();
    }
}
