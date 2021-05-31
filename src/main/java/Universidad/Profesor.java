package Universidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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

        System.out.println("Estos son los alumnos que tienes:");
        try{
            
            prepStat = miConexion.prepareStatement("SELECT p.ID_Persona, p.Nombre, p.Edad, p.Telefono " +
                    "                                   FROM Persona AS p " +
                    "                                   INNER JOIN Matriculacion AS m ON m.ID_Persona = p.ID_Persona" +
                    "                                   INNER JOIN Asignatura AS a ON a.ID_Asignatura = m.ID_Asignatura " +
                    "                                   INNER JOIN Profesor AS pr ON pr.ID_Persona = a.ID_Profesor " +
                    "                                   WHERE pr.ID_Persona = ? ");
            prepStat.setString(1, datos[0]);
            resultado = prepStat.executeQuery();
            while(resultado.next()){
                String trampitas = resultado.getString("ID_Persona") + " " + resultado.getString("Nombre") +
                        " " + resultado.getString("Edad") + " " + resultado.getString("Telefono");
                System.out.println(trampitas.replace(" ", " - "));
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

        PreparedStatement prepStat = null;

        mostrarAlumnos(miConexion, datos);
        System.out.println("Escribe el ID del Alumno al que quieres ponerle nota:");
        System.out.println("ID: ");
        String id = lector.nextLine();

        System.out.println("Que nota quieres ponerle?");
        Double nota = lector.nextDouble();
        lector.nextLine();

        try {
            prepStat = miConexion.prepareStatement("UPDATE matriculacion" +
                    "                                   SET Nota=?" +
                    "                                   WHERE ID_Persona=?");

            prepStat.setDouble(1, nota);
            prepStat.setString(2, id);

            int n = prepStat.executeUpdate();


        }catch(SQLException e){
            System.out.println("No se ha podido insertar la nota");
            e.printStackTrace();
        }finally {
            try {
                if (prepStat != null) {
                    prepStat.close();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

    }

    public static  void eliminarNotaAAlumno(Connection con, String[] datos){
        mostrarAlumnos(con, datos);
        Scanner lector = new Scanner(System.in);
        System.out.println("escribe el DNI del alumno cuya nota quieres eliminar: ");
        String alumno = lector.nextLine();

    }
    
}
