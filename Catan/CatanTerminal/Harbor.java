package Catan.CatanTerminal;

final class Harbor extends SeaBox {

    ////////// Attributs //////////

    final int price;
    final String ressource;
    final String type;
    final int id;
    private static int count = 1;

    ////////// Constructeurs et fonctions associées à ce dernier //////////

    Harbor(Location loc1, Location loc2, int price, String ressource) {
        super(loc1, loc2);
        if (ressource==null && price==3) this.type = "Simple";
        else if (ressource!=null && price==2) this.type = "Special";
        else throw new IllegalArgumentException();
        this.price = price;
        this.ressource = ressource;
        this.id = count++;
    }
    Harbor(Location loc1, Location loc2) {
        this(loc1, loc2, 3, null);
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {
        return (CatanTerminal.CYAN_BOLD_BRIGHT+"P"+
        ((this.type=="Simple")? "S?" : this.ressource.substring(0, 2))+
        CatanTerminal.RESET);
    }
    public String toStringWithId() {
        return (CatanTerminal.CYAN_BOLD_BRIGHT+"P"+
        ((this.type=="Simple")? "S?" : this.ressource.substring(0, 2))+
        " "+this.id+CatanTerminal.RESET);
    }
    
}