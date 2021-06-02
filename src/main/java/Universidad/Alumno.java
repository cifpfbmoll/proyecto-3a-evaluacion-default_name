package Universidad;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Alumno extends Persona {
    //Atributos
    private static Scanner lector = new Scanner(System.in);

    public Alumno() {
    }

    public Alumno(String ID_Persona, String nombre, int edad, String telefono, String contrasena, String rol) {
        super(ID_Persona, nombre, edad, telefono, contrasena, rol);
    }

    public Alumno(Alumno copiaAlumno) {
        super((Persona) copiaAlumno);
    }

    /**
     * Es una transaccion. Muestra las asignaturas matriculadas de este alumno y pide cual quiere dar de baja.
     * No devuelve nada y es estatica.
     *
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para consultar sus matriculas y dar de baja.
     */
    public static void bajaMatricula(Connection con, String id) {
        int filas = 0;
        PreparedStatement preparedSt = null;
        int idAsignatura = 0;
        try {
            con.setAutoCommit(false);
            while (filas == 0) {
                verMatriculaciones(con, id);
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
            if (filas > 0 && filasSt2 > 0) {
                System.out.println("Se ha dado de baja de la asignatura.");
            }

        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println("Error de constraint.");
            try {
                con.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException error) {
            System.out.println("Error en el update.");
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                con.setAutoCommit(true);
                if (preparedSt != null) {
                    preparedSt.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Metodo que muestra todas las matriculaciones de un alumno.
     *
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para consultar sus matriculas y dar de baja.
     */

    public static void verMatriculaciones(Connection con, String id) {
        try {
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
                    System.out.println("ID: " + id_persona + " Id asignatura: " + id_asignatura + " Ano academico: " + ano_academico + " Nota: " + nota);
                } while (resultados.next());

            }
            if (resultados != null) {
                resultados.close();
            }
            if (consulta != null) consulta.close();

        } catch (SQLException error) {
            System.out.println("Error en la consulta.");

        }


    }

    public static void verEstadoAsignaturas(Connection con, String id) {
        int opcion;
        String datos = "";
        boolean bool = false;
        while (bool);
        System.out.println("Como quieres filtrar las asignaturas: ");
        System.out.println("1. Todas");
        System.out.println("2. Supendidas");
        System.out.println("3. Aprobadas");
        System.out.println("4. Solo con nota");
        System.out.println("5. Una");
        System.out.print("> ");
        opcion = Integer.parseInt(lector.nextLine());
        switch(opcion) {
            case 1:
                datos = verEstadoAsignaturasTodas(con, id);
                bool = true;
            break;
            case 2:
                datos = verEstadoAsignaturasSuspendidas(con, id);
                bool = true;
                break;
            case 3:
                datos = verEstadoAsignaturasAprobadas(con, id);
                bool = true;
                break;
            case 4:
                datos = verEstadoAsignaturasNotNull(con, id);
                bool = true;
                break;
            case 5:
                datos = verEstadoAsignaturasUnica(con, id);
                bool = true;
                break;
            default:
                System.out.println("Esa opcion no existe, elije otra.");
                break;

        }
        System.out.println("Quieres guardar la consulta en un archivo? (si/no)");
        if (lector.nextLine().equalsIgnoreCase("si")){
            escribirEstadoAsignatuas(datos);
            System.out.println("Se ha escrito en el archvo 'ESTADOASIGNATURAS.txt'");
        }



    }

    private static void escribirEstadoAsignatuas(String datos){
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = new BufferedWriter(new FileWriter("Ficheros/ESTADOASIGNATURAS.txt"));
            bufferedWriter.write("------ ESTADO DE LAS ASIGNATURAS ------");
            bufferedWriter.newLine();
            bufferedWriter.write(datos);
            bufferedWriter.newLine();


        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(bufferedWriter != null) {bufferedWriter.close();}

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static String verEstadoAsignaturasTodas(Connection con, String id) {
        String datosAux = "";
        String datosTxt = "";
        PreparedStatement consulta = null;
        ResultSet resultados = null;
        try{
            consulta = con.prepareStatement("select * from matriculacion where ID_Persona = ?");
            consulta.setString(1, id);
            resultados = consulta.executeQuery();

            while(resultados.next()){
                datosAux = "ID ASIGNATURA: " + resultados.getInt("ID_Asignatura") + "\n" +
                        "ANO ACADEMICO: " + resultados.getInt("Ano_academico") + "\n" +
                        "NOTA: " + resultados.getDouble("Nota") + "\n\n";
                datosTxt += datosAux;
            }
            System.out.println(datosTxt);

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
            System.out.println(error.getMessage());
            error.printStackTrace();
        }
        finally{
            try {
                if (consulta != null) consulta.close ();//cierra
                if (resultados != null) resultados.close ();//cierra
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return datosTxt;
    }

    private static String verEstadoAsignaturasSuspendidas(Connection con, String id) {
        String datosAux = "";
        String datosTxt = "";
        PreparedStatement consulta = null;
        ResultSet resultados = null;
        try{
            consulta = con.prepareStatement("select * from matriculacion where ID_Persona = ? and nota < 5");
            consulta.setString(1, id);
            resultados = consulta.executeQuery();

            while(resultados.next()){
                datosAux = "ID ASIGNATURA: " + resultados.getInt("ID_Asignatura") + "\n" +
                        "ANO ACADEMICO: " + resultados.getInt("Ano_academico") + "\n" +
                        "NOTA: " + resultados.getDouble("Nota") + "\n\n";
                datosTxt += datosAux;
            }
            System.out.println(datosTxt);

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }
        finally{
            try {
                if (consulta != null) consulta.close ();//cierra
                if (resultados != null) resultados.close ();//cierra
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return datosTxt;
    }
    private static String verEstadoAsignaturasAprobadas(Connection con, String id) {
        String datosAux = "";
        String datosTxt = "";
        PreparedStatement consulta = null;
        ResultSet resultados = null;
        try{
            consulta = con.prepareStatement("select * from matriculacion where ID_Persona = ? and nota > 5"
            );
            consulta.setString(1, id);
            resultados = consulta.executeQuery();

            while(resultados.next()){
                datosAux = "ID ASIGNATURA: " + resultados.getInt("ID_Asignatura") + "\n" +
                        "ANO ACADEMICO: " + resultados.getInt("Ano_academico") + "\n" +
                        "NOTA: " + resultados.getDouble("Nota") + "\n\n";
                datosTxt += datosAux;
            }
            System.out.println(datosTxt);

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }
        finally{
            try {
                if (consulta != null) consulta.close ();//cierra
                if (resultados != null) resultados.close ();//cierra
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return datosTxt;
    }
    private static String verEstadoAsignaturasNotNull(Connection con, String id) {
        String datosAux = "";
        String datosTxt = "";
        PreparedStatement consulta = null;
        ResultSet resultados = null;
        try{
            consulta = con.prepareStatement("select * from matriculacion where ID_Persona = ? and nota IS NOT NULL");
            consulta.setString(1, id);
            resultados = consulta.executeQuery();

            while(resultados.next()){
                datosAux = "ID ASIGNATURA: " + resultados.getInt("ID_Asignatura") + "\n" +
                        "ANO ACADEMICO: " + resultados.getInt("Ano_academico") + "\n" +
                        "NOTA: " + resultados.getDouble("Nota") + "\n\n";
                datosTxt += datosAux;
            }
            System.out.println(datosTxt);

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }
        finally{
            try {
                if (consulta != null) consulta.close ();//cierra
                if (resultados != null) resultados.close ();//cierra
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return datosTxt;
    }
    private static String verEstadoAsignaturasUnica(Connection con, String id) {
        String datosAux = "";
        String datosTxt = "";
        PreparedStatement consulta = null;
        ResultSet resultados = null;
        try{
            System.out.println("Que asignatura desea consultar?");
            int idAsignatura = lector.nextInt();
            lector.nextLine();
            consulta = con.prepareStatement("select * from matriculacion where ID_Persona = ? and ID_asignatura = ?");
            consulta.setString(1, id);
            consulta.setInt(2, idAsignatura);
            resultados = consulta.executeQuery();

            while(resultados.next()){
                datosAux = "ID ASIGNATURA: " + resultados.getInt("ID_Asignatura") + "\n" +
                        "ANO ACADEMICO: " + resultados.getInt("Ano_academico") + "\n" +
                        "NOTA: " + resultados.getDouble("Nota") + "\n\n";
                datosTxt += datosAux;
            }
            System.out.println(datosTxt);

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }
        finally{
            try {
                if (consulta != null) consulta.close ();//cierra
                if (resultados != null) resultados.close ();//cierra
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return datosTxt;
    }

}
