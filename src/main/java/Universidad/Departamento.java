package Universidad;

public class Departamento {
    //    Atributos
    private String ID_Departamento;
    private String Nombre_Departamento;

    //Constructores
    public Departamento() {
    }

    public Departamento(String ID_Departamento, String Nombre_Departamento) {
        this.setID_Departamento(ID_Departamento);
        this.setNombre_Departamento(Nombre_Departamento);
    }

    public Departamento(Departamento d1) {
        this.setID_Departamento(d1.getID_Departamento());
        this.setNombre_Departamento(d1.getNombre_Departamento());
    }
    //Getters y setters

    public String getID_Departamento() {
        return ID_Departamento;
    }

    public void setID_Departamento(String ID_Departamento) {
        this.ID_Departamento = ID_Departamento;
    }

    public String getNombre_Departamento() {
        return Nombre_Departamento;
    }

    public void setNombre_Departamento(String Nombre_Departamento) {
        this.Nombre_Departamento = Nombre_Departamento;
    }

//    toString

    @Override
    public String toString() {
        return "Departamento{" + "ID_Departamento=" + ID_Departamento + ", Nombre_Departamento=" + Nombre_Departamento + '}';
    }

//    Metodos pendientes de crear.
}
