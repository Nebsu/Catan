package Catan.Exceptions;

public final class InvalidNameException extends Exception {
 
    private final String message;

    public InvalidNameException() {
        this.message = "Erreur : Veuillez entrer un nom qui contient au moins un caractère";
    }

    @Override
    public String toString() {
        return this.message;
    }

}