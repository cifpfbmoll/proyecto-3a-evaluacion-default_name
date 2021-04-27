package Universidad;

public class Libros_Reservados {
    //    Atributos
    private String ID_Alumno;
    private String Titulo_Libro;
    private String Fecha_Reserva;
    private String Fecha_Devolucion;

//Constructores

    public Libros_Reservados() {
    }

    public Libros_Reservados(String ID_Alumno, String Titulo_Libro, String Fecha_Reserva, String Fecha_Devolucion) {
        this.setID_Alumno(ID_Alumno);
        this.setTitulo_Libro(Titulo_Libro);
        this.setFecha_Reserva(Fecha_Reserva);
        this.setFecha_Devolucion(Fecha_Devolucion);
    }

    public Libros_Reservados(Libros_Reservados lr1) {
        this.setID_Alumno(lr1.getID_Alumno());
        this.setTitulo_Libro(lr1.getTitulo_Libro());
        this.setFecha_Reserva(lr1.getFecha_Reserva());
        this.setFecha_Devolucion(lr1.getFecha_Devolucion());
    }

//Getters y setters

    public String getID_Alumno() {
        return ID_Alumno;
    }

    public void setID_Alumno(String ID_Alumno) {
        this.ID_Alumno = ID_Alumno;
    }

    public String getTitulo_Libro() {
        return Titulo_Libro;
    }

    public void setTitulo_Libro(String Titulo_Libro) {
        this.Titulo_Libro = Titulo_Libro;
    }

    public String getFecha_Reserva() {
        return Fecha_Reserva;
    }

    public void setFecha_Reserva(String Fecha_Reserva) {
        this.Fecha_Reserva = Fecha_Reserva;
    }

    public String getFecha_Devolucion() {
        return Fecha_Devolucion;
    }

    public void setFecha_Devolucion(String Fecha_Devolucion) {
        this.Fecha_Devolucion = Fecha_Devolucion;
    }

//    toString, lo pongo pero no sé si es necesario.

    @Override
    public String toString() {
        return "Libros_Reservados{" + "ID_Alumno=" + ID_Alumno + ", Titulo_Libro=" + Titulo_Libro + ", Fecha_Reserva=" + Fecha_Reserva + ", Fecha_Devolucion=" + Fecha_Devolucion + '}';
    }

}
