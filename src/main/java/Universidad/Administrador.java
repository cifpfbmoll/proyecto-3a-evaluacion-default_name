package Universidad;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase Administrador
 * @author grupo3
 */
public class Administrador extends Persona{

    // CONSTRUCTOR VACÍO, CONSTRUCTOR CON PARÁMETROS Y CONSTRUCTOR COPIA

    public Administrador() {
    }

    public Administrador(String ID_Persona, String nombre, int edad, String telefono, String contrasena, String rol) {
        super(ID_Persona, nombre, edad, telefono, contrasena, rol);
    }

    public Administrador(Administrador copiaAdministrador){
        super((Persona)copiaAdministrador);
    }

    /**
     * Imprime las personas de la tabla persona.
     * @param con es un objeto de tipo Connection, que es la conexión a la BBDD
     */
    public static void verPersonas(Connection con) {
        try{
            PreparedStatement consulta = con.prepareStatement("select * from persona inner join asignatura on persona.ID_persona = asignatura.ID_profesor INNER join titulacion on asignatura.iD_titulacion = titulacion.id_titulacion  ");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay usuarios.");
            } else {
                do {
                    int ID_Asignatura = resultados.getInt("asignatura.ID_Asignatura");
                    String nombre = resultados.getString("asignatura.Nombre_Asignatura");
                    int ID_Titulacion = resultados.getInt("asignatura.ID_Titulacion");
                    String ID_Profesor = resultados.getString("asignatura.ID_Profesor");
                    String curso = resultados.getString("asignatura.Curso");
                 
                    System.out.println("ID_Asignatura " +ID_Asignatura + " Nombre_Asignatura: "+ nombre +" ID_Titulacion: "
                            +ID_Titulacion + "ID_Profesor: " + ID_Profesor + "curso: " + curso) ;
                } while(resultados.next());
            }
            if (resultados != null) {resultados.close (); }//cierra
            if (consulta != null) consulta.close ();//cierra

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }
    }

    /**
     * Pide el rol y el resto de datos que le quieres dar a la nueva persona
     * @return devuelve una personas con los datos introducidos por el administrador
     */
    public static Persona pedirDatosPersona(){
        Scanner lector = new  Scanner(System.in);
        String tipoPersona;
        do{
            System.out.println("¿Qué tipo de persona quieres introducir?");
            System.out.println("1. Administrador");
            System.out.println("2. Profesor");
            System.out.println("3. Alumno");
            System.out.println("4. Bibliotecario");
            tipoPersona = lector.nextLine();
            System.out.println(tipoPersona);
        }while (!"1".equals(tipoPersona) && !"2".equals(tipoPersona)  && !"3".equals(tipoPersona)  && !"4".equals(tipoPersona) );

        String rol ="";
        switch (tipoPersona){
            case "1": rol = "administrador"; break;
            case "2": rol = "profesor"; break;
            case "3": rol = "alumno"; break;
            case "4": rol = "bibliotecario"; break;
        }

        System.out.println("Dame el DNI");
        String DNI = lector.nextLine();
        System.out.println("Dame la contrasena");
        String constrasena = lector.nextLine();
        System.out.println("Dame la nombre");
        String nombre = lector.nextLine();
        System.out.println("Dime el telefono");
        String telefono = lector.nextLine();
        System.out.println("Dime la edad");
        int edad = lector.nextInt();
        lector.nextLine();
        Persona persona = new Persona();
        persona.setID_Persona(DNI);
        persona.setContrasena(constrasena);
        persona.setNombre(nombre);
        persona.setTelefono(telefono);
        persona.setEdad(edad);
        persona.setRol(rol);

        return persona;
    }

    /**
     * Añade una persona a la BBDD
     * @param miConexion recibe un objeto conexión para conectar con la BBDD
     */
    public static void anadirPersona(Connection miConexion) {
        Persona p = Administrador.pedirDatosPersona();
        String datosPersona = "insert into persona values( ?, ?,?,?,?,?) ";

        PreparedStatement estatementpreparadaPersona = null;
        PreparedStatement estatementpreparadaRol = null;
        try {
            miConexion.setAutoCommit(false);
            estatementpreparadaPersona = miConexion.prepareStatement(datosPersona);
            estatementpreparadaPersona.setString(1, p.getID_Persona());
            estatementpreparadaPersona.setString(2, p.getNombre());
            estatementpreparadaPersona.setInt(3, p.getEdad());
            estatementpreparadaPersona.setString(4, p.getTelefono());
            estatementpreparadaPersona.setString(5, p.getContrasena());
            estatementpreparadaPersona.setString(6, p.getRol());
            int filasMetidas = estatementpreparadaPersona.executeUpdate();
            int id = -1;
            String datosRol = "";
            switch (p.getRol()){
                case "administrador": datosRol = "insert into administrador values( ?) "; break;
                case "profesor":
                    // lamo al metodo de buscar departamento que devuelve un departameno
                    id = Administrador.devolverIdDepartamento(miConexion);
                    datosRol = "insert into profesor values( ?,?) ";
                    break;
                case "alumno": datosRol = "insert into alumno values( ?) "; break;
                case "bibliotecario": datosRol = "insert into bibliotecario values( ?) "; break;
            }

            estatementpreparadaRol = miConexion.prepareStatement(datosRol);
            estatementpreparadaRol.setString(1, p.getID_Persona());

            if (p.getRol().equals("profesor")) {
                boolean dptValido = false;
                int idUsuario = 0;
                while(dptValido == false){
                    Administrador.verDepartamento(miConexion);
                    System.out.println("dime el id del departamento del profesor");
                    Scanner lector = new Scanner(System.in);
                    idUsuario = lector.nextInt();
                    lector.nextLine();
                    dptValido = Administrador.validarIdDepartamento(idUsuario , miConexion);
                }
                estatementpreparadaRol.setInt(2, idUsuario);
            }
            int filasMetidasROl = estatementpreparadaRol.executeUpdate();
            miConexion.commit();

            if (filasMetidas > 0 && filasMetidasROl > 0) {
                System.out.println("Se Ha añadido el registro");
            }

        } catch (MySQLIntegrityConstraintViolationException ex)  {
            System.out.println("Este dni ya está en la base de datos");
        }catch (SQLException throwables) {
           System.out.println("SQLSTATE " + throwables.getSQLState() + "SQLMESSAGE" +throwables.getMessage());
            System.out.println("Hago rollback");
            try {
                miConexion.rollback() ;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }finally {
            try {
                miConexion.setAutoCommit(true);
                if (estatementpreparadaPersona != null) {estatementpreparadaPersona.close (); }//cierra
                if (estatementpreparadaRol != null) {estatementpreparadaRol.close (); }//cierra;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Muestra todas las personas de la tabla personas con su informacion segun te indique el usuario.
     * Posibilidades: Por nombre, por edad y por rol. Es estatico.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     */
    public static void mostrarPersonasAtributo(Connection con) {
        try{
            Scanner lector = new Scanner(System.in);
            System.out.println("¿Por qué atributo quieres ordenar la lista (nombre, edad, rol)?");
            String atributo = lector.nextLine();
            while (!atributo.equals("nombre")&&!atributo.equals("edad")&&!atributo.equals("rol")){
                System.out.println("Introduce un atributo valido:");
                System.out.println("nombre, edad o rol");
                atributo=lector.nextLine();
            }
            PreparedStatement consulta = con.prepareStatement("select * from persona order by ?");
            consulta.setString(1, atributo);
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay usuarios.");
            } else {
                do {
                    String dni = resultados.getString("ID_Persona");
                    String nombre = resultados.getString("Nombre");
                    Integer edad = resultados.getInt("Edad");
                    String telefono = resultados.getString("Telefono");
                    String rol = resultados.getString("Rol");
                    String contrasena = resultados.getString("Contrasena");
                    System.out.println("DNI " +dni + " Nombre : "+nombre +" Edad : " +edad +" Telefono : "+telefono +" Contrasena : " +contrasena +" Rol: " +rol);
                } while(resultados.next());

            }
            if (resultados != null) {resultados.close (); }//cierra
            if (consulta != null) consulta.close ();//cierra

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
       }
    }

    /**
     * Busca por un DNI
     * @param dni un dni para buscar
     * @param con un obbjeto Connection para hacer la búsqueda en la BBDD
     * @return true si ha encontrado el DNI, false si no lo ha encontrado
     */
    public static boolean buscarDni( String dni, Connection con){
        boolean encontrado = false;
        try (PreparedStatement consulta = con.prepareStatement("select * from persona where ID_Persona = ?")) {
            consulta.setString(1, dni);
            ResultSet resultado = consulta.executeQuery();

            if (resultado.next() == false) {
                encontrado = false;
            }else{
                encontrado= true;
            }
            if (consulta != null) {consulta.close (); }//cierra
            if (resultado != null) {resultado.close (); }//cierra
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            return encontrado;
        }
    }

    /**
     * Borra una persona de la BBDD
     * @param con es un objeto Connection para contactar con la BBDD
     */
    public static void borrarPersona(Connection con){
        Administrador.verPersonas(con);
        boolean encontrado = false;
        String datosPersona = "delete from persona where ID_Persona = ?";
        String datosRol = "";
        PreparedStatement estatementpreparadaPersona = null;
        PreparedStatement  estatementpreparadaRol = null;
        ResultSet resultados = null;
        PreparedStatement consulta = null;
        try {
            con.setAutoCommit(false);
            String dni = null;
            while (!encontrado) {
                Scanner lector = new Scanner(System.in);
                System.out.println("Escribe el DNI de la persona a eliminar");
                dni = lector.nextLine();
                encontrado = buscarDni(dni, con);
                //System.out.println(encontrado);
            }

            estatementpreparadaPersona = con.prepareStatement(datosPersona);
            estatementpreparadaPersona.setString(1, dni);

            // busco el rol de la persona a eliminar
            consulta = con.prepareStatement("select * from persona where ID_Persona = ?");
            consulta.setString(1, dni);
            resultados = consulta.executeQuery();
            String rol = null;
            if (resultados.next() == false) {
                System.out.println("No hay usuarios.");
            } else {
                do {
                    rol = resultados.getString("Rol");
                } while(resultados.next());
            }

            switch (rol){
                case "administrador":  datosRol = "delete from administrador where ID_Persona = ?";break;
                case "alumno":  datosRol = "delete from alumno where ID_Persona = ?";break;
                case "profesor": datosRol = "delete from profesor where ID_Persona = ?";break;
                case "bibliotecario":  datosRol = "delete from bibliotecario where ID_Persona = ?";break;
            }
            estatementpreparadaRol= con.prepareStatement(datosRol);
            estatementpreparadaRol.setString(1, dni);

            int filasBorradasRol = estatementpreparadaRol.executeUpdate();
            int filasBorradas = estatementpreparadaPersona.executeUpdate();

            con.commit();
            if(filasBorradas > 0 || filasBorradasRol >0){
                System.out.println("Se ha eliminado el registro");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                System.out.println("Hago rollback, ha habido un fallo.");
                con.rollback() ;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        finally {

            try {
                con.setAutoCommit(true);
                if (estatementpreparadaRol != null) {estatementpreparadaRol.close (); }//cierra
                if (estatementpreparadaPersona != null) {estatementpreparadaPersona.close (); }//cierra
                if (resultados != null) {resultados.close (); }//cierra
                if (consulta != null) {consulta.close (); }//cierra
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

    /**
     * Lista todos los departamentos
     * @param con es el objeto conexion con la base de datos
     */
    public static void verDepartamento (Connection con){
        PreparedStatement consulta = null;
        ResultSet resultados = null;
        try{
            consulta = con.prepareStatement("select * from departamento");
            resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay usuarios.");
            } else {
                do {
                    String nombre = resultados.getString("Nombre_Departamento");
                    int id = resultados.getInt("ID_Departamento");
                    System.out.println("ID " +id  + " Nombre: "+nombre);
                } while(resultados.next());
            }

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }finally{
                    try {
                        if (consulta != null) consulta.close ();//cierra
                        if (resultados != null) resultados.close ();//cierra
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
        }
    }

    public static boolean buscarDepartamento( int id, Connection con){
        boolean encontrado = false;
        try (PreparedStatement consulta = con.prepareStatement("select * from departamento where ID_Departamento = ?")) {
            consulta.setInt(1, id);
            ResultSet resultado = consulta.executeQuery();

            if (resultado.next() == false) {
                encontrado = false;
            }else{
                encontrado= true;
            }
            if (consulta != null) {consulta.close (); }//cierra
            if (resultado != null) {resultado.close (); }//cierra
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            return encontrado;
        }
    }

    public static int devolverIdDepartamento(Connection con){
        boolean encontrado = false;
        int id = -1;
        while (!encontrado) {
            Administrador.verDepartamento(con);
            Scanner lector = new Scanner(System.in);
            System.out.println("Escribe el id del departamento");
            id = lector.nextInt();
            lector.nextLine();
            encontrado = buscarDepartamento(id, con);
            //System.out.println(encontrado);
        }
        return id;
    }


    /**
     * Añade un departamento a la tabla departamento
     * @param miConexion es el objeto conexion para conectar con la BBDD
     */
    public static void anadirDepartamento(Connection miConexion){
        Scanner lector = new Scanner(System.in);
        System.out.println("Escribe el nombre de departamento que quieres introducir. ");
        String nombre = lector.nextLine();

        String datosDpt = "insert into departamento (Nombre_Departamento) values( ?) ";
        PreparedStatement estatementpreparada = null;
        try {
            estatementpreparada = miConexion.prepareStatement(datosDpt);
            estatementpreparada.setString(1, nombre);
            int filasMetidas = estatementpreparada.executeUpdate();
            if (filasMetidas > 0) {
                System.out.println("Se Ha añadido el departamento");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Comprueba, dada una id de un departamento, si éste existe
     * @param id la id del departamento que queremos comprobar si existe
     * @param con es el objeto conexión
     * @return devuelve true si el id del dpto existe, y false si no existe
     */
    public static boolean validarIdDepartamento(int id, Connection con){
        boolean valido = false;
        try{
            PreparedStatement consulta = con.prepareStatement("select * from departamento where ID_Departamento = ? ");
            consulta.setInt(1, id);
            ResultSet resultados = consulta.executeQuery();
            if(resultados.next()){
                valido = true;
            }

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }
        return valido;
    }

    /**
     * Elimina un departamento de la base de datos
     * @param con recibe el objeto conexion
     */
    public static void eliminarDepartamento(Connection con){
        boolean valido = false;
        int idUser = 0;
        while(valido == false){
            Administrador.verDepartamento(con);
            System.out.println("Escribe el ID del departamento para eliminar");
            Scanner lector = new Scanner(System.in);
            idUser = lector.nextInt();
            lector.nextLine();
            valido = Administrador.validarIdDepartamento(idUser, con);
        }

        String datos = "delete from departamento where ID_Departamento= ?  ";
        PreparedStatement estatementpreparada = null;
        try {
            estatementpreparada = con.prepareStatement(datos);
            estatementpreparada.setInt(1, idUser);
            int filasBorradas = estatementpreparada.executeUpdate();
            if(filasBorradas > 0  ){
                System.out.println("Se ha eliminado el departamento");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{
            try {
                if (estatementpreparada != null) {estatementpreparada.close (); }//cierra
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }
    public static void verAsignaturas(Connection miConexion){
        try{
            PreparedStatement consulta = miConexion.prepareStatement("select * from persona inner join asignatura on persona.ID_persona = asignatura.ID_profesor INNER join titulacion on asignatura.iD_titulacion = titulacion.id_titulacion  ");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay asignaturas");
            } else {
                System.out.println("------------LISTADO DE ASIGNATURAS------------");
                do {
                    int ID_Asignatura = resultados.getInt("asignatura.ID_Asignatura");
                    String nombre = resultados.getString("asignatura.Nombre_Asignatura");
                    String nombreprofe = resultados.getString("persona.Nombre");
                    int ID_Titulacion = resultados.getInt("asignatura.ID_Titulacion");
                    String nombretil = resultados.getString("titulacion.Nombre_titulacion");
                    String ID_Profesor = resultados.getString("asignatura.ID_Profesor");
                    String curso = resultados.getString("asignatura.Curso");

                    System.out.println("ID_Asignatura " +ID_Asignatura + " Nombre_Asignatura: "+ nombre ) ;
                    System.out.println("ID_Titulacion: " +ID_Titulacion + " Nombre titulacion: " + nombretil);
                    System.out.println("ID_Profesor: " + ID_Profesor +" Nombre profesor: " + nombreprofe);
                    System.out.println( "Curso: " + curso);
                    System.out.println("----------------------------------------------------------------------");
                } while(resultados.next());
            }
            if (resultados != null) {resultados.close (); }//cierra
            if (consulta != null) consulta.close ();//cierra

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }

    }
}

