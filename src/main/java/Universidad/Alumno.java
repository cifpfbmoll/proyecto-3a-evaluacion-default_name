package Universidad;

public class Alumno {

    private int ID_Persona;

    public Almuno (){
    }

    public Alumno(int ID_Persona);{
        this.setID_Persona(ID_Persona);
    }

    public Alumno(Alumno copiaAlumno);{
        this.setID_Persona(copiaAlumno.getID_Persona);
    }

    public int getID_Persona() {
        return ID_Persona;
    }

    public void setID_Persona(int ID_Persona) {
        this.ID_Persona = ID_Persona;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Alumno{" +
                "ID_Persona=" + ID_Persona +
                '}';
    }
}