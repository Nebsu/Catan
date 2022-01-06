package Catan.Exceptions;

public final class InexistantColonyException extends Exception {
    
    private final String message;

    public InexistantColonyException() {
        this.message = "Erreur : Une route doit etre collee a une de vos colonies/villes";
    }

    @Override
    public String toString() {
        return this.message;
    }

}