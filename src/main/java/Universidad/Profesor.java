package Universidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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
     * @throws SQLException
     */
    public static void mostrarAlumnos(Connection miConexion, String[] datos) throws SQLException {

        System.out.println("Estos son los alumnos que tienes:");
        try{
            PreparedStatement prepStat = miConexion.prepareStatement("SELECT p.ID_Persona, p.Nombre, p.Edad, p.Telefono " +
                    "                                                    FROM Persona AS p " +
                    "                                                    INNER JOIN Matriculacion AS m ON m.ID_Persona = p.ID_Persona" +
                    "                                                    INNER JOIN Asignatura AS a ON a.ID_Asignatura = m.ID_Asignatura " +
                    "                                                    INNER JOIN Profesor AS pr ON pr.ID_Persona = a.ID_Profesor " +
                    "                                                    WHERE pr.ID_Persona = ? ");
            prepStat.setString(1, datos[0]);
            ResultSet resultado = prepStat.executeQuery();
            while(resultado.next()){
                String trampitas = resultado.getString("ID_Persona") + " " + resultado.getString("Nombre") +
                        " " + resultado.getString("Edad") + " " + resultado.getString("Telefono");
                System.out.println(trampitas.replace(" ", " - "));
            }
        }catch(SQLException e){
            System.out.println("No se ha podido realizar la consulta.");
            e.printStackTrace();
        }


    }

    public static void ponerNota(Connection con){

    }
}
