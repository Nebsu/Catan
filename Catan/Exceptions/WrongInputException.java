package Catan.Exceptions;

public final class WrongInputException extends Exception {
    
    public static final String message = 
    "Erreur : Veuillez tapez une entrée valide !";

    @Override
    public String toString() {
        return (message);
    }

}