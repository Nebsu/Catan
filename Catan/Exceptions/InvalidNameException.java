package Catan.Exceptions;

public class InvalidNameException extends Exception {
 
    private String message;

    public InvalidNameException() {
        this.message = "Erreur : Veuillez entrer un nom qui contient au moins un caractère";
    }

    @Override
    public String toString() {
        return (this.message+"\n");
    }

}