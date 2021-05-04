package Universidad;

public class Alumno extends Persona{


    public Alumno () {
    }

    public Alumno(String ID_Persona, String nombre, int edad, String telefono, String contrasena) {
        super(ID_Persona, nombre, edad, telefono, contrasena);

    public Alumno(Alumno copiaAlumno) {
            super((Persona)copiaAlumno);
        }

    }