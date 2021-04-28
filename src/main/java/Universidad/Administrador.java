package Universidad;

public class Administrador extends Persona{
    
    // ATRIBUTOS

    private String ID_Persona;

    // CONSTRUCTOR VACÍO, CONSTRUCTOR CON PARÁMETROS Y CONSTRUCTOR COPIA

    public Administrador() {
    }

    public Administrador(String ID_Persona, String nombre, int edad, String telefono, String contrasena, int ID_Persona1) {
        super(ID_Persona, nombre, edad, telefono, contrasena);
        this.setID_Persona(ID_Persona);
    }

    public Administrador(Administrador copiaAdministrador){
        super((Persona)copiaAdministrador);
        this.setID_Persona(copiaAdministrador.getID_Persona());
    }

    // GETTERS & SETTERS

    @Override
    public String getID_Persona() {
        return ID_Persona;
    }

    public void setID_Persona(String ID_Persona) {
        this.ID_Persona = ID_Persona;
    }
}
