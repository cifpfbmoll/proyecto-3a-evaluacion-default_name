package Universidad;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.net.ConnectException;
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
    //Atributos
    private static Scanner lector = new Scanner(System.in);

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
            PreparedStatement consulta = con.prepareStatement("select * from persona ");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay usuarios.");
            } else {
                System.out.println("------------LISTADO DE PERSONAS----------");
                do {
                    int DNI = resultados.getInt("id_persona");
                    String nombre = resultados.getString("nombre");
                    int edad = resultados.getInt("edad");
                    String telefono = resultados.getString("telefono");
                    String contrasena = resultados.getString("contrasena");
                    String rol = resultados.getString("rol");
                 
                    System.out.println("DNI " +DNI + " Nombre_persona: "+ nombre +" Edad: "
                            +edad + " Telefono: " + telefono + " Contrasena: " + contrasena + " Rol: " + rol) ;
                    System.out.println("------------------------------------------------------");
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

            System.out.println("¿Por qué atributo quieres ordenar la lista (nombre, edad, rol)?");
            String atributo = lector.nextLine();
            while (!atributo.equals("nombre")&&!atributo.equals("edad")&&!atributo.equals("rol")){
                System.out.println("Introduce un atributo valido:");
                System.out.println("nombre, edad o rol");
                atributo=lector.nextLine();
            }
            String st="select * from persona order by %s";
            String st2= String.format(st, atributo);
            PreparedStatement consulta = con.prepareStatement(st2);
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
     * @param con un objeto Connection para hacer la búsqueda en la BBDD
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
     * Muestra todas las titulaciones. Es estatico.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     */
    public static void verTitulaciones(Connection con) {
        try{
            PreparedStatement consulta = con.prepareStatement("select * from titulacion");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay titulaciones.");
            } else {
                do {
                    Integer id_titulacion = resultados.getInt("ID_Titulacion");
                    String nombre_titulacion = resultados.getString("Nombre_titulacion");
                    System.out.println("ID: "+id_titulacion+" Nombre: "+nombre_titulacion);
                } while(resultados.next());

            }
            if (resultados != null) {resultados.close (); }
            if (consulta != null) consulta.close ();

        }catch(SQLException error){
            System.out.println("Error en la consulta.");

        }


    }

    /**
     *Preguntar al administrador que titulaciones quiere crear, preguntarle por todos los atributos de la titulacioen
     * y una vez se tienen los valores del atributo, añadirlos a la tabla de titulaciones. Es estatico.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     */
    //queda pendiente kambiar la PK de la tabla para que el catch error vea si existe el valor en la tabla
    public static void anadirTitulacion(Connection con){
        try {

            System.out.println("¿Qué titulacion desea anadir?");
            String titulacion = lector.nextLine();
            String st = "insert into titulacion(nombre_titulacion) values(?)";
            PreparedStatement preparedSt = con.prepareStatement(st);
            preparedSt.setString(1, titulacion);
            int n = preparedSt.executeUpdate();

            if(n>0){
                System.out.println("Se ha anadido la titulacion");
            }

            if (preparedSt != null) {preparedSt.close (); }//cierra

        } catch(MySQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            System.out.println("Ya existe en la bbdd.");
        } catch(SQLException error){
            error.printStackTrace();
            System.out.println("Error en el update.");
        }

    }
    /**
     * Se muestran las titulaciones que hay y pide cual se quiere eliminar. Es estatico.
     * @param con un objeto Connection para hacer la busqueda en la BBDD.
     */
    public static void eliminarTitulacion(Connection con){
        Administrador.verTitulaciones(con);
        int filas = 0;
        PreparedStatement preparedSt = null;
        try {
            while (filas == 0) {

                System.out.println("Que titulacion desea eliminar?");
                int idTitulacion = lector.nextInt();
                lector.nextLine();
                String st = "delete from titulacion where ID_Titulacion= ?  ";
                preparedSt = con.prepareStatement(st);
                preparedSt.setInt(1, idTitulacion);
                filas = preparedSt.executeUpdate();

                if (filas > 0) {
                    System.out.println("Se ha eliminado la titulacion "+idTitulacion);
                }
                else{
                    System.out.println("id no valido, try again \n");
                }
            }

        }catch(SQLException error){
            error.printStackTrace();
            System.out.println("Error en el update.");
        }
        finally {
            if (preparedSt != null) {
                try {
                    preparedSt.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
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

    /**
     * Comprueba si existe un departamento dada uno id
     * @param id la id del departamento a buscar
     * @param con objeto Connection para conectar con la base de datos
     * @return true si ha encontrado el departamento y false si no lo ha encontrado
     */
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

    /**
     * Pide la id de un departamento y comprueba que existe
     * @param con objeto Connection para conectar con la base de datos
     * @return la id de un departamento que existe
     */
    public static int devolverIdDepartamento(Connection con){
        boolean encontrado = false;
        int id = -1;
        while (!encontrado) {
            Administrador.verDepartamento(con);

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

    /**
     * Creo un listado de asignaturas
     * @param miConexion objeto conexion para conectar con la bbdd
     */
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

    /**
     * Dado un id de asignatura, comprueba que éste exista o no
     * @param miConexion objeto Connection para conectar con la base de datos
     * @param idAsignatura el id de la asignatura a buscar
     * @return true si el id existe en la tabla asignaturas, false si no existe
     */
    public static  boolean comprobarIdAsignatura(Connection miConexion, int idAsignatura){
        boolean encontrado = false;
        PreparedStatement consulta =null;
        ResultSet resultado = null;
        try  {
            consulta = miConexion.prepareStatement("select * from asignatura where ID_asignatura = ?");
            consulta.setInt(1, idAsignatura);
            resultado = consulta.executeQuery();

            if (resultado.next() == false) {
                encontrado = false;
            }else{
                encontrado= true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
                try {
                    if (resultado != null) {resultado.close (); }//cierra
                    if (consulta != null) {consulta.close (); }//cierra

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            return encontrado;
        }

    /**
     * Borra una asignatura de la BBDD
     * @param miConexion objeto conexión para conectar con la BBDD
     */
    public  static void borrarAsignatura(Connection  miConexion){
         boolean idCorrecta = false;
            int idUsuario= -1;
            while (idCorrecta == false){

             System.out.println("Escribe el ID de la asignatura para borrar: ");
             Administrador.verAsignaturas(miConexion);
             idUsuario = lector.nextInt();
             lector.nextLine();
             idCorrecta = comprobarIdAsignatura(miConexion, idUsuario);
         }
             PreparedStatement estatementpreparada = null;
             try {
                 estatementpreparada = miConexion.prepareStatement("delete from asignatura where ID_Asignatura = ?");
                 estatementpreparada.setInt(1, idUsuario);
                 int resultados = estatementpreparada.executeUpdate();
                 if(resultados > 0 ){
                     System.out.println("Se ha eliminado la asignatura");
                 }
             } catch (SQLException throwables) {
                 throwables.printStackTrace();
             } finally {
                 try{
                     if (estatementpreparada != null) {estatementpreparada.close (); }//cierr
                 }catch (SQLException e){
                    e.printStackTrace();
                 }
             }


        }

    /**
     * Añade una asignatura a la base de datos
     * @param miConexion objeto conexión para conectar con la BBDD
     */
    public static void anadirAsignatura(Connection miConexion){

            System.out.println("Escribe el nombre de la asignatura a añadir");
            String nombreAsignatura = lector.nextLine();

            boolean titulacionValida = false;
            int idTitulacion =-1;
            while (titulacionValida == false){
                System.out.println("Escribe el id de la titulacion a la que pertenece la asignatura. ");
                Administrador.verTitulaciones(miConexion);
                idTitulacion = lector.nextInt();
                lector.nextLine();
                titulacionValida = Administrador.comprobarTitulacion(idTitulacion, miConexion);
            }

            boolean profesorValido = false;
            String idProfesor = "";
            while(profesorValido== false){
                System.out.println("Escribe el dni del profesor de esta asignatura");
                Administrador.verProfesores(miConexion);
                idProfesor = lector.nextLine();
                profesorValido = Administrador.comprobarProfesor(idProfesor,miConexion);
            }

            String datosAsign = "insert into asignatura (Nombre_Asignatura, ID_titulacion, ID_profesor, Curso,Plazas_disponibles) values( ?,?,?,?,?) ";
            PreparedStatement estatementpreparada = null;
            try {
                estatementpreparada = miConexion.prepareStatement(datosAsign);
                estatementpreparada.setString(1, nombreAsignatura);
                estatementpreparada.setInt(2, idTitulacion);
                estatementpreparada.setString(3, idProfesor);
                estatementpreparada.setInt(4, 2021);
                estatementpreparada.setInt(5, 80);

                int filasMetidas = estatementpreparada.executeUpdate();
                if (filasMetidas > 0) {
                    System.out.println("Se Ha añadido la asignatura");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            }

    /**
     * Comprueba, dada una id, que la titulacion existe en la BBDD
     * @param idTitulacion id de la titulacion a buscar
     * @param con objeto conexión para conectar con la BBDD
     * @return false si el id no existe, true, si el id existe
     */
    public static boolean comprobarTitulacion( int idTitulacion, Connection con){
        boolean encontrado = false;
        try (PreparedStatement consulta = con.prepareStatement("select * from titulacion where ID_titulacion = ?")) {
            consulta.setInt(1, idTitulacion);
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
     * Lista todos los profesores
     * @param miConexion objeto conexión para conectar con la BBDD
     */
    public static void verProfesores(Connection miConexion){
        try (PreparedStatement consulta = miConexion.prepareStatement("select * from persona  where rol = 'profesor'  ")) {
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay profesores");
            } else {
                System.out.println("----Listado de profesores----");
                do {
                    String ID_profesor = resultados.getString("ID_persona");
                    String nombre = resultados.getString("nombre");
                    int edad = resultados.getInt("edad");
                    String telefono = resultados.getString("telefono");
                    System.out.println("DNI " +ID_profesor + " Nombre_profesor: "+ nombre +" Edad: "
                            +edad + "Telefono: " + telefono) ;
                } while(resultados.next());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Comprueba que la id dada pertenece a un profesor o no
     * @param idProfesor id del profesor a buscar
     * @param con objeto conexión para conectar con la BBDD
     * @return true si el id dado es de un profesor, false si no lo es
     */
        public static boolean comprobarProfesor( String idProfesor, Connection con){
            boolean encontrado = false;
            try (PreparedStatement consulta = con.prepareStatement("select * from persona where ID_persona = ? and rol = 'profesor'")) {
                consulta.setString(1, idProfesor);
                ResultSet resultado = consulta.executeQuery();

                if (resultado.next() == false) {
                    System.out.println("No se ha encontrado");
                    encontrado = false;
                }else{
                    System.out.println("se ha encontrado");
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
     * Lista todos los alumnos
     * @param miConexion objeto conexión para conectar con la BBDD
     */
    public static void verAlumnos(Connection miConexion){
        try (PreparedStatement consulta = miConexion.prepareStatement("select * from persona  where rol = 'alumno'  ")) {
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay alumnos");
            } else {
                System.out.println("----Listado de alumnos----");
                do {
                    String ID = resultados.getString("ID_persona");
                    String nombre = resultados.getString("nombre");
                    int edad = resultados.getInt("edad");
                    String telefono = resultados.getString("telefono");
                    System.out.println("DNI " +ID + " Nombre_alumno: "+ nombre +" Edad: "
                            +edad + " Telefono: " + telefono) ;
                    System.out.println("-----------------------");
                } while(resultados.next());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Lista todos los administradores
     * @param miConexion objeto conexión para conectar con la BBDD
     */
    public static void verAdministradores(Connection miConexion){
        try (PreparedStatement consulta = miConexion.prepareStatement("select * from persona  where rol = 'administrador'  ")) {
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay administradores");
            } else {
                System.out.println("----Listado de administradores----");
                do {
                    String ID = resultados.getString("ID_persona");
                    String nombre = resultados.getString("nombre");
                    int edad = resultados.getInt("edad");
                    String telefono = resultados.getString("telefono");
                    System.out.println("DNI " +ID + " Nombre_administrador: "+ nombre +" Edad: "
                            +edad + " Telefono: " + telefono) ;
                    System.out.println("-----------------------");
                } while(resultados.next());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Lista todos los bibliotecarios
     * @param miConexion objeto conexión para conectar con la BBDD
     */
    public static void verBibliotecarios(Connection miConexion){
        try (PreparedStatement consulta = miConexion.prepareStatement("select * from persona  where rol = 'bibliotecario'  ")) {
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay bibliotecarios");
            } else {
                System.out.println("----Listado de bibliotecarios----");
                do {
                    String id = resultados.getString("ID_persona");
                    String nombre = resultados.getString("nombre");
                    int edad = resultados.getInt("edad");
                    String telefono = resultados.getString("telefono");
                    System.out.println("DNI " +id + " Nombre_bibliotecario: "+ nombre +" Edad: "
                            +edad + " Telefono: " + telefono) ;
                    System.out.println("-----------------------");
                } while(resultados.next());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    }


