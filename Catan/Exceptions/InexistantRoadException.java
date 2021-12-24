package Catan.Exceptions;

public final class InexistantRoadException extends Exception {
    
    private final String message;

    public InexistantRoadException() {
        this.message = "Erreur : Une route doit etre liee a une des vos autres routes";
    }

    @Override
    public String toString() {
        return this.message;
    }

}