package Universidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
/**
 * El profesor puede ver sus alumnos y calificarlos
 * @author grupo3
 */
public class Profesor extends Persona{


    // ATRIBUTOS
    private String ID_Departamento;
    private static Scanner lector = new Scanner(System.in);


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
     * Metodo estatico que busca los alumnos del profesor activo
     * @param miConexion
     * @param datos
     */
    public static void mostrarAlumnos(Connection miConexion, String[] datos) {
        PreparedStatement prepStat = null;
        ResultSet resultado = null;

        try{

            prepStat = miConexion.prepareStatement("SELECT * " +
                    "                                   FROM Persona AS p " +
                    "                                   INNER JOIN Matriculacion AS m ON m.ID_Persona = p.ID_Persona" +
                    "                                   INNER JOIN Asignatura AS a ON a.ID_Asignatura = m.ID_Asignatura " +
                    "                                   INNER JOIN Profesor AS pr ON pr.ID_Persona = a.ID_Profesor " +
                    "                                   WHERE pr.ID_Persona = ? ");
            prepStat.setString(1, datos[0]);
            resultado = prepStat.executeQuery();
            System.out.println("Listado de alumnos matriculados en tus asignaturas");
            while(resultado.next()){
                System.out.println("ID Alumno: " + resultado.getString("ID_persona") + " --Nombre alumno: " + resultado.getString("nombre"));
                System.out.println(" Asignatura: " + resultado.getString("nombre_Asignatura") + "--ID asignatura: " + resultado.getString("ID_asignatura")) ;
                System.out.println(" Ano academico: " + resultado.getInt("ano_academico"));
                System.out.println("Nota actual: " + resultado.getDouble("nota"));
                System.out.println("----------------------------------------------------------------------------------");
            }

        }catch(SQLException e){
            System.out.println("No se ha podido realizar la consulta.");
            e.printStackTrace();
        }finally{
            try {
                if (prepStat != null) {
                    prepStat.close();
                }
                if (resultado != null) {
                    resultado.close();
                }

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Metodo estatico que se utilizara para poner notas a los alumnos
     * @param miConexion
     * @param datos
     */
    public static void ponerNota(Connection miConexion, String[] datos){
        mostrarAlumnos(miConexion, datos);
        Scanner lector = new Scanner(System.in);
        System.out.println("Escribe el DNI del alumno que quieres calificar: ");
        String alumno = lector.nextLine();
        System.out.println("Escribe el id de la asignatura");
        int asignatura = lector.nextInt();
        lector.nextLine();
        System.out.println("Escribe el año de la asignatura");
        int ano = lector.nextInt();
        lector.nextLine();
        System.out.println("Escribe la nota");
        double nota = lector.nextDouble();
        lector.nextLine();
        PreparedStatement prepStat = null;
        try{
            prepStat = miConexion.prepareStatement("update " +
                    "                                   Persona AS p " +
                    "                                   INNER JOIN Matriculacion AS m ON m.ID_Persona = p.ID_Persona" +
                    "                                   INNER JOIN Asignatura AS a ON a.ID_Asignatura = m.ID_Asignatura " +
                    "                                   INNER JOIN Profesor AS pr ON pr.ID_Persona = a.ID_Profesor " +
                    "                                   set m.nota  = ?" +
                    "                                   WHERE pr.ID_Persona = ?  and m.id_persona = ? and " +
                    "m.ano_academico = ? and m.id_asignatura = ?" );
            prepStat.setDouble(1, nota);
            prepStat.setString(2, datos[0]);
            prepStat.setString(3, alumno);
            prepStat.setInt(4, ano);
            prepStat.setInt(5, asignatura);
            int n = prepStat.executeUpdate();
            System.out.println("¡Nota guardada!");
        }catch(SQLException e){
            System.out.println("No se ha podido realizar la accion");
            e.printStackTrace();
        }finally{
            try {
                if (prepStat != null) {
                    prepStat.close();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Pone una nota de un alumno a null
     * @param con objeto conexión para conectar con la BBDD
     * @param datos asd
     */
    public static  void eliminarNotaAAlumno(Connection con, String[] datos){
        mostrarAlumnos(con, datos);
        Scanner lector = new Scanner(System.in);
        System.out.println("Escribe el DNI del alumno cuya nota quieres eliminar: ");
        String alumno = lector.nextLine();
        System.out.println("Escribe el id de la asignatura que quieres borrar la nota");
        int asignatura = lector.nextInt();
        lector.nextLine();
        System.out.println("Escribe el año de la asignatura");
        int ano = lector.nextInt();
        lector.nextLine();

        PreparedStatement prepStat = null;
        ResultSet resultado = null;

        try{
            prepStat = con.prepareStatement("update " +
                    "                                   Persona AS p " +
                    "                                   INNER JOIN Matriculacion AS m ON m.ID_Persona = p.ID_Persona" +
                    "                                   INNER JOIN Asignatura AS a ON a.ID_Asignatura = m.ID_Asignatura " +
                    "                                   INNER JOIN Profesor AS pr ON pr.ID_Persona = a.ID_Profesor " +
                    "                                   set m.nota  = NULL" +
                    "                                   WHERE pr.ID_Persona = ?  and m.id_persona = ? and " +
                    "m.ano_academico = ? and m.id_asignatura = ?" );
            prepStat.setString(1, datos[0]);
            prepStat.setString(2, alumno);
            prepStat.setInt(3, ano);
            prepStat.setInt(4, asignatura);
            int n = prepStat.executeUpdate();
            System.out.println("Nota eliminada");
        }catch(SQLException e){
            System.out.println("No se ha podido realizar la consulta.");
            e.printStackTrace();
        }finally{
            try {
                if (prepStat != null) {
                    prepStat.close();
                }

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

    }
    
}
