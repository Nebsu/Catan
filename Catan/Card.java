package Catan;

class Card {
    
    private String cardname;
    private int id;

    Card(String cardname, int id){
        this.cardname = cardname;
        this.id = id;
    }

    public String toString(){
        return this.cardname + " " + this.id;
    }

}