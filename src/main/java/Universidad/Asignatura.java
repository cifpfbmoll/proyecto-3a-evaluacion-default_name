package Universidad;

public class Asignatura {

    private int ID_Asignatura;
    private String Nombre_Asignatura;
    private int ID_Titulacion;
    private int ID_Profesor;
    private String Curso;

    //Constructor vacío
    public Asignatura(){
    }

    // Constructor con parámetros
    public Asignatura(int ID_Asignatura, String Nombre_Asignatura, int ID_Titulacion, int ID_Profesor, String Curso) {

        this.setID_Asignatura(ID_Asignatura);
        this.setNombre_Asignatura(Nombre_Asignatura);
        this.setID_Titulacion(ID_Titulacion);
        this.setID_Profesor(ID_Profesor);
        this.setCurso(Curso);
    }

    // Constructor copia
    public Asignatura(Asignatura copiaAsignatura){

        this.setID_Asignatura(copiaAsignatura.getID_Asignatura());
        this.setNombre_Asignatura(copiaAsignatura.getNombre_Asignatura());
        this.setID_Titulacion(copiaAsignatura.getID_Titulacion());
        this.setID_Profesor(copiaAsignatura.getID_Profesor());
        this.setCurso(copiaAsignatura.getCurso());
    }

    // Getters & Setters
    public int getID_Asignatura() {
        return ID_Asignatura;
    }

    public void setID_Asignatura(int ID_Asignatura) {
        this.ID_Asignatura = ID_Asignatura;
    }

    public String getNombre_Asignatura() {
        return Nombre_Asignatura;
    }

    public void setNombre_Asignatura(String nombre_Asignatura) {
        Nombre_Asignatura = nombre_Asignatura;
    }

    public int getID_Titulacion() {
        return ID_Titulacion;
    }

    public void setID_Titulacion(int ID_Titulacion) {
        this.ID_Titulacion = ID_Titulacion;
    }

    public int getID_Profesor() {
        return ID_Profesor;
    }

    public void setID_Profesor(int ID_Profesor) {
        this.ID_Profesor = ID_Profesor;
    }

    public String getCurso() {
        return Curso;
    }

    public void setCurso(String curso) {
        Curso = curso;
    }

    // toString
    @java.lang.Override
    public java.lang.String toString() {
        return "Asignatura{" +
                "ID_Asignatura=" + ID_Asignatura +
                ", Nombre_Asignatura=" + Nombre_Asignatura +
                ", ID_Titulacion=" + ID_Titulacion +
                ", ID_Profesor=" + ID_Profesor +
                ", Curso=" + Curso +
                '}';
    }

    // Método de mostrar asignaturas en la clase Titulación

}