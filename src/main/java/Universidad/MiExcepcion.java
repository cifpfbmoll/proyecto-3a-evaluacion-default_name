package Universidad;

public class MiExcepcion extends Exception {
    private int codigoError;

    public MiExcepcion(int codigoError) {
        super();
        this.codigoError = codigoError;
    }

    @Override
    public String getMessage() {
        String mensaje="";
        switch(codigoError){
            case 1:
                mensaje="Tipo de dato incorrecto";
                break;
            case 2:
                mensaje="Te has equivocado demasiadas veces";
                break;
        }

        return mensaje;
    }
}
