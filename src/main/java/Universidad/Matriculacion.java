package Universidad;

public class Matriculacion {
    //    Atributos
    private String ID_Alumno;
    private int ID_Asignatura;
    private int Ano_Academico;
    private double Nota;

//Constructores

    public Matriculacion() {
    }

    public Matriculacion(String ID_Alumno, int ID_Asignatura, int Ano_Academico, int Nota) {
        this.setID_Alumno(ID_Alumno);
        this.setID_Asignatura(ID_Asignatura);
        this.setAno_Academico(Ano_Academico);
        this.setNota(Nota);
    }

    public Matriculacion(Matriculacion m1) {
        this.setID_Alumno(m1.getID_Alumno());
        this.setID_Asignatura(m1.getID_Asignatura());
        this.setAno_Academico(m1.getAno_Academico());
        this.setNota(m1.getNota());
    }

//Getters y setters

    public String getID_Alumno() {
        return ID_Alumno;
    }

    public void setID_Alumno(String ID_Alumno) {
        this.ID_Alumno = ID_Alumno;
    }

    public int getID_Asignatura() {
        return ID_Asignatura;
    }

    public void setID_Asignatura(int ID_Asignatura) {
        this.ID_Asignatura = ID_Asignatura;
    }

    public int getAno_Academico() {
        return Ano_Academico;
    }

    public void setAno_Academico(int Ano_Academico) {
        this.Ano_Academico = Ano_Academico;
    }

    public double getNota() {
        return Nota;
    }

    public void setNota(double Nota) {
        this.Nota = Nota;
    }

    //    toString, lo pongo pero no sé si es necesario.
    @Override
    public String toString() {
        return "Matriculacion{" + "ID_Alumno=" + ID_Alumno + ", ID_Asignatura=" + ID_Asignatura + ", A\u00f1o_Academico=" + Ano_Academico + ", Nota=" + Nota + '}';
    }


}
