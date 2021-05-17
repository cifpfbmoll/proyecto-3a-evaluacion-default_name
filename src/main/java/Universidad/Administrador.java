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
            PreparedStatement consulta = con.prepareStatement("select * from persona");
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay usuarios.");
            } else {
                do {
                    String dni = resultados.getString("ID_Persona");
                    String nombre = resultados.getString("Nombre");
                    int edad = resultados.getInt("Edad");
                    String telefono = resultados.getString("Telefono");
                    String rol = resultados.getString("Rol");
                    String contrasena = resultados.getString("Contrasena");
                    System.out.println("DNI " +dni + " ROL: "+rol +" Contrasena: "
                            +contrasena + "Nombre: " + nombre + "Telefono: " + telefono + "Edad: " + edad);
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

            String datosRol = "";
            switch (p.getRol()) {
                case "administrador":
                    datosRol = "insert into administrador values( ?) ";
                    break;
                case "profesor":
                    datosRol = "insert into profesor values( ?, ?) ";
                    break;
                case "alumno":
                    datosRol = "insert into alumno values( ?) ";
                    break;
                case "bibliotecario":
                    datosRol = "insert into bibliotecario values( ?) ";
                    break;
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
     * Borra una persona de la BBD
     * @param con es un objeto Connection para contactar con la BBDD
     */
    public static void borrarPersona(Connection con){
        Administrador.verPersonas(con);
        boolean encontrado = false;
        PreparedStatement estatementpreparadaRol =null;
        PreparedStatement estatementpreparada = null;
        try {
            con.setAutoCommit(false);
            String dni = null;
            while (!encontrado) {
                Scanner lector = new Scanner(System.in);
                System.out.println("Escribe el DNI de la persona a eliminar");
                dni = lector.nextLine();
                encontrado = buscarDni(dni, con);
                System.out.println(encontrado);
            }
            String datosPersona = "delete from persona where ID_Persona = ?  ";
            estatementpreparada = con.prepareStatement(datosPersona);
            estatementpreparada.setString(1, dni);

            int filasBorradas = estatementpreparada.executeUpdate();

            con.commit();
            if(filasBorradas > 0  ){
                System.out.println("Se ha eliminado el registro");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                con.rollback() ;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }finally{
            try {
                if (estatementpreparada != null) {estatementpreparada.close (); }//cierr
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Obtiene el rol dado un usuario
     * @param con objeto conexion con la base de datos
     * @param dni el dni de la persona que quiero buscar
     * @return el rol de la persona buscada (administrador, profesor, alumno, bibliotecario)
     */
    public static String obtenerRol(Connection con, String dni){
        String rol = "";
        try{
            PreparedStatement consulta = con.prepareStatement("select * from persona where ID_Persona = ?");
            consulta.setString(1, dni);
            ResultSet resultados = consulta.executeQuery();
            if (resultados.next() == false) {
                System.out.println("No hay usuarios.");
            } else {
                do {
                    rol = resultados.getString("Rol");
                    System.out.println("el rol es "+rol);
                } while(resultados.next());
            }
            if (resultados != null) {resultados.close (); }//cierra
            if (consulta != null) consulta.close ();//cierra

        }catch(SQLException error){
            System.out.println("Error en la consulta.");
        }finally {
            return  rol;
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
            Scanner lector = new Scanner(System.in);
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
                Scanner lector = new Scanner(System.in);
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
        }finally {
            try{
                if (resultados != null) {resultados.close (); }//cierra
                if (consulta != null) consulta.close ();//cierra
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
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
}
