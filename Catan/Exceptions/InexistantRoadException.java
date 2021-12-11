package Catan.Exceptions;

public class InexistantRoadException extends Exception {
    
    private final String message;

    public InexistantRoadException() {
        this.message = "Erreur : Une route doit être liée à une des vos autres routes";
    }

    @Override
    public String toString() {
        return (this.message+"\n");
    }

}