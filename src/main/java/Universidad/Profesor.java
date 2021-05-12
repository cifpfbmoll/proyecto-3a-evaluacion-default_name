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
     * Metodo estatico que busca los alumnos del profesor activo
     * @param miConexion
     * @param datos
     * @throws SQLException
     */
    public static void mostrarAlumnos(Connection miConexion, String[] datos) throws SQLException {
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
            if(prepStat != null){
                prepStat.close();
            }
            if (resultado != null){
                resultado.close();
            }
        }
    }

    /**
     * Metodo estatico que se utilizara para poner notas a los alumnos
     * @param miConexion
     * @param datos
     * @throws SQLException
     */
    public static void ponerNota(Connection miConexion, String[] datos) throws SQLException{
        Scanner lector = new Scanner(System.in);
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
            if(prepStat != null) {
                prepStat.close();
            }
        }
    }
    
}
