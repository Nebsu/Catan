package Catan.Exceptions;

public final class WrongInputException extends Exception {
    
    public static final String MESSAGE = "Erreur : Veuillez tapez une entrée valide !";

    @Override
    public String toString() {
        return (MESSAGE);
    }

}