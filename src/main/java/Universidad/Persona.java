package Universidad;

public abstract class Persona {
    private String ID_Persona;
    private String Nombre;
    private int Edad;
    private String Telefono;
    private String Contraseña;

    // getters y setters
    public String getID_Persona() {
        return ID_Persona;
    }

    public void setID_Persona(String ID_Persona) {
        this.ID_Persona = ID_Persona;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getEdad() {
        return Edad;
    }

    public void setEdad(int edad) {
        Edad = edad;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    // constructor completo
    public Persona(String ID_Persona, String nombre, int edad, String telefono, String contraseña) {
        this.ID_Persona = ID_Persona;
        this.Nombre = nombre;
        this.Edad = edad;
        this.Telefono = telefono;
        this.Contraseña = contraseña;
    }

    // constructor vacio
    public Persona() {
    }

    // constructor copia
    public Persona(Persona p) {
        this.setID_Persona(p.getID_Persona());
        this.setNombre(p.getNombre());
        this.setEdad(p.getEdad());
        this.setTelefono(p.getTelefono());
        this.setContraseña(p.getContraseña());
    }

    // metodo identificarse (este método no se que recibe ni que devuelve, lo dejo asi de momento)
    public static void identificarse(ArrayList<Persona> listaPersonas) {

        pass;
    }

    //método buscar info personas (este método no se que recibe ni que devuelve, lo dejo asi de momento)
    public static void buscarInfoPersonas(ArrayList<Persona> listaPersonas) {
        pass;
    }

}
