package Universidad;

public class Bibliotecario extends Persona{
    //ATRÍBUTOS -- la clase no tiene atríbutos porque los hereda directamenete todos de persona

    //constructor vacío
    public Bibliotecario(){
    }


    //constructor con parametros
    public Bibliotecario(String ID_Persona, String nombre, int edad, String telefono, String contrasena) {
        super(ID_Persona, nombre, edad, telefono, contrasena);
    }

    //constructor copia
    public Bibliotecario(Bibliotecario copiaBibliotecario){
        super((Persona)copiaBibliotecario);
    }

}