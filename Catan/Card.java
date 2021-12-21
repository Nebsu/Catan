package Catan;

class Card {

    private final String cardname;
    private final int id;

    Card(String cardname, int id){
        this.cardname = cardname;
        this.id = id;
    }

    public String getCardname() {
        return cardname;
    }

    public int getId() {
        return id;
    }

    public String toString(){
        return this.cardname + " " + this.id;
    }

}