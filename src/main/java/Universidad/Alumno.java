package Universidad;

public class Alumno extends Persona{


    public Alumno () {
    }

    public Alumno(String ID_Persona, String nombre, int edad, String telefono, String contrasena, String rol) {
        super(ID_Persona, nombre, edad, telefono, contrasena, rol);
    }
    public Alumno(Alumno copiaAlumno) {
        super((Persona)copiaAlumno);
        }

    }