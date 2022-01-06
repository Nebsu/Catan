package Catan.Exceptions;

public final class NotEnoughRessourcesException extends Exception {

    private final String message;

    public NotEnoughRessourcesException() {
        this.message = "Erreur : vous n'avez pas assez de ressources pour utiliser ce port";
    }

    @Override
    public String toString() {
        return this.message;
    }
    
}