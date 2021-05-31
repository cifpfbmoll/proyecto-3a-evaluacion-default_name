package Universidad;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import java.util.Scanner;

public class Alumno extends Persona{
    //Atributos
    private static Scanner lector = new Scanner(System.in);

    public Alumno () {
    }

    public Alumno(String ID_Persona, String nombre, int edad, String telefono, String contrasena, String rol) {
        super(ID_Persona, nombre, edad, telefono, contrasena, rol);
    }
    public Alumno(Alumno copiaAlumno) {
        super((Persona)copiaAlumno);
        }

    /**
     * Es una transaccion. Muestra las asignaturas matriculadas de este alumno y pide cual quiere dar de baja.
     * No devuelve nada y es estatica.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id el dni del alumno para consultar sus matriculas y dar de baja.
     */
    public static void bajaMatricula(Connection con, String id){
        int filas = 0;
        PreparedStatement preparedSt = null;
        int idAsignatura = 0;
        try {
            con.setAutoCommit(false);
            while (filas == 0) {
                verMatriculaciones(con, id);
                Scanner lector = new Scanner(System.in);
                System.out.println("Que matricula (indique id de la asignatura) desea eliminar?");
                idAsignatura = lector.nextInt();
                lector.nextLine();
                String st = "delete from matriculacion where ID_Asignatura= ? and ID_Persona = ?  ";
                preparedSt = con.prepareStatement(st);
                preparedSt.setInt(1, idAsignatura);
                preparedSt.setString(2, id);
                filas = preparedSt.executeUpdate();

                if (filas < 1) {
                    System.out.println("Error try again.");
                }
            }
            System.out.println(idAsignatura);
            PreparedStatement preparedSt2 = con.prepareStatement("update asignatura set plazas_disponibles = (plazas_disponibles)+1 where id_asignatura = ?");
            preparedSt2.setInt(1, idAsignatura);
            int filasSt2 = preparedSt2.executeUpdate();

            con.commit();
            if (filas > 0 && filasSt2>0) {
                System.out.println("Se ha dado de baja de la asignatura.");
            }

        }catch(MySQLIntegrityConstraintViolationException e){
            System.out.println("Error de constraint.");
            try{
                con.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch(SQLException error){
            System.out.println("Error en el update.");
            try {
                con.rollback() ;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                con.setAutoCommit(true);
                if (preparedSt != null) { preparedSt.close ();}
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Metodo que muestra todas las matriculaciones de un alumno.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id el dni del alumno para consultar sus matriculas y dar de baja.
     */

    public static void verMatriculaciones(Connection con, String id) {
        try{
            PreparedStatement consulta = con.prepareStatement("select * from matriculacion where ID_Persona = ?");
            consulta.setString(1, id);
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay matriculaciones.");
            } else {
                do {
                    String id_persona = resultados.getString("ID_Persona");
                    Integer id_asignatura = resultados.getInt("ID_Asignatura");
                    Integer ano_academico = resultados.getInt("Ano_academico");
                    Double nota = resultados.getDouble("Nota");
                    System.out.println("ID: "+id_persona+" Id asignatura: "+id_asignatura+" Ano academico: "+ano_academico+" Nota: "+nota);
                } while(resultados.next());

            }
            if (resultados != null) {resultados.close (); }
            if (consulta != null) consulta.close ();

        }catch(SQLException error){
            System.out.println("Error en la consulta.");

        }


    }

}