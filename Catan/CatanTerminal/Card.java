package Catan.CatanTerminal;

class Card {

    ////////// Attributs ////////// 

    protected final String cardname;
    protected final int id;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    Card(String cardname, int id){
        this.cardname = cardname;
        this.id = id;
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {
        return this.cardname + " " + this.id;
    }

}