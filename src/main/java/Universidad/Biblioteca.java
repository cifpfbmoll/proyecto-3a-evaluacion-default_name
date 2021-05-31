package Universidad;

public class Biblioteca {
    private String ID_Biblioteca;

    // getters and setters
    public String getID_Biblioteca() {
        return ID_Biblioteca;
    }

    public void setID_Biblioteca(String ID_Biblioteca) {
        this.ID_Biblioteca = ID_Biblioteca;
    }

    // constructor con parametros
    public Biblioteca(String ID_Biblioteca) {
        this.ID_Biblioteca = ID_Biblioteca;
    }

    // constructor vacio
    public Biblioteca() {
    }

    //constructor copia
    public Biblioteca(Biblioteca b) {
        this.setID_Biblioteca(b.getID_Biblioteca());
    }

}
