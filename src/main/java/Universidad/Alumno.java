package Universidad;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * El alumno puede gestionar sus matriculas
 * @author grupo3
 */
public class Alumno extends Persona {
    //Atributos
    static ArrayList<Integer> lista = new ArrayList();
    private static Scanner lector = new Scanner(System.in);
    static int auxiliarAnoAcademico;
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
        PreparedStatement preparedSt2 = null;
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
            preparedSt2 = con.prepareStatement("update asignatura set plazas_disponibles = plazas_disponibles+1 where id_asignatura = ?");
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
                if (preparedSt2 != null) {
                    preparedSt2.close();
                }
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

    /**
     * Se pedira al alumno si quiere filtrar de alguna manera, es decir, si quiere ver todas, solo las suspendias,
     * las aprobadas, solo las que tienen nota o de una asignatura a su eleccion. Luego debera pedir al alumno si
     * quiere guardar la informacion en un .txt.
     * Si la respuesta es que si, se debera imprimir la informacion ordenada en un archivo llamado notas.txt.
     * SE USARA EL PAIR PROGRAMMING.
     * Es estatico.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para consultar sus matriculas y dar de baja.
     */

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

    /**
     * Metodo para crear un archivo de txt que recoge los datos de los metodos para ver asignaturas.
     * @param datos string de datos con los datos de las matriculas del alumno.
     */
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

    /**
     * Muestra todas las asignaturas matriculadas del alumno.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para consultar sus matriculas y dar de baja.
     * @return datosTxt Es un string de datos con los datos de las matriculas del alumno.
     */
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
    /**
     * Muestra las asignaturas matriculadas del alumno que estan suspendidas.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para consultar sus matriculas y dar de baja.
     * @return datosTxt Es un string de datos con los datos de las matriculas del alumno.
     */
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
    /**
     * Muestra las asignaturas matriculadas del alumno que estan aprobadas.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para consultar sus matriculas y dar de baja.
     * @return datosTxt Es un string de datos con los datos de las matriculas del alumno.
     */
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
    /**
     * Muestra las asignaturas matriculadas del alumno que tienen una nota.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para consultar sus matriculas y dar de baja.
     * @return datosTxt Es un string de datos con los datos de las matriculas del alumno.
     */
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
    /**
     * Muestra la asignatura que el alumne indique.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para consultar sus matriculas y dar de baja.
     * @return datosTxt Es un string de datos con los datos de las matriculas del alumno.
     */
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




    /**
     * Metodo que imprime la matricula de un alumno en un archivo txt, par que quede
     * constancia de lo matriculado.
     *
     * @param conexionBase un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para realizar los inserts en la tabla
     *            matriculaciones y dar de alta.
     */
    private static void imprimirMatricula(Connection conexionBase, String id) {
        PreparedStatement SqlMatricula = null;
        ArrayList<String> lista_Nombres_matricula = new ArrayList();
        BufferedWriter miBuffer = null;
        try {
            conexionBase.setAutoCommit(false);
            SqlMatricula = conexionBase.prepareStatement(
                    "select nombre_asignatura from asignatura a, matriculacion m where a.ID_Asignatura = m.ID_Asignatura and m.ID_Persona=?");
            SqlMatricula.setString(1, id);
            ResultSet rs = SqlMatricula.executeQuery();
            while (rs.next()) {
                String nombreA = rs.getString("nombre_asignatura");
                System.out.println(nombreA);
                lista_Nombres_matricula.add(nombreA);
            }
            String resumen = "Alumno con id : " + id + " matriculado de las siguientes asignaturas: " + "\n";
            String finalMatricula = "\n" + "Correspondientes al ano : " + auxiliarAnoAcademico;
            miBuffer = new BufferedWriter(new FileWriter("Ficheros/matricula.txt"));
            miBuffer.write(resumen);
            for (int i = 0; i < lista_Nombres_matricula.size(); i++) {
                miBuffer.write(lista_Nombres_matricula.get(i));
                miBuffer.newLine();
            }
            miBuffer.write(finalMatricula);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (SqlMatricula != null) {
                    SqlMatricula.close();
                }
                if (miBuffer != null) {
                    miBuffer.close();
                }
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        }


    }

    /**Metodo que registra matriculas a traves del campo dni de alumno y los codigos obtenidos en el
     * metodo registrarAsignaturas.
     *
     * @param conexionBase un objeto Connection para hacer la busqueda en la BBDD.
     * @param id  el dni del alumno para realizar los inserts en la tabla
     *            matriculaciones y dar de alta.
     */
    public static void altaMatricula(Connection conexionBase, String id) {
        PreparedStatement Sql1 = null;
        PreparedStatement Sql2 = null;
        try {
            conexionBase.setAutoCommit(false);
            // comenzamos el preparedstatement para actualizar la transaccion en caso de que
            // todo vaya bien
            // por bloques se mostraran todos los procesos implicados en la matriculacion
            // primer bloque que ejecuta la matriculacion
            lista = registrarAsignaturas();
            System.out.println("De que ano academico quieres matricularte?");
            int ano_academico = Integer.parseInt(lector.nextLine());
            auxiliarAnoAcademico = ano_academico;
            int asignatura;
            for (int i = 0; i < lista.size(); i++) {
                Sql1 = conexionBase.prepareStatement("insert into matriculacion values (?,?,?,null)");
                String alumno = id;
                asignatura = lista.get(i);
                Sql1.setString(1, alumno);
                Sql1.setInt(2, asignatura);
                Sql1.setInt(3, ano_academico);
                Sql1.executeUpdate();
                // segundo bloque que resta una plaza a la asignatura
                Sql2 = conexionBase.prepareStatement(
                        "update asignatura set plazas_disponibles = (plazas_disponibles)-1 where id_asignatura =?");
                Sql2.setInt(1, asignatura);
                Sql2.executeUpdate();
            }
            imprimirMatricula(conexionBase, id);
            System.out.println("Se ha exportado esta informacion a un txt.");
            conexionBase.commit();
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println("Asignatura ya matriculada");
            try {
                conexionBase.rollback();
            } catch (SQLException e1) {
                // TODO Bloque catch generado automaticamente
                e1.printStackTrace();
            }
        } catch (SQLException e1) {
            System.out.println("Conexion fallida");

            try {
                conexionBase.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            e1.printStackTrace();
        } finally {
            try {
                if (Sql1 != null) {
                    Sql1.close();
                }
                if (Sql2 != null) {
                    Sql2.close();
                }
                conexionBase.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * metodo registrarAsginaturas, te pregunta la candtidad de asignaturas de las
     * cuales te quieres registrar y, seguidamente, pide los codigos de estas, las
     * almacena en un ArrayList. Devuelve el arraylist con los codigos de las
     * asignaturas
     *
     * @return listaAsignaturas, es un arraylist que contiene los codigos de las
     *         asignaturas para hacer un tratamieno posterior
     */
    private static ArrayList<Integer> registrarAsignaturas() {
        ArrayList<Integer> listaAsignaturas = new ArrayList();
        System.out.println("Introduce la cantidad de asignaturas de las cuales te vas a matricular: ");
        int opcion = Integer.parseInt(lector.nextLine());
        for (int i = 0; i < opcion; i++) {
            System.out.println("Introduce el codigo de la " + (i + 1) + " asignatura de la cual te quieres matricular: ");
            listaAsignaturas.add(Integer.parseInt(lector.nextLine()));
        }
        return listaAsignaturas;
    }



}
