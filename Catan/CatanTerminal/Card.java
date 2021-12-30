package Catan.CatanTerminal;

public class Card {

    ////////// Attributs ////////// 

    protected final String cardname;
    public final int id;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    public Card(String cardname, int id){
        this.cardname = cardname;
        this.id = id;
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {
        return this.cardname;
    }

}