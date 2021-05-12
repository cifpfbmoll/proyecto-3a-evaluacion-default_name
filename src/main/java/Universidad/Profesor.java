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
     * Metodo estatico que busca los alumnos del profesor activo
     * @param miConexion
     * @param datos
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

    /**
     * Metodo estatico que se utilizara para poner notas a los alumnos
     * @param miConexion
     * @param datos
     * @throws SQLException
     */
    public static void ponerNota(Connection miConexion, String[] datos) throws SQLException{
        Scanner lector = new Scanner(System.in);

        mostrarAlumnos(miConexion, datos);
        System.out.println("Escribe el ID del Alumno al que quieres ponerle nota:");
        System.out.println("ID: ");
        String id = lector.nextLine();

        System.out.println("Que nota quieres ponerle?");
        Double nota = lector.nextDouble();
        lector.nextLine();

        try {
            PreparedStatement prepStat = miConexion.prepareStatement("UPDATE matriculacion" +
                    "                                                     SET Nota=?" +
                    "                                                     WHERE ID_Persona=?");

            prepStat.setDouble(1, nota);
            prepStat.setString(2, id);

            int n = prepStat.executeUpdate();

        }catch(SQLException e){
            System.out.println("No se ha podido insertar la nota");
            e.printStackTrace();
        }

    }

    /**
     * Metodo estatico que mostrara los alumnos de una asignatura
     * @param miConexion
     * @param datos
     * @throws SQLException
     */
    public static void mostrarAlumnosPorAsignatura(Connection miConexion, String[] datos) throws SQLException{
        Scanner lectura = new Scanner (System.in);

        mostrarAlumnosPorAsignatura(miConexion, datos);
        System.out.println("Escribe el ID de la asignatura que quieres ver los alumnos: ");
        String asign = lectura.nextLine();

        System.out.println("Estos son los alumnos de la asignatura: ");
        try{

            PreparedStatement prepStat = miConexion.prepareStatement("SELECT p.ID_Persona, p.Nombre, p.Edad, p.Telefono " +                                                     FROM Persona AS p " +
                    "                                                     INNER JOIN Matriculacion AS m ON m.ID_Persona = p.ID_Persona " +
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
