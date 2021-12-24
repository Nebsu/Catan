package Catan;

class Card {

    protected final String cardname;
    protected final int id;

    Card(String cardname, int id){
        this.cardname = cardname;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.cardname + " " + this.id;
    }

}