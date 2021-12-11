package Catan.Exceptions;

public class InexistantColonyException extends Exception {
    
    private final String message;

    public InexistantColonyException() {
        this.message = "Erreur : Une route doit être collée à une de vos colonies/villes";
    }

    @Override
    public String toString() {
        return (this.message+"\n");
    }

}