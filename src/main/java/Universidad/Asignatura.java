package Universidad;

public class Asigantura {

    private int ID_Asignatura;
    private string Nombre_Asignatura;
    private int ID_Titulacion;
    private int ID_Profesor;
    private string Curso;

    //Constructor vacío
    public Asignatura(){
    }

    // Constructor con parámetros
    public Asignatura(String ID_Asignatura, String Nombre_Asignatura, int ID_Titulacion, int ID_Profesor, String Curso) {

        this.setID_Asigantura(ID_Asigantura);
        this.setNombre_Asignatura(Nombre_Asignaatura);
        this.setID_Tituacion(ID_Titulacion);
        this.setID_Profesor(ID_Profesor);
        this.setCurso(Curso);
    }

    // Constructor copia
    public Asigantura(Asignatura copiaAsignatura){

       this.setID_Asigantura(copiaAsignatura.getID_Asigantura);
       this.setNombre_Asignatura(copiaAsignatura.getNombre_Asignaatura);
       this.setID_Tituacion(copiaAsignatura.getID_Titulacion);
       this.setID_Profesor(copiaAsignatura.getID:Profesor);
       this.setCurso(copiaAsignatura.getCurso);
    }

    // Getters & Setters
    public int getID_Asignatura() {
        return ID_Asignatura;
    }

    public void setID_Asignatura(int ID_Asignatura) {
        this.ID_Asignatura = ID_Asignatura;
    }

    public string getNombre_Asignatura() {
        return Nombre_Asignatura;
    }

    public void setNombre_Asignatura(string nombre_Asignatura) {
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

    public string getCurso() {
        return Curso;
    }

    public void setCurso(string curso) {
        Curso = curso;
    }

    // toString
    @java.lang.Override
    public java.lang.String toString() {
        return "Asigantura{" +
                "ID_Asignatura=" + ID_Asignatura +
                ", Nombre_Asignatura=" + Nombre_Asignatura +
                ", ID_Titulacion=" + ID_Titulacion +
                ", ID_Profesor=" + ID_Profesor +
                ", Curso=" + Curso +
                '}';
    }

    // Método de mostrar asignaturas en la clase Titulación

}
