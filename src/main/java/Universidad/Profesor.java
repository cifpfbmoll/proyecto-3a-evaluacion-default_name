package Universidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Profesor extends Persona{


    // ATRIBUTOS
    private String ID_Departamento;

    // CONSTRUCTOR VACÍO, CONSTRUCTOR CON PARÁMETROS, CONSTRUCTOR COPIA

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
     *
     * @param miConexion
     */
    public static void mostrarAlumnos(Connection miConexion) throws SQLException {
        Scanner lector = new Scanner(System.in);

        System.out.println("Quien eres? Escribe tu ID y tu contrasena para poder ver los alumnos");
        System.out.println("ID:");
        String id = lector.nextLine();
        System.out.println("Contrasena:");
        String contrasena = lector.nextLine();

        System.out.println("Estos son los alumnos que tienes:");

        PreparedStatement prepStat = miConexion.prepareStatement("SELECT * FROM Persona WHERE ID_Persona = ? AND Contrasena = ?");
        prepStat.setString(1, id);
        prepStat.setString(2, contrasena);

        ResultSet resultado = prepStat.executeQuery();

        while(resultado.next()){
            String trampitas = resultado.getString("ID_Persona") + " " + resultado.getString("Nombre") +
                    " " + resultado.getString("Edad") + " " + resultado.getString("Telefono");
            System.out.println(trampitas.replace(" ", " - "));
        }

    }
}
