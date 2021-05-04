package Universidad;

public class Administrador extends Persona{

    // CONSTRUCTOR VACÍO, CONSTRUCTOR CON PARÁMETROS Y CONSTRUCTOR COPIA

    public Administrador() {
    }

    public Administrador(String ID_Persona, String nombre, int edad, String telefono, String contrasena) {
        super(ID_Persona, nombre, edad, telefono, contrasena);
    }

    public Administrador(Administrador copiaAdministrador){
        super((Persona)copiaAdministrador);
    }
}
