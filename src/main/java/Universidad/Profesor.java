package Universidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Profesor extends Persona{


    // ATRIBUTOS
    private String ID_Departamento;

    // CONSTRUCTOR VACIO, CONSTRUCTOR CON PARAMETROS, CONSTRUCTOR COPIA

    public Profesor() {
    }

    public Profesor(String ID_Persona, String nombre, int edad, String telefono, String contrasena, String rol) {
        super(ID_Persona, nombre, edad, telefono, contrasena, rol);
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
     * Metodo que muestra las asignaturas
     * @param miConexion
     * @param datos
     * @throws SQLException
     */

    public static void verAsignaturas(Connection miConexion, String[] datos) throws SQLException {

        System.out.println("Ahora se mostrarán las asignaturas actuales");

        try{

            PreparedStatement prepStat = miConexion.prepareStatement("SELECT a.ID_Asignatura, a.Nombre_Asignatura " +
                    "                                                    From Asignatura AS a");

            ResultSet resol = prepStat.executeQuery();
            while(resol.next()){
                String cosas = resol.getString("ID_Asignatura") + " " + resol.getString("Nombre_Asignatura");
                System.out.println(cosas.replace(" ", " - "));
            }
        }catch(SQLException e){
            System.out.println("No se ha podido realizar la consulta.");
            e.printStackTrace();
        }
    }

    /**
     * Metodo estatico que mostrara los alumnos matriculados de una asignatura
     * @param miConexion
     * @param datos
     * @throws SQLException
     */
    public static void mostrarAlumnosPorAsignatura(Connection miConexion, String[] datos) throws SQLException{
        Scanner lectura = new Scanner (System.in);

        verAsignaturas(miConexion, datos);
        System.out.println("Escribe el ID de la asignatura de la cual quieres ver los alumnos: ");
        String asign = lectura.nextLine();

        System.out.println("Estos son los alumnos de la asignatura: ");
        try{

            PreparedStatement prepStat = miConexion.prepareStatement("SELECT p.ID_Persona, p.Nombre, p.Edad, p.Telefono " +
                    "                                                     FROM Persona AS p " +
                    "                                                     INNER JOIN Matriculacion AS m ON m.ID_Alumno = p.ID_Persona " +
                    "                                                     INNER JOIN Asignatura AS a ON a.ID_Asignatura = m.ID_Asignatura " +
                    "                                                     INNER JOIN Alumno AS al ON al.ID_Persona = m.ID_Alumno " +
                    "                                                     WHERE a.ID_Asignatura = ? ");

            prepStat.setString(1, asign);
            ResultSet resul = prepStat.executeQuery();
            while(resul.next()){
                String cositas = resul.getString("ID_Persona") + " " + resul.getString("Nombre") + " " + resul.getString("Edad") + " " + resul.getString("Telefono");
                System.out.println(cositas.replace(" ", " - "));
            }
        }catch(SQLException e){
            System.out.println("No se ha podido realizar la consulta.");
            e.printStackTrace();
        }
    }
}
