package Catan.Exceptions;

public class WrongInputException extends Exception {
    
    private String message;

    public WrongInputException() {
        this.message = "Erreur : Veuillez tapez une entrée valide";
    }

    @Override
    public String toString() {
        return this.message;
    }

}