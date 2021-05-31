package Universidad;

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

    }